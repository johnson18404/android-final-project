package com.example.final_project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;

public class AboutActivity extends AppCompatActivity {

    RecyclerView mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        mList = (RecyclerView) findViewById(R.id.card_about);

        ArrayList<String> myDataset = new ArrayList<>();
        for(int i = 0; i < 1; i++){
            myDataset.add("Title"+String.valueOf(i));
        }

//        List<Member> myDataset = new ArrayList<Member>();
//        myDataset.add(new Member("title0","content0"));

        AboutActivity.MyAdapter myAdapter = new MyAdapter(myDataset);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mList.setLayoutManager(layoutManager);
        mList.setAdapter(myAdapter);

    }

    public class MyAdapter extends RecyclerView.Adapter<AboutActivity.MyAdapter.ViewHolder> {
        private List<String> mData;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView title;
            public TextView content;


            public ViewHolder(View v) {
                super(v);
                title = (TextView) v.findViewById(R.id.title);
                content = (TextView) v.findViewById(R.id.content);

            }
        }

        public MyAdapter(List<String> data) {
            mData = data;
        }

        @Override
        public AboutActivity.MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.about, parent, false);
            AboutActivity.MyAdapter.ViewHolder vh = new ViewHolder(v);

            return vh;
        }

        @Override
        public void onBindViewHolder(AboutActivity.MyAdapter.ViewHolder holder, final int position) {
//            holder.title.setText(mData.get(position));
//            holder.content.setText(mData.get(position));

        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }
}
