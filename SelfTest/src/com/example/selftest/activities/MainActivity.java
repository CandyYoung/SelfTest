package com.example.selftest.activities;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.example.selftest.MyBroadcastReceiver;
import com.example.selftest.R;
import com.example.selftest.R.id;
import com.example.selftest.R.layout;
import com.example.selftest.services.MessageService;
import com.example.selftest.utils.HttpUtil;
import com.example.selftest.utils.ImageLoader;
import com.example.selftest.utils.LoadingManager;
import com.example.selftest.utils.StreamConvertor;

import android.R.string;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.AsyncTaskLoader;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	private static final String TAG = "MainActivity";

	private static WebView mWebView;
	private RelativeLayout relativeLayout;
	private TextView mTVCacheSize;

	@Override
	protected void finalize() throws Throwable {
		System.out.println("MainActivity.finalize");
		super.finalize();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		relativeLayout = (RelativeLayout) findViewById(R.id.rl_root);
		findViewById(R.id.btn_get).setOnClickListener(this);
		findViewById(R.id.btn_go).setOnClickListener(this);
		findViewById(R.id.ctv_test).setOnClickListener(this);
		findViewById(R.id.rl_clean).setOnClickListener(this);
		findViewById(R.id.iv_test).setOnClickListener(this);
		mTVCacheSize = (TextView) findViewById(R.id.tv_cache_size);
		mWebView = (WebView) findViewById(R.id.webView_html);
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {

				view.loadUrl(url);
				return true;
			}
		});
		Rect outRect = new Rect();
		getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
		Log.d(TAG, "height="+outRect.height()+", width="+outRect.width());
	}

	@Override
	protected void onResume() {
		super.onResume();

		long size = new ImageLoader(null, this).getSize();
		Log.d("getSize", "size=" + size);
		double showSize = size / 8.0f / 1024f;
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		mTVCacheSize.setText(decimalFormat.format(showSize) + "kb");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
			mWebView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private MyHandler mHandler = new MyHandler();

	private static class MyHandler extends Handler {
		public void handleMessage(android.os.Message msg) {

			if (msg.what == 0) {
				String result = (String) msg.obj;
				// mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
				String html = "<html><body><p>Anroid操作系统在Boot后</p></body></html>";
				mWebView.loadDataWithBaseURL("http://www.baidu.com", result,
						"text/html", "utf-8", null);
				// mWebView.loadData(result, "text/html", "utf-8");
			}
		};
	}

	Timer timer;

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_get:
			/* Thread+Runnable实现异步 Handler中处理消息更新UI */
			// new Thread(new Runnable() {
			//
			// @Override
			// public void run() {
			// // String html = HttpUtil.get("http://www.baidu.conull);
			// // mHandler.obtainMessage(0, html).sendToTarget();
			// doSocket("113.31.87.86", 15001);
			// // doSocket("www.zhanqi.tv/api/static/live.roomid/2021.json",
			// // 80);
			// }
			// }).start();

			/* AsyncTask实现异步 */
			// HttpAsyncTask task = new HttpAsyncTask();
			// task.execute("http://www.baidu.com");

			/* Handler实现异步 */
			// System.out.println("new HandlerThread");
			// HandlerThread hThread = new HandlerThread("thread-1",
			// android.os.Process.THREAD_PRIORITY_DEFAULT);
			// hThread.start();
			// Looper looper = hThread.getLooper();
			// final Handler handler = new Handler(looper, new
			// Handler.Callback() {
			//
			// @Override
			// public boolean handleMessage(Message msg) {
			// System.out.println("HandlerThread handleMessage");
			// return true;
			// }
			// });
			// handler.post(new Runnable() {
			//
			// @Override
			// public void run() {
			// String html = HttpUtil.get("http://www.baidu.com");
			// System.out.println(html);
			// // mHandler.obtainMessage(0, html).sendToTarget();
			// }
			// });
			//
			// if (handler != null) {
			// handler.removeCallbacksAndMessages(null);
			// }

			/* Loader实现异步 */
			// Bundle b = new Bundle();
			// b.putString("name", "Jack");
			// b.putInt("age", 28);
			// b.putString("url", "http://www.baidu.com");
			// getLoaderManager().initLoader(1111, b, this);

			if (!isLoading) {
				isLoading = true;
				LoadingManager.getInstance(this).beginLoading3();

				// timer = new Timer();
				// timer.schedule(new TimerTask() {
				//
				// @Override
				// public void run() {
				// Log.d("MainActivity", "Timer fireed " +
				// getCurrentTimeString());
				// }
				// }, 3000, 1000);
			} else {
				isLoading = false;
				LoadingManager.getInstance(this).endLoading3();

				// timer.purge();
				// timer.cancel();
			}
			break;

		case R.id.btn_go:
			Intent intent = new Intent(this, HomeActivity.class);
			startActivity(intent);
			break;

		case R.id.iv_test:
			Intent intent1 = new Intent("com.example.selftest.MyBroadcastReceiver");
			intent1.putExtra("MSG", "Do you remember?");
//			intent1.setAction("com.example.selftest.MyBroadcastReceiver");
//			LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
//			manager.sendBroadcast(intent1);
			sendBroadcast(intent1);
			break;
			
		case R.id.ctv_test:
			// Animation animation = AnimationUtils.loadAnimation(this,
			// R.anim.animation_loading_pea);
			// View view = findViewById(R.id.view_test);
			// view.startAnimation(animation);

			// startService(new Intent(this, MessageService.class));

			TextView tv = new TextView(this);
			ImageView iv = new ImageView(this);
			Toast.makeText(
					this,
					"TextView:" + String.valueOf(tv.isClickable())
							+ ", ImageView:" + String.valueOf(iv.isClickable()),
					Toast.LENGTH_SHORT).show();
			break;

		case R.id.rl_clean:
			new ImageLoader(null, this).ClearCache();
			String tt = null;
			mTVCacheSize.setText(tt);

			// (new Intent(this, MessageService.class));
			break;

		default:
			break;
		}

	}

	private String getCurrentTimeString() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String now = sdf.format(new Date());
		return now;
	}

	boolean isLoading;

	/* AsyncTask实现异步 */
	static class HttpAsyncTask extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			return "";
			// return HttpUtil.get(params[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			mWebView.loadDataWithBaseURL("http://www.baidu.com", result,
					"text/html", "utf-8", "http://www.baidu.com");
		}
	}

	private void doSocket(String desName, int port) {
		Socket socket = null;
		try {
			socket = new Socket(desName, port);
			InputStream is = socket.getInputStream();
			String result = StreamConvertor.inputStreamToString(is);
			Log.d("SocketResponse", "result：" + result);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (socket != null && !socket.isClosed()) {
				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	class MyViewGroup extends ViewGroup {

		public MyViewGroup(Context context, AttributeSet attrs) {
			super(context, attrs);
			// TODO Auto-generated constructor stub

			getChildAt(1);
			addView(new TextView(context));
		}

		@Override
		protected void onLayout(boolean changed, int l, int t, int r, int b) {
			// TODO Auto-generated method stub

		}

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			getSuggestedMinimumWidth();
			MeasureSpec.getMode(widthMeasureSpec);
			int width = MeasureSpec.getSize(widthMeasureSpec);
			int height = MeasureSpec.getSize(heightMeasureSpec);
			setMeasuredDimension(width, height);
			getMeasuredWidth();
			getMeasuredHeight();
			requestLayout();

			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}

	}
}
