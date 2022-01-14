package com.example.plandial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    SpinnableImageView mSpinnableImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSpinnableImageView = (SpinnableImageView) findViewById(R.id.main_dial);
        mRecyclerView = (RecyclerView) findViewById(R.id.urgent_dials);

        {
            // urgent dial 표시 설정
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            mRecyclerView.setLayoutManager(layoutManager);

            UrgentDialAdapter adapter = new UrgentDialAdapter();
            mRecyclerView.setAdapter(adapter);
        }
    }
}