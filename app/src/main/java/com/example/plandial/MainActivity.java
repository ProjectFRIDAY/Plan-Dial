package com.example.plandial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    SpinnableImageView mSpinnableImageView;
    RecyclerView mGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.urgent_dials);
        mSpinnableImageView = (SpinnableImageView) findViewById(R.id.main_dial);
        mGridView = (RecyclerView) findViewById(R.id.category_dials);

        {
            // urgent dial 표시 설정
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            mRecyclerView.setLayoutManager(layoutManager);

            UrgentDialAdapter adapter = new UrgentDialAdapter();
            mRecyclerView.setAdapter(adapter);

            // category dial 표시 설정
            GridLayoutManager gridlayoutManager = new GridLayoutManager(this,3);
            mGridView.setLayoutManager(gridlayoutManager);

            CategoryDialAdapter gridadapter = new CategoryDialAdapter();
            mGridView.setAdapter(gridadapter);

        }
    }
}