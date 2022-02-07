package com.example.plandial;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class EditDialActivity extends AppCompatActivity implements TextView.OnEditorActionListener {
    private final DialManager dialManager = DialManager.getInstance();
    private final IconRecommendation iconRecommendation = new IconRecommendation();
    private DialEditingViewModel dialEditingViewModel;
    private Intent intent;
    private Dial dial;

    private EditText Input_DialName;
    private ImageButton backButton;
    private TextView period, unitOfTime;
    private ImageButton periodPlus,  periodMinus;
    private ImageButton unitOfTimePlus, unitOfTimeMinus;
    int countForPeriod = 0;
    private int countForUnitOfTime = 0;
    private ArrayList<String> TimeArray = new ArrayList<String>(Arrays.asList("분","시간","일","주","개월"));
    private ImageView iconImage;
    private LinearSelector unitSelector;
    private Switch unableSwitch;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dial);

        {
            //  값 불러오기
            intent = getIntent();
            Category category = dialManager.getCategoryByName(intent.getStringExtra("categoryName"));
            dial = category.getDialByName(intent.getStringExtra("dialName"));

            // 값 보여주기
            Input_DialName = findViewById(R.id.Input_DialName);
            iconImage = findViewById(R.id.DialImage_Recommend);
            unitSelector = findViewById(R.id.unit_selector);
            unableSwitch = findViewById(R.id.switchButton);

            Input_DialName.setText(dial.getName());
            iconImage.setImageResource(dial.getIcon());
            // (수정 필요) unitSelector도 이동해야 함.
            unableSwitch.setChecked(dial.isDisabled());
        }

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
        // 아이콘 표시 설정
        Input_DialName.setOnEditorActionListener(this);

        // 저장
        dialEditingViewModel = new DialEditingViewModel(this, dial);

        //region 완료 버튼 설정
        {
            Button completeButton = findViewById(R.id.DialEdit_Button);
            completeButton.setOnClickListener(view -> dialEditingViewModel.complete());
        }
        //endregion
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (v.getId() == R.id.Input_DialName && actionId == EditorInfo.IME_ACTION_DONE) {
            String text = v.getText().toString();

            if (text.length() == 0) {
                iconImage.setImageBitmap(null);
            } else {
                int imageId = iconRecommendation.getUnknownImage();
                if (iconRecommendation.getIsReady()) {
                    imageId = iconRecommendation.getIconByName(this, text);
                }
                iconImage.setImageResource(imageId);
            }
        }
        return false;
    }
}