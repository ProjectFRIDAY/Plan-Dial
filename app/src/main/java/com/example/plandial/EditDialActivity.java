package com.example.plandial;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class EditDialActivity extends AppCompatActivity {
    private Intent intent;
    String dialName;
    private ImageButton backButton;
    private TextView period, unitOfTime;
    private ImageButton periodPlus,  periodMinus;
    private ImageButton unitOfTimePlus, unitOfTimeMinus;
    int countForPeriod = 0;
    private int countForUnitOfTime = 0;
    private ArrayList<String> TimeArray = new ArrayList<String>(Arrays.asList("분","시간","일","주","개월"));

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dial);
        EditText Input_DialName = findViewById(R.id.Input_DialName);

        intent = getIntent();
        Input_DialName.setText(intent.getStringExtra("dialName"));

        period = findViewById(R.id.DialTime_Period);
        periodPlus =findViewById(R.id.Period_Up);
        periodMinus = findViewById(R.id.Period_Down);
        unitOfTime = findViewById(R.id.DialTime_UnitOfTime);
        unitOfTime.setText("");
        unitOfTimePlus = findViewById(R.id.UnitOfTime_Up);
        unitOfTimeMinus = findViewById(R.id.UnitOfTime_Down);

        {
            // 뒤로가기 버튼 설정
            backButton = findViewById(R.id.BackButton);
            backButton.setOnClickListener(view -> finish());
        }

        periodPlus.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                handler_up.post(runnable_up);
                return false;
            }
        });

        periodPlus.setOnTouchListener(new View.OnTouchListener() { //터치 이벤트 리스너 등록(누를때)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) { //눌렀을 때 동작
                    countForPeriod++;
                    setPeriod();
                }
                if (event.getAction() == MotionEvent.ACTION_UP) { //뗐을 때 동작
                    handler_up.removeCallbacks(runnable_up);
                }
                return false;
            }
        });
        periodMinus.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {    // 길게 누르면 동작
                handler_down.post(runnable_down);
                return false;
            }
        });

        periodMinus.setOnTouchListener(new View.OnTouchListener() { //터치 이벤트 리스너 등록(누를때)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) { //눌렀을 때 동작
                    countForPeriod--;
                    setPeriod();
                }
                if (event.getAction() == MotionEvent.ACTION_UP) { //뗐을 때 동작
                    handler_down.removeCallbacks(runnable_down);
                }
                return false;
            }
        });

        unitOfTimePlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (countForUnitOfTime <4) {
                    countForUnitOfTime++;
                    unitOfTime.setText(TimeArray.get(countForUnitOfTime));
                }
            }
        });

        unitOfTimeMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (countForUnitOfTime > 0){
                    countForUnitOfTime--;
                    unitOfTime.setText(TimeArray.get(countForUnitOfTime));
                }
            }
        });
    }

    private Handler handler_up = new Handler();
    private Runnable runnable_up = new Runnable() {
        @Override
        public void run() {
            // Print out your letter here...
            countForPeriod++;
            setPeriod();
            // Call the runnable again
            handler_up.postDelayed(this, 100);
        }
    };

    private Handler handler_down = new Handler();
    private Runnable runnable_down = new Runnable() {
        @Override
        public void run() {
            // Print out your letter here...
            countForPeriod--;
            setPeriod();
            // Call the runnable again
            handler_down.postDelayed(this, 100);
        }
    };

    public void setPeriod() {
        if (countForPeriod <= 100 && countForPeriod >= 0) {
            period.setText(""+countForPeriod);
        } else {
            if (countForPeriod > 100) {
                countForPeriod = 100;
            }
            if (countForPeriod <0) {
                countForPeriod = 0;
            }
        }
    }
}