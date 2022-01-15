package com.example.plandial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

public class MainActivity extends AppCompatActivity {

    RecyclerView urgentDialView;
    SpinnableImageView spinnableImageView;
    RecyclerView categoryDialView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        urgentDialView = (RecyclerView) findViewById(R.id.urgent_dials);
        spinnableImageView = (SpinnableImageView) findViewById(R.id.main_dial);
        categoryDialView = (RecyclerView) findViewById(R.id.category_dials);

        {
            // urgent dial 표시 설정
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            urgentDialView.setLayoutManager(layoutManager);

            UrgentDialAdapter adapter = new UrgentDialAdapter();
            urgentDialView.setAdapter(adapter);

            // category dial 표시 설정
            GridLayoutManager gridlayoutManager = new GridLayoutManager(this,3);
            categoryDialView.setLayoutManager(gridlayoutManager);

            CategoryDialAdapter gridAdapter = new CategoryDialAdapter();
            categoryDialView.setAdapter(gridAdapter);

        }
    }
}