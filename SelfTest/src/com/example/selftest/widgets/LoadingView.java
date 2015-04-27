package com.example.selftest.widgets;

import com.example.selftest.R;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LoadingView extends RelativeLayout {
	
	private Context mContext;
	private ImageView mIVAnimation;
	private TextView mTVTips;
	

	public LoadingView(Context context) {
		this(context, null);
	}

	public LoadingView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		mContext = context;
		init();
	}

	void init() {
		inflate(mContext, R.layout.layout_loading_view, this);
		
		mIVAnimation = (ImageView) findViewById(R.id.iv_loading);
		mTVTips = (TextView) findViewById(R.id.tv_tips);
	}
	
	public void startLoading(String tips) {
		setVisibility(View.VISIBLE);
		
		mTVTips.setText(tips);
		
		mIVAnimation.setBackgroundResource(R.drawable.loading_drawable);
		AnimationDrawable anim = (AnimationDrawable) mIVAnimation.getBackground();
		anim.start();
	}
	
	public void loadSuccess() {
		this.post(new Runnable() {
			
			@Override
			public void run() {
				AnimationDrawable anim = (AnimationDrawable) mIVAnimation.getBackground();
				anim.stop();
				
				setVisibility(View.GONE);
			}
		});
		
	}
	
	public void loadFailed(final String tips) {
		
		this.post(new Runnable() {
			
			@Override
			public void run() {
//				AnimationDrawable anim = (AnimationDrawable) mIVAnimation.getBackground();
//				anim.stop();
				
				mTVTips.setText(tips);
				
				mIVAnimation.setBackgroundResource(R.drawable.loading_failed_drawable);
				AnimationDrawable anim1 = (AnimationDrawable) mIVAnimation.getBackground();
				anim1.start();
			}
		});
	}
}
