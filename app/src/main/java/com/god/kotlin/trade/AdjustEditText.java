package com.god.kotlin.trade;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.core.content.ContextCompat;
import com.god.kotlin.R;
import com.god.kotlin.util.MathUtils;

public class AdjustEditText extends RelativeLayout implements View.OnClickListener {

    ImageView plus;
    ImageView reduce;
    EditText edit;

    View line1;
    View line2;
    View line3;
    View line4;
    View line5;
    View line6;

    String hint;
    private int color = R.color.trade_pull_text;
    double unit = 100;
    int exp = 2; //MathUtils.formatNum(exp)

    public AdjustEditText(Context context) {
        super(context);
    }

    public AdjustEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs,
                R.styleable.AdjustEditText, 0, 0);
        hint = ta.getString(R.styleable.AdjustEditText_hint);
        unit = ta.getFloat(R.styleable.AdjustEditText_unit, 0.01f);
        color = ta.getResourceId(R.styleable.AdjustEditText_ad_color, 0);

        if(color == 0){
            color =  R.color.trade_little_gray;
        }

        if (unit >= 100) {
            exp = 2;
        } else {
            exp = 4;
        }

        ta.recycle();

        inflate(context, R.layout.trade_view_adjust, this);
        plus = findViewById(R.id.plus);
        reduce = findViewById(R.id.reduce);
        edit = findViewById(R.id.trade_entrust_num);
        plus.setOnClickListener(this);
        reduce.setOnClickListener(this);

        line1 = findViewById(R.id.line1);
        line2 = findViewById(R.id.line2);
        line3 = findViewById(R.id.line3);
        line4 = findViewById(R.id.line4);
        line5 = findViewById(R.id.line5);
        line6 = findViewById(R.id.line6);

        edit.setHint(hint);
        edit.setTextSize(TypedValue.COMPLEX_UNIT_DIP,15);
        setColor(color);
    }

    public void setColor(int color) {
        this.color = color;
        line1.setBackgroundColor(ContextCompat.getColor(getContext(), color));
        line2.setBackgroundColor(ContextCompat.getColor(getContext(), color));
        line3.setBackgroundColor(ContextCompat.getColor(getContext(), color));
        line4.setBackgroundColor(ContextCompat.getColor(getContext(), color));
        line5.setBackgroundColor(ContextCompat.getColor(getContext(), color));
        line6.setBackgroundColor(ContextCompat.getColor(getContext(), color));
        plus.setColorFilter(ContextCompat.getColor(getContext(), color));
        reduce.setColorFilter(ContextCompat.getColor(getContext(), color));

        if(color == R.color.trade_red){
            plus.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.trade_trans_red));
            reduce.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.trade_trans_red));
        }else if(color == R.color.trade_blue){
            plus.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.trade_trans_blue));
            reduce.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.trade_trans_blue));
        }
    }


    public void setText(String text) {
        if (Double.parseDouble(text) < unit) {
           text = "0";
        } else {
            plus.setClickable(true);
            reduce.setClickable(true);
        }
        edit.setText(MathUtils.formatNum(Double.parseDouble(text), exp));
    }

    public String getText() {
        return edit.getText().toString();
    }

    @Override
    public void onClick(View v) {
        if (v == plus) {
            String e1 = edit.getText().toString();
            if (TextUtils.isEmpty(e1)) {
                e1 = "0";
            }
            double temp = Double.parseDouble(e1);
            temp = temp + unit;
            edit.setText(MathUtils.formatNum(temp, exp));
            edit.clearFocus();
        } else if (v == reduce) {
            String e1 = edit.getText().toString();
            if (TextUtils.isEmpty(e1)) {
                return;
            }
            double temp = Double.parseDouble(e1);
            temp = temp - unit;
            if (temp < 0) {
                temp = 0;
            }
            edit.setText(MathUtils.formatNum(temp, exp));
            edit.clearFocus();
        }

//        else if(v == this){
//            edit.setClickable(true);
//            edit.setFocusable(true);
//            edit.requestFocus();
//            if(edit.getText() != null) {
//                edit.setSelection(edit.getText().length());
//            }
//        }
    }
}
