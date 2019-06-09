package com.example.final_project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Main3Activity extends AppCompatActivity {

    // global setup
    final String thumb_face = "https://s3-ap-northeast-1.amazonaws.com/johnson18404-imtwebimages/public/imt_alignface_0906/";
    final String thumb_pic = "https://s3-ap-northeast-1.amazonaws.com/johnson18404-imtwebimages/public/imt_pictures_0906/";
    private String cwd;
    File filesDir;
    class Person {
        public String name;
        public int sex;
        public double ratio;
        public String url_face;
        public String url_pic;

        Person(String name, int sex, double ratio, String url_face, String url_pic) {
            this.name = name;
            this.sex = sex;
            this.ratio = ratio;
            this.url_face = url_face;
            this.url_pic = url_pic;
        }
    }

    private int[] imagesId={R.drawable.u1,R.drawable.u2,R.drawable.u3,R.drawable.u4};
    private String[] name = {"小王1","小王2","小王3","小王4"};
    private double[] ratio = {18.24,15.26,11.48,10.55};

    // UI
    Toolbar toolbar;
    SeekBar seekbar;
    RecyclerView mList;
    ImageView thumbImageView;
    ImageView faceImageView;


    // data
    int selectFace;
    String res;
    ArrayList<Person> list;



    private class GetResultFromServer extends AsyncTask<String, Integer, String> {


        @Override
        protected String doInBackground(String... params) {
            String attachmentFileName = params[0];

            String attachmentName = "image";
            String crlf = "\r\n";
            String twoHyphens = "--";
            String boundary =  "*****";
            File FileToUpload = new File(filesDir, attachmentFileName);

            HttpURLConnection conn = null;
            InputStream responseStream = null;
            BufferedReader responseStreamReader = null;
            StringBuilder stringBuilder = new StringBuilder();
            String response = "";

            try {
                conn = (HttpURLConnection) new URL("http://imt2019.iamss.cc:5000/lasso").openConnection();
                conn.setUseCaches(false);
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                // conn.setRequestProperty("Connection", "close");
                conn.setRequestProperty("Cache-Control", "no-cache");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                // content wrapper
                DataOutputStream request = new DataOutputStream(conn.getOutputStream());
                request.writeBytes(twoHyphens + boundary + crlf);
                request.writeBytes("Content-Disposition: form-data; name=\"" +
                        attachmentName + "\";filename=\"" +
                        attachmentFileName + "\"" + crlf);
                request.writeBytes(crlf);

                // write data
                FileInputStream inputStreamToLogFile = new FileInputStream(FileToUpload);
                int bytesRead;
                byte[] dataBuffer = new byte[1024];
                while((bytesRead = inputStreamToLogFile.read(dataBuffer)) != -1) {
                    request.write(dataBuffer, 0, bytesRead);
                }
                request.flush();

                // end content wrapper
                request.writeBytes(crlf);
                request.writeBytes(twoHyphens + boundary + twoHyphens + crlf);

                // flush output buffer
                request.flush();
                request.close();


                // get response
                responseStream = new BufferedInputStream(conn.getInputStream());
                responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));
            }
            catch (Exception e) {
                e.printStackTrace();
                return "network_error";
            }


            // try parse as much as possible
            int cnt = 0;
            try {
//                String line = "";
//                while ((line = responseStreamReader.readLine()) != null) {
//                    stringBuilder.append(line).append("\n");
//                }

                int ch;
                while ((ch=responseStreamReader.read()) != -1) {
                    // Log.d("ch", String.valueOf((char) ch));
                    stringBuilder.append((char) ch);

                    cnt += 1;
                }
            }
            catch (Exception e) {
                Log.d("error", "ignore error");
                // e.printStackTrace();
            }

            Log.d("cnt", String.valueOf(cnt));

            // concat parsing result
            response = stringBuilder.toString();
            Log.d("response", response);


            // finally close conn
            try {
                responseStreamReader.close();
                responseStream.close();
                conn.disconnect();
            }
            catch (Exception e) {
                e.printStackTrace();
                // ignore return
            }

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s.equals("network_error")) {
                Log.d("error", "network_error");
                res = "error";
                return;
            }

            res = s;
            try {
                JSONObject obj = new JSONObject(res);

                int offset = obj.getInt("offset");
                Log.d("offset", String.valueOf(offset));
                if (offset == -1) {
                    // can not found face
                    Log.d("result fail", "can not find face.");
                    return;
                }

                seekbar.setProgress(offset);

                JSONArray arr = obj.getJSONArray("list");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject t = arr.getJSONObject(i);

                    String name = t.getString("name");
                    int sex = t.getInt("sex");
                    double ratio = t.getDouble("ratio");
                    String url_face = thumb_face + t.getString("url_face");
                    String url_pic = thumb_pic + t.getString("url_pic");

                    list.add(new Person(name, sex, ratio, url_face, url_pic));

                    Log.d("name", name);
                    Log.d("url_pic", url_pic);
                }

            }
            catch (Exception e) {
                e.printStackTrace(); // JSON parsing error

            }
        }
    }


    private void ReArrange(int newoffset) {
        int male = newoffset;
        int female = 6 - newoffset;

        ArrayList<Person> newList = new ArrayList<>();

        for (Person p : list) {
            if (male==0 && female==0) break;

            if (male>0 && p.sex==1) {
                newList.add(p);
                male -= 1;
            }
            else if (female>0 && p.sex==0) {
                newList.add(p);
                female -= 1;
            }

        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // init
        cwd = this.getFilesDir().getAbsolutePath() + "/";
        filesDir = this.getFilesDir();
        Log.d("cwd", cwd);
        res = "";
        list = new ArrayList<>();

        // setup UI
        mList = (RecyclerView) findViewById(R.id.result_view);

        thumbImageView = findViewById(R.id.img1);
        faceImageView = findViewById(R.id.img2);

        seekbar = (SeekBar) findViewById(R.id.seekBar);
        seekbar.setProgress(3);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                               @Override
                                               public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                                   Log.d("now",Integer.toString(progress));

                                                   Log.d("res", res);
                                               }

                                               @Override
                                               public void onStartTrackingTouch(SeekBar seekBar) {

                                               }

                                               @Override
                                               public void onStopTrackingTouch(SeekBar seekBar) {


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


        // intent receive data from MainActivity
        Intent intent = getIntent();
        selectFace = intent.getIntExtra("selectFace", -1);
        Log.d("selectFace", String.valueOf(selectFace));



        // data
        // setup thumb
        File thumbFile = new File(this.getFilesDir(), "thumb.jpg");
        Log.d("thumbFile", thumbFile.getAbsolutePath());
        if (thumbFile.exists()) {
            Bitmap bmp = BitmapFactory.decodeFile(thumbFile.getAbsolutePath());
            thumbImageView.setImageBitmap(bmp);
        }

        // setup selectFace
        File faceFile = new File(this.getFilesDir(), String.valueOf(selectFace)+".jpg");
        Log.d("selectFace", faceFile.getAbsolutePath());
        if (faceFile.exists()) {
            Bitmap bmp = BitmapFactory.decodeFile(faceFile.getAbsolutePath());
            faceImageView.setImageBitmap(bmp);
        }


        // get result
        new GetResultFromServer().execute("2.jpg");


        ArrayList<String> myDataset = new ArrayList<>();
        for(int i = 0; i < imagesId.length; i++){
            myDataset.add(imagesId[i] + "");
        }
        MyAdapter myAdapter = new MyAdapter(myDataset);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mList.setLayoutManager(layoutManager);
        mList.setAdapter(myAdapter);
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

        // update recyclerview
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class MyAdapter extends RecyclerView.Adapter<Main3Activity.MyAdapter.ViewHolder> {
        private List<String> mData;

        public class ViewHolder extends RecyclerView.ViewHolder {
            //            public TextView mTextView;
            public ImageView img_small;
            public ImageView img_big;
            public TextView name;
            public TextView ratio;

            public ViewHolder(View v) {
                super(v);

                name = (TextView) v.findViewById(R.id.text_name);
                ratio = (TextView) v.findViewById(R.id.text_ratio);
                img_small  = (ImageView)v.findViewById(R.id.img_small);
                img_big  = (ImageView)v.findViewById(R.id.img_big);

            }
        }

        public MyAdapter(List<String> data) {
            mData = data;
        }

        @Override
        public Main3Activity.MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.result, parent, false);
            Main3Activity.MyAdapter.ViewHolder vh = new Main3Activity.MyAdapter.ViewHolder(v);

            return vh;
        }

        @Override
        public void onBindViewHolder(Main3Activity.MyAdapter.ViewHolder holder, int position) {
//            holder.mTextView.setText(mData.get(position));


            holder.name.setText(name[position]);
            holder.ratio.setText(Double.toString(ratio[position]));
            holder.img_small.setImageResource(Integer.valueOf(mData.get(position)));
            holder.img_big.setImageResource(Integer.valueOf(mData.get(position)));
//            holder.imgbtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
////                    Log.d("123","888");
////                    Intent newAct = new Intent();
////                    newAct.setClass( Main3Activity.this, Main3Activity.class );
////
////                    startActivity(newAct);
////
//////                    Main2Activity.this.finish();
//                }
//            });


        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }






}



