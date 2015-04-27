package com.example.selftest.widgets;

import java.util.ArrayList;
import java.util.List;

import com.example.selftest.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;

public class BottomTabControl extends LinearLayout {

	private Context mContext;
	private CharSequence[] labels;
	private TypedArray iconArray;
	private List<CheckedTextView> ctvList = new ArrayList<CheckedTextView>();

	public interface OnTabSelected {
		void tabSelected(int selectedIndex);
	}

	private OnTabSelected tabSelected;

	public void setTabSelected(OnTabSelected tabSelected) {
		this.tabSelected = tabSelected;
	}

	public BottomTabControl(Context context, AttributeSet attrs) {
		super(context, attrs);

		mContext = context;
		TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.MyTabBar, 0, 0);
		labels = ta.getTextArray(R.styleable.MyTabBar_bottom_labels);
		int resId = ta.getResourceId(R.styleable.MyTabBar_bottom_icon, 0);
		iconArray = context.getResources().obtainTypedArray(resId);

		ta.recycle();
		init();
	}

	private void init() {
		this.setOrientation(HORIZONTAL);
		this.setBackgroundColor(0x5f5f5f);

		LayoutInflater inflater = LayoutInflater.from(mContext);

		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.MATCH_PARENT);
		lp.weight = 1.0f;
		lp.gravity = Gravity.CENTER;

		for (int i = 0; i < labels.length; i++) {

			View view = inflater.inflate(R.layout.tab_item, null, true);
			view.setLayoutParams(lp);
			CheckedTextView ctv = (CheckedTextView) view
					.findViewById(R.id.ctv_label);
			ctv.setText(labels[i]);
			// ctv.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.home_icon_selector,0,0);
			ctv.setCompoundDrawablesWithIntrinsicBounds(null, mContext
					.getResources().getDrawable(iconArray.getResourceId(i, 0)),
					null, null);
			// ctv.setCompoundDrawables(
			// null,
			// mContext.getResources().getDrawable(
			// iconArray.getResourceId(i, 0)), null, null);

			final int index = i;
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					for (int j = 0; j < ctvList.size(); j++) {
						if (j == index) {
							ctvList.get(j).setChecked(true);
							ctvList.get(j).setTextColor(Color.GREEN);
						} else {
							ctvList.get(j).setChecked(false);
							ctvList.get(j)
									.setTextColor(
											mContext.getResources()
													.getColor(
															R.color.hint_foreground_material_light));
						}
					}

					if (tabSelected != null) {
						tabSelected.tabSelected(index);
					}
				}
			});

			ctvList.add(ctv);

			if (i == 0) {
				view.setBackgroundColor(0x000000);
				ctv.setChecked(true);
				ctv.setTextColor(Color.GREEN);
			} else {
				view.setBackgroundColor(0x000000);
				ctv.setChecked(false);
				ctv.setTextColor(mContext.getResources().getColor(
						R.color.hint_foreground_material_light));
			}

			this.addView(view);
		}
	}

}
