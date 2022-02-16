package com.friday.plandial;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.plandial.R;

import java.util.ArrayList;

public class PlusDialActivity extends AppCompatActivity implements TextView.OnEditorActionListener {
    private static final int VIBRATE_STRENGTH = 5;
    private Vibrator vibrator;

    private final IconRecommendation iconRecommendation = new IconRecommendation();
    private DialSettingViewModel dialSettingViewModel;

    private DateTimeTextView startDayInput;
    private ImageButton backButton;
    private ImageView iconImage;
    private EditText dialName;
    private TextView period, unitOfTime;
    private ImageButton periodPlus, periodMinus;
    private ImageButton unitOfTimePlus, unitOfTimeMinus;

    int countForPeriod = 1;
    private int countForUnitOfTime = 0;
    private final ArrayList<String> timeArray = UnitOfTime.unitNames;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plus_dial);

        period = findViewById(R.id.DialTime_Period);
        periodPlus = findViewById(R.id.Period_Up);
        periodMinus = findViewById(R.id.Period_Down);
        unitOfTime = findViewById(R.id.DialTime_UnitOfTime);
        unitOfTimePlus = findViewById(R.id.UnitOfTime_Up);
        unitOfTimeMinus = findViewById(R.id.UnitOfTime_Down);

        vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);

        dialSettingViewModel = new DialSettingViewModel(
                this,
                DialManager.getInstance().getCategoryByName(getIntent().getExtras().getString("categoryName")));

        {
            // 뒤로가기 버튼 설정
            backButton = findViewById(R.id.BackButton);
            backButton.setOnClickListener(view -> finish());
        }

        {
            // 아이콘 표시 설정
            dialName = findViewById(R.id.Input_DialName);
            iconImage = findViewById(R.id.DialImage_Recommend);
            dialName.setOnEditorActionListener(this);
        }

        {
            startDayInput = findViewById(R.id.Input_Startday);
            Activity activity = this;
            startDayInput.setOnClickListener(view -> {
                DateTimePickerDialog dateTimePickerDialog = new DateTimePickerDialog(activity, startDayInput);
                dateTimePickerDialog.show();
            });
        }

        //region 완료 버튼 설정
        {
            Button completeButton = findViewById(R.id.DialPlus_Button);
            completeButton.setOnClickListener(view -> dialSettingViewModel.complete());
        }
        //endregion

        periodPlus.setOnLongClickListener(v -> {
            handler_up.post(runnable_up);
            return false;
        });

        //터치 이벤트 리스너 등록(누를때)
        periodPlus.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) { //눌렀을 때 동작
                countForPeriod++;
                setPeriod();
            }
            if (event.getAction() == MotionEvent.ACTION_UP) { //뗐을 때 동작
                handler_up.removeCallbacks(runnable_up);
            }
            return false;
        });

        periodMinus.setOnLongClickListener(v -> {    // 길게 누르면 동작
            handler_down.post(runnable_down);
            return false;
        });

        //터치 이벤트 리스너 등록(누를때)
        periodMinus.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) { //눌렀을 때 동작
                countForPeriod--;
                setPeriod();
            }
            if (event.getAction() == MotionEvent.ACTION_UP) { //뗐을 때 동작
                handler_down.removeCallbacks(runnable_down);
            }
            return false;
        });

        unitOfTimePlus.setOnClickListener(view -> {
            if (countForUnitOfTime < 4) {
                countForUnitOfTime++;
                unitOfTime.setText(timeArray.get(countForUnitOfTime));
            }
        });

        unitOfTimeMinus.setOnClickListener(view -> {
            if (countForUnitOfTime > 0) {
                countForUnitOfTime--;
                unitOfTime.setText(timeArray.get(countForUnitOfTime));
            }
        });
    }

    private final Handler handler_up = new Handler();
    private final Runnable runnable_up = new Runnable() {
        @Override
        public void run() {
            // Print out your letter here...
            countForPeriod++;
            setPeriod();
            // Call the runnable again
            handler_up.postDelayed(this, 100);
            vibrator.vibrate(VibrationEffect.createOneShot(VIBRATE_STRENGTH, VibrationEffect.DEFAULT_AMPLITUDE));
        }
    };

    private final Handler handler_down = new Handler();
    private final Runnable runnable_down = new Runnable() {
        @Override
        public void run() {
            // Print out your letter here...
            countForPeriod--;
            setPeriod();
            // Call the runnable again
            handler_down.postDelayed(this, 100);
            vibrator.vibrate(VibrationEffect.createOneShot(VIBRATE_STRENGTH, VibrationEffect.DEFAULT_AMPLITUDE));
        }
    };

    public void setPeriod() {
        if (countForPeriod <= 100 && countForPeriod > 0) {
            period.setText(String.valueOf(countForPeriod));
        } else {
            if (countForPeriod > 100) {
                countForPeriod = 100;
            }
            if (countForPeriod < 1) {
                countForPeriod = 1;
            }
        }
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
