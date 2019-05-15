package com.example.final_project;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

//    private ListView lv1;

    private int[] imagesId={R.drawable.u1,R.drawable.u2,R.drawable.u3,R.drawable.u4};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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



        ArrayList<String> myDataset = new ArrayList<>();
        for(int i = 0; i < imagesId.length; i++){
            myDataset.add(imagesId[i] + "");
        }
        MyAdapter myAdapter = new MyAdapter(myDataset);
        RecyclerView mList = (RecyclerView) findViewById(R.id.list_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mList.setLayoutManager(layoutManager);
        mList.setAdapter(myAdapter);

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

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private List<String> mData;

        public class ViewHolder extends RecyclerView.ViewHolder {
//            public TextView mTextView;
            public ImageView face;
            public ViewHolder(View v) {
                super(v);
//                mTextView = (TextView) v.findViewById(R.id.info_text);
                face  = (ImageView)v.findViewById(R.id.img);
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
        public void onBindViewHolder(ViewHolder holder, int position) {
//            holder.mTextView.setText(mData.get(position));

            holder.face.setImageResource(Integer.valueOf(mData.get(position)));


        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }

}
