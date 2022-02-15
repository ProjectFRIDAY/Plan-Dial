package com.example.plandial;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

public class FormatTextView extends androidx.appcompat.widget.AppCompatTextView {
    protected final String formatString;

    public FormatTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedAttrs = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.FormatString,
                0,
                0
        );

        this.formatString = typedAttrs.getString(R.styleable.FormatString_formatString);

        // recycle TypedArray
        typedAttrs.recycle();
    }
}
