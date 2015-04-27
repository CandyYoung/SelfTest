package com.example.selftest.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;

/**
 * Created by 海强 on 2015/4/24.
 */
public class LinearItemsControl extends LinearLayout {

    public interface DrawItemCallback {
        View getItemView(int position);
        int getCount();
    }

    private DrawItemCallback mCallback;

    public LinearItemsControl(Context context) {
        this(context, null);
        setOrientation(VERTICAL);
    }

    public LinearItemsControl(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
    }

    public void setCallback(DrawItemCallback callback) {
        mCallback = callback;
    }

    public void drawItems() {
        if (mCallback != null) {
            for (int i = 0; i < mCallback.getCount(); i++) {
                View view = mCallback.getItemView(i);
                if (view != null) {
                    this.addView(view);
                }
            }
        }
    }
}
