package com.example.labtwoausecondversion.ui.parameters;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.RadioButton;

public class RadioButtonGroupGridLayout extends GridLayout implements View.OnClickListener {
    private RadioButton mRadioButton;

    public RadioButtonGroupGridLayout(Context context) {
        super(context);
    }

    public RadioButtonGroupGridLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    public void onClick(View v) {
        RadioButton radioButton = (RadioButton) v;
        if (mRadioButton != null) {
            mRadioButton.setChecked(false);
        }
        radioButton.setChecked(true);
        mRadioButton = radioButton;
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        super.addView(child, params);
        if (child instanceof RadioButton) {
            RadioButton radioButton = (RadioButton) child;
            if (mRadioButton == null) {
                radioButton.setChecked(true);
                mRadioButton = radioButton;
            }
            setChildrenOnClickListener(radioButton);
        }
    }

    private void setChildrenOnClickListener(RadioButton radioButton) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            radioButton.setOnClickListener(this);
        }
    }

    public int getCheckedRadioButtonId() {
        if (mRadioButton != null) {
            return mRadioButton.getId();
        }
        return -1;
    }

    public void reset() {
        if (mRadioButton != null) {
            mRadioButton.setChecked(false);
            if (getChildAt(0) instanceof RadioButton) {
                RadioButton radioButton = (RadioButton) getChildAt(0);
                radioButton.setChecked(true);
                mRadioButton = radioButton;
            }
        }
    }
}
