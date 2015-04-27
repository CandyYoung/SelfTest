package com.example.selftest.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;


/**
 * 用于嵌套在Scroll类型的布局容器中的GridView
 * 
 * @author 海强
 *
 */
public class MeasuredGridView extends GridView {

	public MeasuredGridView(Context context) {
		super(context, null);
	}

	public MeasuredGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MeasuredGridView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@SuppressLint("NewApi")
	public MeasuredGridView(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
