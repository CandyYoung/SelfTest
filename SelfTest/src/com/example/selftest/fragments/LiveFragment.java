package com.example.selftest.fragments;

import java.util.Locale;

import com.example.selftest.R;
import com.example.selftest.adapters.VideoItemAdapter;
import com.example.selftest.adapters.VideoItemAdapter.LoadMoreCallback;
import com.example.selftest.entity.HttpAction;
import com.example.selftest.entity.LiveListResponse;
import com.example.selftest.entity.MyWebResponse;
import com.example.selftest.entity.RoomInfo;
import com.example.selftest.utils.HttpUtil;
import com.example.selftest.utils.HttpUtil.WebRequestListener;
import com.example.selftest.utils.JsonUtil;
import com.example.selftest.widgets.LoadingView;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.PtrUIHandler;

public class LiveFragment extends Fragment implements WebRequestListener,
		LoadMoreCallback {
	private static final String TAG = "LiveFragment";

	private static final int RES_COMPLETE = 0;
	private static final int REQ_COUNT = 20;

	private String gameId;

	private Activity mActivity;
	private GridView mGridView;
	private VideoItemAdapter mAdapter;
	private LoadingView mLoadingView;

    private PtrClassicFrameLayout mRefreshLayout;

    private boolean isRefresh;
	private int currentIndex = 1;
	private boolean noMore = false;

	private Handler mHandler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			if (msg.what == RES_COMPLETE) {
				if (msg.obj != null) {

					if (!isRefresh) {
						mAdapter.appendData((RoomInfo[]) msg.obj);
					} else {
                        mRefreshLayout.refreshComplete();
						mAdapter.setData((RoomInfo[]) msg.obj);
					}
				}
			}
			
			if (mAdapter.getData() == null || mAdapter.getData().size() <= 0) {
				mLoadingView.loadFailed("暂时获取不到数据");
			} else {
				mLoadingView.loadSuccess();
			}

			return true;
		}
	});

	public LiveFragment() {
		// TODO Auto-generated constructor stub
		
	}

	public LiveFragment(String gameId) {
		this.gameId = gameId;
	}

	@Override
	public void onAttach(Activity activity) {
		Log.i(TAG, "onAttach");
		super.onAttach(activity);
		mActivity = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i(TAG, "onCreateView");
		return inflater.inflate(R.layout.fragment_livelist, container, false);
	}

	@Override
	public void onResume() {
		Log.i(TAG, "onResume");
		super.onResume();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		Log.i(TAG, "onViewCreated");
		super.onViewCreated(view, savedInstanceState);

		mGridView = (GridView) view.findViewById(R.id.gv_videos);
		mAdapter = new VideoItemAdapter(mActivity, null);
		mAdapter.setCallback(this);
		mGridView.setAdapter(mAdapter);
		
		mLoadingView = (LoadingView) view.findViewById(R.id.loading_view);
        mRefreshLayout = (PtrClassicFrameLayout) view.findViewById(R.id.frame_layout);
        mRefreshLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                noMore = false;
                isRefresh = true;
                currentIndex = 1;
                getLiveList();
            }
        });
		mLoadingView.startLoading("正在玩命加载...");
        isRefresh = true;
		getLiveList();
	}

	private boolean isBusy;

	private void getLiveList() {
		if (isBusy) {
			return;
		}
		isBusy = true;
		
		if (TextUtils.isEmpty(gameId)) {
			HttpUtil.get(String.format(Locale.getDefault(),
					"http://www.zhanqi.tv/api/static/live.hots/%d-%d.json",
					REQ_COUNT, currentIndex), HttpAction.HOME_LIVE_HOT, this);
		} else {
			HttpUtil.get(String.format(Locale.getDefault(),
					"http://www.zhanqi.tv/api/static/game.lives/%s/%d-%d.json",
					gameId, REQ_COUNT, currentIndex), HttpAction.LIVE_GAME,
					this);
		}
	}

	@Override
	public void onComplete(MyWebResponse result) {
		isBusy = false;
		if (result == null || result.getResponseString() == "") {
			return;
		}
		try {
			LiveListResponse resp = JsonUtil.fromJson(
					result.getResponseString(),
					new TypeToken<LiveListResponse>() {
					}.getType());
			if (resp != null) {
				mHandler.obtainMessage(RES_COMPLETE, resp.getData().getRooms())
						.sendToTarget();

				if (resp.getData().getRooms().length < REQ_COUNT) {
					noMore = true;
				} else {
					currentIndex++;
				}

				if (resp.getCode() == "1") {
					Toast.makeText(mActivity, resp.getMessage(),
							Toast.LENGTH_SHORT).show();
				} 
			}
		} catch (Exception e) {
			Log.e("onComplete", "Json parse exception");
			e.printStackTrace();
		}
	}

	@Override
	public void loadMore() {
		if (!noMore) {
            isRefresh = false;
			getLiveList();
		}
	}
}
