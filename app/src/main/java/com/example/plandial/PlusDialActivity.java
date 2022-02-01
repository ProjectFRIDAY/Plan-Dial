package com.example.plandial;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PlusDialActivity extends AppCompatActivity implements TextView.OnEditorActionListener {
    private ImageButton backButton;
    private ImageView iconImage;
    private EditText dialName;

    private final IconRecommendation iconRecommendation = new IconRecommendation();
    private DialSettingViewModel dialSettingViewModel;

    private DateTimeTextView startDayInput;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plus_dial);

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
            Activity activity = this;
            Button completeButton = findViewById(R.id.DialPlus_Button);
            completeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialSettingViewModel.complete();
                }
            });
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
                dialSettingViewModel.setImage(imageId);
            }
        }
        return false;
    }
}
