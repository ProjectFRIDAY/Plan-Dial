package com.example.plandial;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class EditDialActivity extends AppCompatActivity implements TextView.OnEditorActionListener {
    private static final int VIBRATE_STRENGTH = 5;

    private final DialManager dialManager = DialManager.getInstance();
    private final IconRecommendation iconRecommendation = new IconRecommendation();
    private Vibrator vibrator;

    private DialEditingViewModel dialEditingViewModel;
    private Intent intent;
    private Category category;
    private Dial dial;

    private EditText Input_DialName;
    private ImageButton backButton;
    private ImageButton removeButton;
    private TextView period, unitOfTime;
    private ImageButton periodPlus, periodMinus;
    private ImageButton unitOfTimePlus, unitOfTimeMinus;

    int countForPeriod = 0;
    private int countForUnitOfTime = 0;
    private final ArrayList<String> timeArray = UnitOfTime.unitNames;
    private ImageView iconImage;
    private Switch unableSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dial);

        {
            vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);

            //  값 불러오기
            intent = getIntent();
            category = dialManager.getCategoryByName(intent.getStringExtra("categoryName"));
            dial = category.getDialByName(intent.getStringExtra("dialName"));

            // 값 보여주기
            Input_DialName = findViewById(R.id.Input_DialName);
            iconImage = findViewById(R.id.DialImage_Recommend);
            unableSwitch = findViewById(R.id.switchButton);
            period = findViewById(R.id.DialTime_Period);
            periodPlus = findViewById(R.id.Period_Up);
            periodMinus = findViewById(R.id.Period_Down);
            unitOfTime = findViewById(R.id.DialTime_UnitOfTime);
            unitOfTimePlus = findViewById(R.id.UnitOfTime_Up);
            unitOfTimeMinus = findViewById(R.id.UnitOfTime_Down);

            Input_DialName.setText(dial.getName());
            iconImage.setImageResource(dial.getIcon());

            countForPeriod = dial.getPeriod().getTimes();
            countForUnitOfTime = (int) UnitOfTime.unitToIndex.get(dial.getPeriod().getUnit());
            period.setText(String.valueOf(countForPeriod));
            unitOfTime.setText(timeArray.get(countForUnitOfTime));

            unableSwitch.setChecked(dial.isDisabled());
        }

        {
            // 뒤로가기 버튼 설정
            backButton = findViewById(R.id.BackButton);
            backButton.setOnClickListener(view -> finish());
        }

        {
            // 삭제 버튼 설정
            removeButton = findViewById(R.id.RemoveButton);
            removeButton.setOnClickListener(view -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("정말로 " + dial.getName() + " 다이얼을 삭제하시겠습니까?")
                        .setPositiveButton("예", (dialogInterface, i) -> {
                            category.removeDialByObject(dial);
                            this.finish();
                        })
                        .setNegativeButton("아니오", (dialogInterface, i) -> {
                        });
                builder.show();
            });
        }

        // 아이콘 표시 설정
        Input_DialName.setOnEditorActionListener(this);

        // 저장
        dialEditingViewModel = new DialEditingViewModel(this, dial, category);

        //region 완료 버튼 설정
        {
            ImageButton completeButton = findViewById(R.id.SaveButton);
            completeButton.setOnClickListener(view -> dialEditingViewModel.complete());
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