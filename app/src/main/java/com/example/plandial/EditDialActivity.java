package com.example.plandial;

import android.content.Intent;
import android.os.Bundle;
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

public class EditDialActivity extends AppCompatActivity implements TextView.OnEditorActionListener {
    private final DialManager dialManager = DialManager.getInstance();
    private final IconRecommendation iconRecommendation = new IconRecommendation();
    private DialEditingViewModel dialEditingViewModel;
    private Intent intent;
    private Dial dial;

    private EditText Input_DialName;
    private ImageButton backButton;
    private ImageView iconImage;
    private LinearSelector unitSelector;
    private Switch unableSwitch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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

        {
            // 뒤로가기 버튼 설정
            backButton = findViewById(R.id.BackButton);
            backButton.setOnClickListener(view -> finish());
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