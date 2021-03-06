package com.friday.plandial;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.friday.plandial.db.WorkDatabase;

import java.util.ArrayList;

public class EditDialActivity extends AppCompatActivity implements TextView.OnEditorActionListener {
    private static final int VIBRATE_STRENGTH = 5;

    private final DialManager dialManager = DialManager.getInstance();
    private final IconRecommendation iconRecommendation = new IconRecommendation();
    private Vibrator vibrator;

    private DialEditingViewModel dialEditingViewModel;
    private Intent intent;
    private Category category;
    private AlertDial dial;

    private DateTimeTextView startDayInput;
    private EditText Input_DialName;
    private ImageButton backButton;
    private ImageButton removeButton;
    private TextView period, unitOfTime;
    private ImageButton periodPlus, periodMinus;
    private ImageButton unitOfTimePlus, unitOfTimeMinus;
    private CheckBox iconCheckbox;

    int countForPeriod = 0;
    private int countForUnitOfTime = 0;
    private final ArrayList<String> timeArray = UnitOfTime.unitNames;
    private ImageView iconImage;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch unableSwitch;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dial);

        {
            vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);

            //  ??? ????????????
            intent = getIntent();
            category = dialManager.getCategoryByName(intent.getStringExtra("categoryName"));
            dial = (AlertDial) category.getDialByName(intent.getStringExtra("dialName"));

            // ??? ????????????
            Input_DialName = findViewById(R.id.Input_DialName);
            iconImage = findViewById(R.id.DialImage_Recommend);
            unableSwitch = findViewById(R.id.switchButton);
            period = findViewById(R.id.DialTime_Period);
            periodPlus = findViewById(R.id.Period_Up);
            periodMinus = findViewById(R.id.Period_Down);
            unitOfTime = findViewById(R.id.DialTime_UnitOfTime);
            unitOfTimePlus = findViewById(R.id.UnitOfTime_Up);
            unitOfTimeMinus = findViewById(R.id.UnitOfTime_Down);
            iconCheckbox = findViewById(R.id.Icon_Checkbox);

            Input_DialName.setText(dial.getName());
            iconImage.setImageResource(dial.getIcon());

            countForPeriod = dial.getPeriod().getTimes();
            countForUnitOfTime = (int) UnitOfTime.unitToIndex.get(dial.getPeriod().getUnit());
            period.setText(String.valueOf(countForPeriod));
            unitOfTime.setText(timeArray.get(countForUnitOfTime));

            unableSwitch.setChecked(dial.isDisabled());
        }

        {
            // ???????????? ?????? ??????
            backButton = findViewById(R.id.BackButton);
            backButton.setOnClickListener(view -> finish());
        }

        {
            // ?????? ?????? ??????
            removeButton = findViewById(R.id.RemoveButton);
            removeButton.setOnClickListener(view -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("????????? " + dial.getName() + " ???????????? ?????????????????????????")
                        .setPositiveButton("???", (dialogInterface, i) -> {
                            dial.disable(this);
                            category.removeDialByObject(dial);
                            WorkDatabase.getInstance().delDial(dial);
                            PlanDialWidget.Update(this); // widget update
                            this.finish();
                        })
                        .setNegativeButton("?????????", (dialogInterface, i) -> {
                        });
                builder.show();
            });
        }

        // ????????? ?????? ??????
        Input_DialName.setOnEditorActionListener(this);

        // ??????
        dialEditingViewModel = new DialEditingViewModel(this, dial, category);

        //region ?????? ?????? ??????
        {
            ImageButton completeButton = findViewById(R.id.SaveButton);
            completeButton.setOnClickListener(view -> dialEditingViewModel.complete());
        }
        //endregion

        {
            startDayInput = findViewById(R.id.Input_Startday);
            startDayInput.setByOffsetDateTime(dial.getStartDateTime());
            startDayInput.setOnClickListener(view -> {
                DateTimePickerDialog dateTimePickerDialog = new DateTimePickerDialog(this, startDayInput, dial.getStartDateTime());
                dateTimePickerDialog.show();
            });
        }


        ScrollView scrollView = findViewById(R.id.ScrollView);
        scrollView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) { //?????? ??? ??????
                handler_up.removeCallbacks(runnable_up);
                handler_down.removeCallbacks(runnable_down);
            }
            return false;
        });

        periodPlus.setOnLongClickListener(v -> {
            handler_up.post(runnable_up);
            return false;
        });

        //?????? ????????? ????????? ??????(?????????)
        periodPlus.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) { //????????? ??? ??????
                countForPeriod++;
                setPeriod();
            }
            if (event.getAction() == MotionEvent.ACTION_UP) { //?????? ??? ??????
                handler_up.removeCallbacks(runnable_up);
            }
            return false;
        });

        periodMinus.setOnLongClickListener(v -> {    // ?????? ????????? ??????
            handler_down.post(runnable_down);
            return false;
        });

        //?????? ????????? ????????? ??????(?????????)
        periodMinus.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) { //????????? ??? ??????
                countForPeriod--;
                setPeriod();
            }
            if (event.getAction() == MotionEvent.ACTION_UP) { //?????? ??? ??????
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

        iconCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                changeIcon(Input_DialName.getText().toString());
            } else {
                iconImage.setImageResource(dial.getIcon());
            }
        });
    }

    private final Handler handler_up = new Handler();
    private final Runnable runnable_up = new Runnable() {
        @Override
        public void run() {
            countForPeriod++;
            setPeriod();
            handler_up.postDelayed(this, 100);
            vibrator.vibrate(VibrationEffect.createOneShot(VIBRATE_STRENGTH, VibrationEffect.DEFAULT_AMPLITUDE));
        }
    };

    private final Handler handler_down = new Handler();
    private final Runnable runnable_down = new Runnable() {
        @Override
        public void run() {
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

    public void changeIcon(String text) {
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

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (v.getId() == R.id.Input_DialName && actionId == EditorInfo.IME_ACTION_DONE && iconCheckbox.isChecked()) {
            changeIcon(v.getText().toString());
        }
        return false;
    }
}