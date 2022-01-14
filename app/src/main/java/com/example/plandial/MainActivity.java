package com.example.plandial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.widget.ImageView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    SpinnableDialView mSpinnableDialView;
    ConstraintLayout mMainDial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSpinnableDialView = findViewById(R.id.main_dial_board);
        mMainDial = findViewById(R.id.main_dial);

        {
            ArrayList<ImageView> circles = new ArrayList<>();
            circles.add(findViewById(R.id.category_circle_0));
            circles.add(findViewById(R.id.category_circle_1));
            circles.add(findViewById(R.id.category_circle_2));
            circles.add(findViewById(R.id.category_circle_3));
            circles.add(findViewById(R.id.category_circle_4));
            circles.add(findViewById(R.id.category_circle_5));
            circles.add(findViewById(R.id.category_circle_6));
            circles.add(findViewById(R.id.category_circle_7));
            circles.add(findViewById(R.id.category_circle_8));
            circles.add(findViewById(R.id.category_circle_9));
            mSpinnableDialView.setCircles(this, circles);
        }
    }
}