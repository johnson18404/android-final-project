package com.example.final_project;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.content.CursorLoader;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import id.zelory.compressor.Compressor;


import static android.media.MediaRecorder.VideoSource.CAMERA;

public class MainActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static int CAMERA_REQUEST_CODE = 1;
    private static int GALLERY_REQUEST_CODE = 2;
    private static int CROP_REQUEST_CODE = 3;

    private final String TAG = "main";
    private Mat mRgba;
    private Mat mGray;
    private File mCascadeFile;
    private CascadeClassifier mJavaDetector;

    // OpenCV parameter
    private float                  mRelativeFaceSize   = 0.0002f;
    private int                    mAbsoluteFaceSize   = 0;
    private float expandRatio = 0.3f;

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView actualImageView;
    private File actualImage;
    private File compressedImage;

    private ImageView thumbImageView;

    Bitmap compressedImageBitmap = null;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            if (status == LoaderCallbackInterface.SUCCESS) {
                    Log.d(TAG, "OpenCV loaded successfully");

                    // after loaded
                    // load cascade file
                    try {
                        // load cascade file from application resources
                        InputStream is = getResources().openRawResource(R.raw.lbpcascade_frontalface);
                        File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
                        mCascadeFile = new File(cascadeDir, "lbpcascade_frontalface.xml");
                        Log.d(TAG, cascadeDir.getAbsolutePath());
                        FileOutputStream os = new FileOutputStream(mCascadeFile);

                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = is.read(buffer)) != -1) {
                            os.write(buffer, 0, bytesRead);
                        }
                        is.close();
                        os.close();

                        mJavaDetector = new CascadeClassifier(mCascadeFile.getAbsolutePath());
                        if (mJavaDetector.empty()) {
                            Log.d(TAG, "Failed to load cascade classifier");
                            mJavaDetector = null;
                        }
                        else
                            Log.d(TAG, "Loaded cascade classifier from " + mCascadeFile.getAbsolutePath()); // load success

                        // mNativeDetector = new DetectionBasedTracker(mCascadeFile.getAbsolutePath(), 0);

                        cascadeDir.delete();

                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.d(TAG, "Failed to load cascade. Exception thrown: " + e);
                    }

                    // mOpenCvCameraView.enableView();
            }
            else {
                super.onManagerConnected(status);
            }
        }
    };

    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }

        //setting preference value
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String ratio_str = sp.getString(SettingsActivity.KEY_PREF_RATIO, "0.0002");
        try {
            mRelativeFaceSize = Float.parseFloat(ratio_str);
        }
        catch (Exception e) {
            ShowMsg("Invalid RelativeFaceSize. Using default value: 0.0002");
        }

        String enlarge_str = sp.getString(SettingsActivity.KEY_PREF_ENLARGE, "30%");
        enlarge_str = enlarge_str.replace('%', ' ');
        try {
            expandRatio = Float.parseFloat(enlarge_str)/100.0f;
        }
        catch (Exception e) {
            ShowMsg("Invalid expandRatio. Using default value: 30%");
        }

        // Log.d("preference",ratio_str + enlarge_str);
        // Toast.makeText(this,ratio_str + enlarge_str,Toast.LENGTH_SHORT).show();
        // Log.d("preference", enlarge_str);
        // Toast.makeText(this, enlarge_str,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // setup UI component
        thumbImageView = findViewById(R.id.choose_img);

        // init
        compressedImageBitmap = null;


//        //setting preference value
//        SharedPreferences sp = PreferenceManager
//                .getDefaultSharedPreferences(this);
//        String ratio_str = sp.getString
//                (SettingsActivity.KEY_PREF_RATIO, "0.0002");
//
//        try {
//            mRelativeFaceSize = Float.parseFloat(ratio_str);
//        }
//        catch (Exception e) {
//            ShowMsg("Invalid RelativeFaceSize. Using default value: 0.0002");
//        }
//
//        String enlarge_str = sp.getString
//                (SettingsActivity.KEY_PREF_ENLARGE, "30%");
//        enlarge_str = enlarge_str.replace('%', ' ');




//        Button button = (Button)findViewById(R.id.choose_file);
//
//        button.setOnClickListener(new Button.OnClickListener(){
//
//                                      @Override
//                                      public void onClick(View v) {
//                                          Intent intent = new Intent();
//                                          intent.setType("image/*");
//                                          intent.setAction(Intent.ACTION_GET_CONTENT);
//                                          startActivityForResult(intent, 1);
//
//                                      }
//                                  });

//        ImageButton camera = findViewById(R.id.camera_btn);
//        camera.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v){
//                ContentValues value = new ContentValues();
//                value.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
//                Uri uri= getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                        value);
//                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri.getPath());
//                startActivityForResult(intent, CAMERA);


//                Intent intent = new Intent(
//                        android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                File tmpFile = new File(
//                        Environment.getExternalStorageDirectory(), "image.jpg");
//                Uri outputFileUri = Uri.fromFile(tmpFile);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
//                startActivityForResult(intent, CAMERA);


//                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                startActivity(intent);
//
//            }
//
//        });




