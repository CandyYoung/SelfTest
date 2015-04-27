package com.example.selftest.activities;

import java.util.Locale;

import com.example.selftest.R;
import com.example.selftest.entity.HttpAction;
import com.example.selftest.entity.LiveListResponse;
import com.example.selftest.entity.MyWebResponse;
import com.example.selftest.entity.RoomInfo;
import com.example.selftest.entity.RoomInfoResponse;
import com.example.selftest.fragments.SurfaceViewFragment;
import com.example.selftest.utils.HttpUtil;
import com.example.selftest.utils.JsonUtil;
import com.example.selftest.utils.HttpUtil.WebRequestListener;
import com.google.gson.reflect.TypeToken;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

public class RoomActivity extends FragmentActivity implements
		WebRequestListener {
	private static final int RES_COMPLETE = 0;

	private static final String TAG = "RoomActivity";
	public static final String ROOM_ID = "video_id";

	private FrameLayout frameLayout;

	private String mRoomId;
	private static String m3u8Url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		// getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_room);
		frameLayout = (FrameLayout) findViewById(R.id.fl_video);

		Intent tIntent = getIntent();
		if (tIntent != null) {
			mRoomId = tIntent.getStringExtra(ROOM_ID);
		}

		getRoomInfo();
	}

	private boolean isBusy;

	private void getRoomInfo() {
		if (isBusy) {
			return;
		}
		isBusy = true;
		HttpUtil.get(
				String.format(Locale.getDefault(),
						"http://www.zhanqi.tv/api/static/live.roomid/%s.json",
						mRoomId), HttpAction.ROOM_INFO, this);
	}

	@Override
	public void onComplete(MyWebResponse result) {
		isBusy = false;
		if (result == null || result.getResponseString() == "") {
			return;
		}
		try {
			final RoomInfoResponse resp = JsonUtil.fromJson(
					result.getResponseString(),
					new TypeToken<RoomInfoResponse>() {
					}.getType());
			if (resp != null) {
				this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if (resp.getCode() == "0") {
							Toast.makeText(RoomActivity.this,
									resp.getMessage(), Toast.LENGTH_SHORT)
									.show();
						}

						if (resp.getData().getStatus().equals("4")) {
							m3u8Url = String
									.format("http://dlhls.cdn.zhanqi.tv/zqlive/%s_1024/index.m3u8",
											resp.getData().getVideoId());
							Log.d(TAG, "m3u8Url=" + m3u8Url);

							getSupportFragmentManager()
									.beginTransaction()
									.add(R.id.fl_video,
											new SurfaceViewFragment(m3u8Url))
									.commit();
						} else {
							Toast.makeText(
									RoomActivity.this,
									"主播尚未開播，VideoId="
											+ resp.getData().getVideoId()
											+ ", Status="
											+ resp.getData().getStatus(),
									Toast.LENGTH_SHORT).show();
						}
					}
				});

			}
		} catch (Exception e) {
			Log.e("onComplete", "Json parse exception");
			e.printStackTrace();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Display display = getWindowManager().getDefaultDisplay();
			int rotation = display.getRotation();
			if (rotation == Surface.ROTATION_90
					|| rotation == Surface.ROTATION_270) {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

				getWindow().setFlags(
						WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
						WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
				DisplayMetrics metrics = new DisplayMetrics();
				display.getMetrics(metrics);
				double scale = metrics.density;
				frameLayout.setLayoutParams(new RelativeLayout.LayoutParams(
						LayoutParams.FILL_PARENT, (int) (scale * 200)));
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		Display display = getWindowManager().getDefaultDisplay();
		Log.d(TAG, "display width=" + display.getWidth() + ", display height="
				+ display.getHeight());
		if (display.getWidth() > display.getHeight()) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);

			frameLayout.setLayoutParams(new RelativeLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		} else {
			getWindow().setFlags(
					WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

			DisplayMetrics metrics = new DisplayMetrics();
			display.getMetrics(metrics);
			double scale = metrics.density;
			frameLayout.setLayoutParams(new RelativeLayout.LayoutParams(
					LayoutParams.FILL_PARENT, (int) (scale * 200)));
		}
	}
}
