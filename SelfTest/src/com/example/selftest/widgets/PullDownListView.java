package com.example.selftest.widgets;

import com.example.selftest.R;

import android.content.Context;
import android.nfc.Tag;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class PullDownListView extends ListView implements OnScrollListener {

	private static final String TAG = "PullDownListView";

	private final static int NONE_PULL_REFRESH = 0; // 正常状态
	private final static int ENTER_PULL_REFRESH = 1; // 进入下拉刷新状态
	private final static int OVER_PULL_REFRESH = 2; // 进入松手刷新状态
	private final static int EXIT_PULL_REFRESH = 3; // 松手后反弹后加载状态
	private int mPullRefreshState = 0; // 记录刷新状态

	private Context mContext;

	private View mHeaderView;
	private TextView mTVRefreshStatus;
	private TextView mTVLastRefreshTime;

	private int mHeaderHeight;
	private int mCurrentScrollState;

	private float mDownY;
	private float mMoveY;

	public PullDownListView(Context context) {
		super(context);
		mContext = context;

		init();
	}

	public PullDownListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;

		init();
	}

	private void init() {
		mHeaderView = LayoutInflater.from(mContext).inflate(
				R.layout.view_pull_list_header, null);

		mTVRefreshStatus = (TextView) mHeaderView
				.findViewById(R.id.tv_refresh_status);
		mTVLastRefreshTime = (TextView) mHeaderView
				.findViewById(R.id.tv_last_refresh_time);

		this.addHeaderView(mHeaderView);
		setSelection(1);
		setOnScrollListener(this);

		measureView(mHeaderView);
		mHeaderHeight = mHeaderView.getMeasuredHeight();
	}

	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}

		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		Log.d(TAG, "onTouchEvent, Action:" + ev.getAction());
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 记下按下位置
			// 改变
			mDownY = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			// 移动时手指的位置
			mMoveY = ev.getY();
			if (mPullRefreshState == OVER_PULL_REFRESH) {
				// 注意下面的mDownY在onScroll的第二个else中被改变了
				mHeaderView.setPadding(
						mHeaderView.getPaddingLeft(),
						(int) ((mMoveY - mDownY) / 3), // 1/3距离折扣
						mHeaderView.getPaddingRight(),
						mHeaderView.getPaddingBottom());
			}
			break;
		case MotionEvent.ACTION_UP:

			break;
		}
		return super.onTouchEvent(ev);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		mCurrentScrollState = scrollState;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (mCurrentScrollState == SCROLL_STATE_TOUCH_SCROLL
				&& firstVisibleItem == 0
				&& (mHeaderView.getBottom() >= 0 && mHeaderView.getBottom() < mHeaderHeight)) {
			// 进入且仅进入下拉刷新状态
			if (mPullRefreshState == NONE_PULL_REFRESH) {
				mPullRefreshState = ENTER_PULL_REFRESH;
			}
		} else if (mCurrentScrollState == SCROLL_STATE_TOUCH_SCROLL
				&& firstVisibleItem == 0
				&& (mHeaderView.getBottom() >= mHeaderHeight)) {
			// 下拉达到界限，进入松手刷新状态
			if (mPullRefreshState == ENTER_PULL_REFRESH
					|| mPullRefreshState == NONE_PULL_REFRESH) {
				mPullRefreshState = OVER_PULL_REFRESH;
				// 下面是进入松手刷新状态需要做的一个显示改变
				mDownY = mMoveY;// 用于后面的下拉特殊效果
				mTVRefreshStatus.setText("松手刷新");
				// mHeaderPullDownImageView.setVisibility(View.GONE);
				// mHeaderReleaseDownImageView.setVisibility(View.VISIBLE);
			}
		} else if (mCurrentScrollState == SCROLL_STATE_TOUCH_SCROLL
				&& firstVisibleItem != 0) {
			// 不刷新了
			if (mPullRefreshState == ENTER_PULL_REFRESH) {
				mPullRefreshState = NONE_PULL_REFRESH;
			}
		} else if (mCurrentScrollState == SCROLL_STATE_FLING
				&& firstVisibleItem == 0) {
			// 飞滑状态，不能显示出header，也不能影响正常的飞滑
			// 只在正常情况下才纠正位置
			if (mPullRefreshState == NONE_PULL_REFRESH) {
				setSelection(1);
			}
		}
	}
}