//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.restart) {
            Intent restart_intent = new Intent(this, MainActivity.class);
            startActivity(restart_intent);
            this.finish();
            return true;
        }
        else if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

            return true;
        }
        else if (id == R.id.about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //  v2
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && (requestCode == GALLERY_REQUEST_CODE)) {
            if (data == null) {
                // showError("Failed to open picture!");
                return;
            }
            try {

                InputStream inputStream = this.getContentResolver().openInputStream(data.getData());
                File tempFile = new File(this.getFilesDir(), "tmp.jpg");
                FileOutputStream out = new FileOutputStream(tempFile);
                copy(inputStream, out);
                inputStream.close();
                out.close();

                compressedImageBitmap = new Compressor(this).compressToBitmap(tempFile);

                thumbImageView.setImageBitmap(compressedImageBitmap);

            } catch (IOException e) {
                ShowMsg("Open picture fail !");
                e.printStackTrace();
            }
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            compressedImageBitmap = imageBitmap;

            thumbImageView.setImageBitmap(imageBitmap);
        }

    }

    // https://github.com/zetbaitsu
    private static final int EOF = -1;
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
    private static long copy(InputStream input, OutputStream output) throws IOException {
        long count = 0;
        int n;
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }


    private void save_to_tmp() {

        try {
            FileOutputStream stream = this.openFileOutput("thumb.jpg", Context.MODE_PRIVATE);
            compressedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);

            stream.close();
            // compressedImage.();
        }
        catch (Exception e) {
            ShowMsg("save to thumb error");
            e.printStackTrace();
        }

    }

    public void start(View view) {
        Log.d(TAG, "btn start click");

        // openCV face detect
        int facesNum = Analyze();
        if (facesNum==0) {
            ShowMsg("Can not detect any faces.");
            return;
        }
        else if (facesNum==-1) {
            ShowMsg("Please choose a photo or take a picture first.");
            return;
        }

        // save current to thumb.jpg. this will be used in main2, main3 thumb.
        save_to_tmp();

        // jump to activity2
        Intent intent = new Intent(this, Main2Activity.class);
        intent.putExtra("facesNum", facesNum);
        startActivity(intent);

        // release memory
        thumbImageView.setImageResource(android.R.color.transparent);
        compressedImageBitmap.recycle();
        compressedImageBitmap = null;
    }

    private int Analyze() {
        Log.d("main", "Analyze()");

        if (compressedImageBitmap == null) {
            return -1;
        }

        // load image
        // Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.t2);
        mRgba = new Mat();
        mGray = new Mat();
        Utils.bitmapToMat(compressedImageBitmap, mRgba);
        // Imgcodecs.imwrite(this.getFilesDir()+"/mRgba.jpg", mRgba);
        Imgproc.cvtColor(mRgba, mGray, Imgproc.COLOR_BGR2GRAY);

        int height = mGray.rows(), width = mGray.cols();

        // some pre-process, calc min face range.
        if (mAbsoluteFaceSize == 0) {
            if (Math.round(height * mRelativeFaceSize) > 0) {
                mAbsoluteFaceSize = Math.round(height * mRelativeFaceSize);
            }
            // mNativeDetector.setMinFaceSize(mAbsoluteFaceSize);
        }


        // face detect
        MatOfRect faces = new MatOfRect();

        if (mJavaDetector != null)
            mJavaDetector.detectMultiScale(mGray, faces, 1.1, 2, 2, // TODO: objdetect.CV_HAAR_SCALE_IMAGE
                    new Size(mAbsoluteFaceSize, mAbsoluteFaceSize), new Size());
        else {
            Log.d(TAG, "Detection method is not selected!");
        }


        expandRatio = 0.3f;

        // get result
        Rect[] facesArray = faces.toArray();
        Log.d(TAG, "detect finish. get: ");
        Log.d(TAG, String.valueOf(facesArray.length));

        if (facesArray.length==0) return 0;

        for (int i = 0; i < facesArray.length; i++) {

            // Imgproc.rectangle(mRgba, facesArray[i].tl(), facesArray[i].br(), FACE_RECT_COLOR, 3);
            Point a = facesArray[i].tl();
            Point b = facesArray[i].br();

            Log.d(TAG, String.valueOf(a.x) + "\t" + String.valueOf(a.y));
            Log.d(TAG, String.valueOf(b.x) + "\t" + String.valueOf(b.y));

            Rect rect = facesArray[i];
            // Rect rectCrop = new Rect(rect.x, rect.y, rect.width, rect.height);

            int dx = Math.round(rect.width * expandRatio);
            int dy = Math.round(rect.height * expandRatio);
            int x = rect.x - dx;;
            if (x < 0) x = 0;
            if (x >= width) x = width-1;
            int y = rect.y - dy;;
            if (y < 0) y = 0;
            if (y >= height) y = height-1;
            int w = Math.round(rect.width + dx * 2);
            if (x + w >= width) w = width - x - 1;
            int h = Math.round(rect.height + dy * 2);
            if (y + h >= height) h = height - y - 1;

            Log.d("main2", "width, height");
            Log.d("main2", String.valueOf(w)+"_____"+String.valueOf(h));

            Rect rectCrop = new Rect(x, y, w, h);

            Mat image_roi = new Mat(mRgba, rectCrop); // BGR


            Mat image_roi_rgb = new Mat();;
            Imgproc.cvtColor(image_roi, image_roi_rgb, Imgproc.COLOR_BGR2RGB);

            Mat image_roi_rgb_resize = new Mat();
            Imgproc.resize(image_roi_rgb, image_roi_rgb_resize, new Size(160, 160));
            Imgcodecs.imwrite(this.getFilesDir()+"/"+String.valueOf(i)+".jpg", image_roi_rgb_resize);

            image_roi_rgb.release();
            image_roi.release();
        }


        // release
        mRgba.release();
        mGray.release();

        return facesArray.length;
    }


    public void btnChooseImage(View view) {
        Log.d(TAG, "btnChooseImage click");

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // startActivityForResult(intent.createChooser(intent, "Please select a image."), 100);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    public void btnCamera(View view) {
        Log.d(TAG, "btnCamera click");

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void ShowMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
