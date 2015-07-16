package com.example.selftest.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.selftest.R;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class LoadingAnimation {
	private static final String TAG = "_LoadingManager";

	private static Activity mContext;
	private static View viewRoot;
	private static View circle1;
	private static View circle2;

	private boolean isLoading = false;

	private static LoadingAnimation instance;

	public static LoadingAnimation getInstance(Activity context) {
		mContext = context;
		viewRoot = LayoutInflater.from(mContext)
				.inflate(R.layout.loading, null);
		circle1 = viewRoot.findViewById(R.id.circle_1);
		circle2 = viewRoot.findViewById(R.id.circle_2);
		viewRoot.setTag("123");

		if (instance == null) {
			instance = new LoadingAnimation();
		}
		return instance;
	}

	public void beginLoading() {
		isLoading = true;
		LayoutParams layoutParams = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT,
				RelativeLayout.TRUE);
		RelativeLayout rl = (RelativeLayout) mContext
				.findViewById(R.id.rl_root);
		rl.addView(viewRoot, layoutParams);

		AnimationSet animation = (AnimationSet) AnimationUtils.loadAnimation(
				mContext, R.anim.animation_loading);
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				Log.d(TAG, "circle1 onAnimationStart " + getCurrentTimeString());
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				Log.d(TAG, "circle1 onAnimationRepeat "
						+ getCurrentTimeString());
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				Log.d(TAG, "circle1 onAnimationEnd " + getCurrentTimeString());
				if (isLoading) {
					Animation anim = AnimationUtils.loadAnimation(mContext,
							R.anim.animation_loading);
					anim.setAnimationListener(this);
					circle1.startAnimation(anim);
				}
			}
		});
		circle1.startAnimation(animation);
		circle1.setVisibility(View.VISIBLE);

		circle2.postDelayed(new Runnable() {

			@Override
			public void run() {
				if (!isLoading) {
					return;
				}
				AnimationSet animation2 = (AnimationSet) AnimationUtils
						.loadAnimation(mContext, R.anim.animation_loading);
				animation2.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub
						Log.d(TAG, "circle2 onAnimationStart "
								+ getCurrentTimeString());

					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						Log.d(TAG, "circle2 onAnimationRepeat "
								+ getCurrentTimeString());
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						Log.d(TAG, "circle2 onAnimationEnd "
								+ getCurrentTimeString());

						if (isLoading) {
							Animation anim = AnimationUtils.loadAnimation(
									mContext, R.anim.animation_loading);
							anim.setAnimationListener(this);
							circle2.startAnimation(anim);
						}
					}
				});
				circle2.startAnimation(animation2);
				circle2.setVisibility(View.VISIBLE);
			}
		}, 1500);
	}

	public void endLoading() {
		isLoading = false;
		RelativeLayout rl = (RelativeLayout) mContext
				.findViewById(R.id.rl_root);
		View v = rl.findViewWithTag("123");
		circle1 = v.findViewById(R.id.circle_1);
		circle2 = v.findViewById(R.id.circle_2);

		if (circle1 != null) {
			circle1.clearAnimation();
		}
		if (circle2 != null) {
			circle2.clearAnimation();
		}

		rl.removeView(v);
	}

	private String getCurrentTimeString() {
		SimpleDateFormat sdf = new SimpleDateFormat("hh:MM:ss");
		String now = sdf.format(new Date());
		return now;
	}

	public void beginLoading1() {
		isLoading = true;
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT,
				RelativeLayout.TRUE);
		RelativeLayout rl = (RelativeLayout) mContext
				.findViewById(R.id.rl_root);

		View vr = LayoutInflater.from(mContext).inflate(R.layout.loading_pea,
				null);
		vr.setTag("111");
		rl.addView(vr, layoutParams);

		doAnimation(vr);
	}

	private void doAnimation(final View vr) {
		final View rectangle1 = vr.findViewById(R.id.rectangle_1);
		final View rectangle2 = vr.findViewById(R.id.rectangle_2);
		final View rectangle3 = vr.findViewById(R.id.rectangle_3);
		final View rectangle4 = vr.findViewById(R.id.rectangle_4);
		final View rectangle5 = vr.findViewById(R.id.rectangle_5);

		Animation animation = AnimationUtils.loadAnimation(mContext,
				R.anim.animation_loading_pea);
		rectangle1.startAnimation(animation);

		rectangle2.postDelayed(new Runnable() {

			@Override
			public void run() {
				if (!isLoading) {
					return;
				}
				// TODO Auto-generated method stub
				Animation anim = AnimationUtils.loadAnimation(mContext,
						R.anim.animation_loading_pea);
				rectangle2.startAnimation(anim);
			}
		}, 100);
		rectangle3.postDelayed(new Runnable() {

			@Override
			public void run() {
				if (!isLoading) {
					return;
				}
				// TODO Auto-generated method stub
				Animation anim = AnimationUtils.loadAnimation(mContext,
						R.anim.animation_loading_pea);
				rectangle3.startAnimation(anim);
			}
		}, 200);
		rectangle4.postDelayed(new Runnable() {

			@Override
			public void run() {
				if (!isLoading) {
					return;
				}
				// TODO Auto-generated method stub
				Animation anim = AnimationUtils.loadAnimation(mContext,
						R.anim.animation_loading_pea);
				rectangle4.startAnimation(anim);
			}
		}, 300);
		rectangle5.postDelayed(new Runnable() {

			@Override
			public void run() {
				if (!isLoading) {
					return;
				}
				// TODO Auto-generated method stub
				Animation anim = AnimationUtils.loadAnimation(mContext,
						R.anim.animation_loading_pea);
				anim.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						rectangle5.postDelayed(new Runnable() {

							@Override
							public void run() {
								if (isLoading) {
									doAnimation(vr);
								}
							}
						}, 100);
					}
				});
				rectangle5.startAnimation(anim);
			}
		}, 400);
	}

	public void endLoading1() {
		isLoading = false;
		RelativeLayout rl = (RelativeLayout) mContext
				.findViewById(R.id.rl_root);
		View vr = rl.findViewWithTag("111");

		final View rectangle1 = vr.findViewById(R.id.rectangle_1);
		final View rectangle2 = vr.findViewById(R.id.rectangle_2);
		final View rectangle3 = vr.findViewById(R.id.rectangle_3);
		final View rectangle4 = vr.findViewById(R.id.rectangle_4);
		final View rectangle5 = vr.findViewById(R.id.rectangle_5);

		rectangle1.clearAnimation();
		rectangle2.clearAnimation();
		rectangle3.clearAnimation();
		rectangle4.clearAnimation();
		rectangle5.clearAnimation();

		rl.removeView(vr);
	}

	@SuppressLint("NewApi")
	public void beginLoading2() {
		isLoading = true;
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT,
				RelativeLayout.TRUE);
		RelativeLayout rl = (RelativeLayout) mContext
				.findViewById(R.id.rl_root);

		View vr = LayoutInflater.from(mContext).inflate(
				R.layout.loading_rectangle, null);
		vr.setTag("111");
		rl.addView(vr, layoutParams);

		View rec = vr.findViewById(R.id.rectangle);

		AnimatorSet animatorSet = new AnimatorSet();

		ObjectAnimator animator1 = ObjectAnimator.ofFloat(rec, "rotationX",
				0.0f, 180.0f).setDuration(5000);
		ObjectAnimator animator2 = ObjectAnimator.ofFloat(rec, "rotationY",
				0.0f, 180.0f).setDuration(5000);

		// animatorSet.playSequentially(animator1, animator2);
		animatorSet.play(animator1).before(animator2);
		animatorSet.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				animation.start();
			}
		});
		animatorSet.start();
	}

	public void endLoading2() {
		isLoading = false;
		RelativeLayout rl = (RelativeLayout) mContext
				.findViewById(R.id.rl_root);
		View vr = rl.findViewWithTag("111");

		if (vr != null) {
			vr.clearAnimation();
			rl.removeView(vr);
		}
	}

	@SuppressLint("NewApi")
	public void beginLoading3() {
		isLoading = true;
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT,
				RelativeLayout.TRUE);
		RelativeLayout rl = (RelativeLayout) mContext
				.findViewById(R.id.rl_root);

		View vr = LayoutInflater.from(mContext).inflate(
				R.layout.loading_rectangle_two, null);
		vr.setTag("111");
		rl.addView(vr, layoutParams);

		View rec1 = vr.findViewById(R.id.rectangle_1);
		View rec2 = vr.findViewById(R.id.rectangle_2);

		AnimatorSet animatorSet = new AnimatorSet();

		PropertyValuesHolder scaleX_da = PropertyValuesHolder.ofFloat(
				"scaleX", 1.0f, 1.8f);
		PropertyValuesHolder scaleY_da = PropertyValuesHolder.ofFloat(
				"scaleY", 1.0f, 1.8f);
		PropertyValuesHolder scaleX_xiao = PropertyValuesHolder.ofFloat(
				"scaleX", 1.8f, 1f);
		PropertyValuesHolder scaleY_xiao = PropertyValuesHolder.ofFloat(
				"scaleY", 1.8f, 1f);

		PropertyValuesHolder translate1Y = PropertyValuesHolder.ofFloat(
				"translationY", 0.0f, 110.0f);
		PropertyValuesHolder translate1_X = PropertyValuesHolder.ofFloat(
				"translationX", 0.0f, -110.0f);
		PropertyValuesHolder translate1_Y = PropertyValuesHolder.ofFloat(
				"translationY", 110.0f, 0.0f);
		PropertyValuesHolder translate1X = PropertyValuesHolder.ofFloat(
				"translationX", -110.0f, 0.0f);

		PropertyValuesHolder translate2Y = PropertyValuesHolder.ofFloat(
				"translationY", -110.0f, 0.0f);
		PropertyValuesHolder translate2_X = PropertyValuesHolder.ofFloat(
				"translationX", 110.0f, 0.0f);
		PropertyValuesHolder translate2_Y = PropertyValuesHolder.ofFloat(
				"translationY", 0.0f, -110.0f);
		PropertyValuesHolder translate2X = PropertyValuesHolder.ofFloat(
				"translationX", 0.0f, 110.0f);

		PropertyValuesHolder ratation = PropertyValuesHolder.ofFloat(
				"rotation", 0.0f, -90.0f);
		
		ObjectAnimator animator1_1 = ObjectAnimator.ofPropertyValuesHolder(
				rec1, translate1Y, scaleX_da, scaleY_da, ratation).setDuration(800);
		ObjectAnimator animator1_2 = ObjectAnimator.ofPropertyValuesHolder(
				rec1, translate1_X, scaleX_xiao, scaleY_xiao, ratation).setDuration(800);
		ObjectAnimator animator1_3 = ObjectAnimator.ofPropertyValuesHolder(
				rec1, translate1_Y, scaleX_da, scaleY_da, ratation).setDuration(800);
		ObjectAnimator animator1_4 = ObjectAnimator.ofPropertyValuesHolder(
				rec1, translate1X, scaleX_xiao, scaleY_xiao, ratation).setDuration(800);
		ObjectAnimator animator2_1 = ObjectAnimator.ofPropertyValuesHolder(
				rec2, translate2_Y, scaleX_da, scaleY_da, ratation).setDuration(800);
		ObjectAnimator animator2_2 = ObjectAnimator.ofPropertyValuesHolder(
				rec2, translate2X, scaleX_xiao, scaleY_xiao, ratation).setDuration(800);
		ObjectAnimator animator2_3 = ObjectAnimator.ofPropertyValuesHolder(
				rec2, translate2Y, scaleX_da, scaleY_da, ratation).setDuration(800);
		ObjectAnimator animator2_4 = ObjectAnimator.ofPropertyValuesHolder(
				rec2, translate2_X, scaleX_xiao, scaleY_xiao, ratation).setDuration(800);
		
		animatorSet.play(animator1_2).after(animator1_1).after(200);
		animatorSet.play(animator1_3).after(animator1_2).after(200);
		animatorSet.play(animator1_4).after(animator1_3).after(200);
		animatorSet.play(animator2_2).after(animator2_1).after(200);
		animatorSet.play(animator2_3).after(animator2_2).after(200);
		animatorSet.play(animator2_4).after(animator2_3).after(200);
		
		animatorSet.setDuration(4000).setInterpolator(new AccelerateDecelerateInterpolator());
		animatorSet.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				animation.setStartDelay(200);
				animation.start();
			}
		});
		animatorSet.start();
	}

	public void endLoading3() {
		isLoading = false;
		RelativeLayout rl = (RelativeLayout) mContext
				.findViewById(R.id.rl_root);
		View vr = rl.findViewWithTag("111");

		if (vr != null) {
			vr.clearAnimation();
			rl.removeView(vr);
		}
	}
}
