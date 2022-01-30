package com.example.plandial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;

import com.example.plandial.db.CategoryTable;
import com.example.plandial.db.DialTable;
import com.example.plandial.db.ICategoryDao;
import com.example.plandial.db.IDialDao;
import com.example.plandial.db.IPresetDao;
import com.example.plandial.db.PlanDatabase;
import com.example.plandial.db.PresetTable;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }
}