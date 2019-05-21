package com.example.final_project;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.content.CursorLoader;

import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView intro = (TextView) findViewById(R.id.intro);
        SpannableStringBuilder spannable = new SpannableStringBuilder(intro.getText().toString());
        spannable.setSpan(new AbsoluteSizeSpan(40), 16, 85, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        intro.setText(spannable);


        Button button = (Button)findViewById(R.id.choose_file);

        button.setOnClickListener(new Button.OnClickListener(){

                                      @Override
                                      public void onClick(View v) {
                                          Intent intent = new Intent();
                                          intent.setType("image/*");
                                          intent.setAction(Intent.ACTION_GET_CONTENT);
                                          startActivityForResult(intent, 1);

                                      }
                                  });




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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //當使用者按下確定後
        if (resultCode == RESULT_OK) {
            //取得圖檔的路徑位置
            Uri uri = data.getData();
            //寫log
            Log.e("uri", uri.toString());
            //抽象資料的接口
            ContentResolver cr = this.getContentResolver();
            try {
                //由抽象資料接口轉換圖檔路徑為Bitmap
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                //取得圖片控制項ImageView
                ImageView imageView = (ImageView) findViewById(R.id.choose_img);
                // 將Bitmap設定到ImageView
                imageView.setImageBitmap(bitmap);

                TextView mtext = findViewById(R.id.file_name);

                String path;
                int find;

                find = uri.getPath().lastIndexOf('/');
                path = uri.getPath().substring(find+1,uri.getPath().length());
                Log.d("path", path);

//                String path = "圖片路徑";
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inJustDecodeBounds = true;
//                BitmapFactory.decodeFile(path, options);
//                String type = options.outMimeType;
//                Log.d("image type -> ", type.toString());
                 mtext.setText("\n"+"                              "+path);

//                String[] proj = {MediaStore.Images.Media.DATA};
//                CursorLoader loader = new CursorLoader(this, uri, proj, null, null, null);
//                Cursor cursor = loader.loadInBackground();
//                if (cursor != null) {
//                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//                    cursor.moveToFirst();
//
//                    String test = cursor.getString(column_index);
//
////                    Log.d("path222", test.toString());
//
//
//
//                }
//                cursor.close();

//                Log.d("000",bitmap)
            } catch (FileNotFoundException e) {
                Log.e("Exception", e.getMessage(),e);
            }

//            //取得圖片控制項ImageView
//            ImageView imageView = (ImageView) findViewById(R.id.choose_img);
//            imageView.setImageURI(uri);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }



//    public void choose_file(View view) {
//    }

    public void start(View view) {
        Intent newAct = new Intent();
        newAct.setClass( this, Main2Activity.class );

        startActivity(newAct);

//        this.finish();
    }
}
