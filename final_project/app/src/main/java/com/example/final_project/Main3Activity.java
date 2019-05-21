package com.example.final_project;

import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Main3Activity extends AppCompatActivity {

    private int[] imagesId={R.drawable.u1,R.drawable.u2,R.drawable.u3,R.drawable.u4};
    private String[] name = {"小王1","小王2","小王3","小王4"};
    private double[] ratio = {18.24,15.26,11.48,10.55};
    private SeekBar seekbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        seekbar = (SeekBar) findViewById(R.id.seekBar);
        seekbar.setProgress(3);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                               @Override
                                               public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                                   Log.d("now",Integer.toString(progress));

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

        ArrayList<String> myDataset = new ArrayList<>();
        for(int i = 0; i < imagesId.length; i++){
            myDataset.add(imagesId[i] + "");
        }
        MyAdapter myAdapter = new MyAdapter(myDataset);
        RecyclerView mList = (RecyclerView) findViewById(R.id.result_view);
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
//                mTextView = (TextView) v.findViewById(R.id.info_text);
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



