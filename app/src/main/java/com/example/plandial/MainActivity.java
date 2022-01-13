package com.example.plandial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.time.OffsetDateTime;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView= findViewById(R.id.recyclerview);
    SpinnableImageView spinnableImageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnableImageView = findViewById(R.id.main_dial);



    }
}