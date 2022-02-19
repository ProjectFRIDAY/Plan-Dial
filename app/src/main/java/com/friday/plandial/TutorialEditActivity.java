package com.friday.plandial;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;

import androidx.annotation.Nullable;

import com.example.plandial.R;

public class TutorialEditActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_edit);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        return super.dispatchTouchEvent(ev);
    }
}
