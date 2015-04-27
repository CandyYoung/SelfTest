package com.example.selftest.fragments;

import java.io.IOException;

import com.example.selftest.R;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.SurfaceHolder.Callback;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class SurfaceViewFragment extends Fragment implements
		OnPreparedListener, OnCompletionListener, OnErrorListener,
		OnInfoListener, OnSeekCompleteListener, OnVideoSizeChangedListener,
		Callback {
	private static final String TAG = "SurfaceViewFragment";

	private Activity mAcitivity;
	private View viewRoot;

	private String m3u8Url;

	private SurfaceView mSurfaceView;
	private SurfaceHolder mHolder;
	private MediaPlayer mPlayer;

	public SurfaceViewFragment(String url) {
		super();
		m3u8Url = url;
	}

	@Override
	public void onAttach(Activity activity) {
		mAcitivity = activity;

		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return viewRoot = inflater.inflate(R.layout.fragment_surfaceview,
				container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mSurfaceView = (SurfaceView) viewRoot.findViewById(R.id.sv_video);
		// 给SurfaceView添加CallBack监听
		mHolder = mSurfaceView.getHolder();
		mHolder.addCallback(this);
		mHolder.setKeepScreenOn(true);
		// 为了可以播放视频或者使用Camera预览，我们需要指定其Buffer类型
		// mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		setMediaPlayer();
	}

	private void setMediaPlayer() {
		mPlayer = new MediaPlayer();
		mPlayer.setOnPreparedListener(this);
		mPlayer.setOnCompletionListener(this);
		mPlayer.setOnErrorListener(this);
		mPlayer.setOnInfoListener(this);
		mPlayer.setOnSeekCompleteListener(this);
		mPlayer.setOnVideoSizeChangedListener(this);

		try {
			mPlayer.setDataSource(m3u8Url);
		} catch (IllegalArgumentException e1) {
			Log.d(TAG, "setDataSource catch IllegalArgumentException");
			e1.printStackTrace();
		} catch (SecurityException e1) {
			Log.d(TAG, "setDataSource catch SecurityException");
			e1.printStackTrace();
		} catch (IllegalStateException e1) {
			Log.d(TAG, "setDataSource catch IllegalStateException");
			e1.printStackTrace();
		} catch (IOException e1) {
			Log.d(TAG, "setDataSource catch IOException");
			e1.printStackTrace();
		} catch (Exception e) {
			Log.d(TAG, "setDataSource catch Exception");
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		if (mPlayer != null) {
			mPlayer.release();
			mPlayer = null;
		}
	}

//	@Override
//	public void onConfigurationChanged(Configuration newConfig) {
//		super.onConfigurationChanged(newConfig);
//
//		Display display = mAcitivity.getWindowManager().getDefaultDisplay();
//		Log.d(TAG, "display width=" + display.getWidth() + ", display height="
//				+ display.getHeight());
//		if (display.getWidth() > display.getHeight()) {
//			mAcitivity.getWindow().setFlags(
//					WindowManager.LayoutParams.FLAG_FULLSCREEN,
//					WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//			mSurfaceView.setLayoutParams(new RelativeLayout.LayoutParams(
//					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
//		} else {
//			mAcitivity.getWindow().setFlags(
//					WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
//					WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
//
//			DisplayMetrics metrics = new DisplayMetrics();
//			mAcitivity.getWindowManager().getDefaultDisplay()
//					.getMetrics(metrics);
//			double scale = metrics.density;
//			mSurfaceView.setLayoutParams(new RelativeLayout.LayoutParams(
//					LayoutParams.FILL_PARENT, (int) (scale * 200)));
//		}
//	}

	@Override
	public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
		Log.d(TAG, "onVideoSizeChanged");

		if (width > 0 && height > 0) {
			Log.d(TAG, "play again in onVideoSizeChanged");
			playback(width, height);// 获得视频高宽后播放。
		}
	}

	@Override
	public void onSeekComplete(MediaPlayer mp) {
		Log.d(TAG, "onSeekComplete");

	}

	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		Log.d(TAG, "onInfo");
		// 当一些特定信息出现或者警告时触发
		switch (what) {
		case MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
			break;
		case MediaPlayer.MEDIA_INFO_METADATA_UPDATE:
			break;
		case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
			break;
		case MediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
			break;
		}
		return false;
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		Log.d(TAG, "onError");
		switch (what) {
		case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
			Log.w("Play Error:::", "MEDIA_ERROR_SERVER_DIED");
			break;
		case MediaPlayer.MEDIA_ERROR_UNKNOWN:
			Log.w("Play Error:::", "MEDIA_ERROR_UNKNOWN");
			break;
		default:
			break;
		}
		return false;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		Log.d(TAG, "onCompletion");

		if (mPlayer != null) {
			mPlayer.release();
			// mPlayer.reset();
			//
			// setMediaPlayer();
		}
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		Log.d(TAG, "onPrepared");

		// 当prepare完成后，该方法触发，在这里我们播放视频

		// 首先取得video的宽和高
		int vWidth = mPlayer.getVideoWidth();
		int vHeight = mPlayer.getVideoHeight();

		if (vWidth != 0 && vHeight != 0) {
			playback(vWidth, vHeight);
		} else {
			// 无法获得信息的情况下仍然start MediaPlayer。
			// 这样处理后会再次触发OnVideoSizeChangedListener接口的onVideoSizeChanged方法，并且在这个回调时能正确的返回视频信息
			mPlayer.start();
		}
	}

	private void playback(int vWidth, int vHeight) {
		if (vWidth > mSurfaceView.getWidth()
				|| vHeight > mSurfaceView.getHeight()) {
			// 如果video的宽或者高超出了当前屏幕的大小，则要进行缩放
			float wRatio = (float) vWidth / (float) mSurfaceView.getWidth();
			float hRatio = (float) vHeight / (float) mSurfaceView.getHeight();

			// 选择大的一个进行缩放
			float ratio = Math.max(wRatio, hRatio);

			vWidth = (int) Math.ceil((float) vWidth / ratio);
			vHeight = (int) Math.ceil((float) vHeight / ratio);

			// 设置surfaceView的布局参数
			// mSurfaceView.setLayoutParams(new
			// LinearLayout.LayoutParams(vWidth,
			// vHeight));

			// 然后开始播放视频
			mPlayer.start();
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.d(TAG, "surfaceCreated");
		// 当SurfaceView中的Surface被创建的时候被调用
		// 在这里我们指定MediaPlayer在当前的Surface中进行播放
		mPlayer.setDisplay(holder);
		// 在指定了MediaPlayer播放的容器后，我们就可以使用prepare或者prepareAsync来准备播放了
		mPlayer.prepareAsync();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.d(TAG, "surfaceChanged");

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "surfaceDestroyed");

		holder.removeCallback(this);
	}

}
