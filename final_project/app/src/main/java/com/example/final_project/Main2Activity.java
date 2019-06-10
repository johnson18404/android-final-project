package com.example.final_project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {
    // global setup
    private String cwd;

    // UI
    Toolbar toolbar;
    RecyclerView mList;
    ImageView thumbImageView;

    // data
    int facesNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // init
        cwd = this.getFilesDir().getAbsolutePath() + "/";
        Log.d("cwd", cwd);

        // setup UI
        mList = (RecyclerView) findViewById(R.id.list_view);
        thumbImageView = findViewById(R.id.img_1);

        // intent receive data from MainActivity
        Intent intent = getIntent();
        facesNum = intent.getIntExtra("facesNum", -1);
        Log.d("main2", "facesNum");
        Log.d("facesNum", String.valueOf(facesNum));


        // setup thumb
        File thumbFile = new File(this.getFilesDir(), "thumb.jpg");
        Log.d("main2", thumbFile.getAbsolutePath());
        if (thumbFile.exists()) {
            Bitmap bmp = BitmapFactory.decodeFile(thumbFile.getAbsolutePath());
            thumbImageView.setImageBitmap(bmp);
        }


//        lv1 = (ListView) findViewById(R.id.list_view);
//
//        BaseAdapter adapter = new BaseAdapter() {

//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//
//                View layout=View.inflate(Main2Activity.this, R.layout.item, null);
//                ImageView face = (ImageView)layout.findViewById(R.id.img);
//
//                face.setImageResource(imagesId[position]);
//
//                return layout;
//            }


//            @Override
//            public int getCount() {
//                return imagesId.length;
//            }
//
//            @Override
//            public Object getItem(int position) {
//                return imagesId[position];
//            }
//
//            @Override
//            public long getItemId(int position) {
//                return position;
//            }
//
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//                View layout=View.inflate(Main2Activity.this, R.layout.item, null);
//                ImageView face = (ImageView)layout.findViewById(R.id.img);
//
//                face.setImageResource(imagesId[position]);
//
//                return layout;
//            }
//
//
//        };
//
//
//
//        lv1.setAdapter(adapter);



//        ArrayList<String> myDataset = new ArrayList<>();
//        for(int i = 0; i < imagesId.length; i++){
//            myDataset.add(imagesId[i] + "");
//
//            myDataset.add(String.valueOf(i)+".jpg");
//        }



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
    protected void onResume() {
        super.onResume();
        Log.d("main2", "onResume");

        ArrayList<String> myDataset = new ArrayList<>();
        for(int i = 0; i < facesNum; i++){
            myDataset.add(cwd+String.valueOf(i)+".jpg");
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
        if (id == R.id.restart) {
            Intent restart_intent = new Intent(this, MainActivity.class);
            startActivity(restart_intent);
            this.finish();
            return true;
        }
        else if (id == R.id.action_settings) {

            return true;
        }
        else if (id == R.id.about) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private List<String> mData;

        public class ViewHolder extends RecyclerView.ViewHolder {

            public ImageView face;
            public ImageButton imgbtn;
            public ViewHolder(View v) {
                super(v);

                face  = (ImageView)v.findViewById(R.id.img);
                imgbtn = (ImageButton)v.findViewById(R.id.imageButton);
            }
        }

        public MyAdapter(List<String> data) {
            mData = data;
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item, parent, false);
            ViewHolder vh = new ViewHolder(v);

            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
//            holder.mTextView.setText(mData.get(position));

            // holder.face.setImageResource(Integer.valueOf(mData.get(position)));
            // wrap with try ..
            Bitmap bmp = BitmapFactory.decodeFile(mData.get(position));
            holder.face.setImageBitmap(bmp);

            holder.imgbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("123","888");
                    Intent newAct = new Intent();
                    newAct.setClass( Main2Activity.this, Main3Activity.class );
                    newAct.putExtra("selectFace", position);
                    startActivity(newAct);

//                    Main2Activity.this.finish();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }

}
