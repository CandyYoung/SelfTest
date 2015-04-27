package com.example.selftest.fragments;

import java.util.Locale;

import com.example.selftest.R;
import com.example.selftest.adapters.GameListAdapter;
import com.example.selftest.adapters.GameListAdapter.LoadMoreCallback;
import com.example.selftest.entity.GameInfo;
import com.example.selftest.entity.GameListResponse;
import com.example.selftest.entity.HttpAction;
import com.example.selftest.entity.MyWebResponse;
import com.example.selftest.utils.HttpUtil;
import com.example.selftest.utils.JsonUtil;
import com.example.selftest.utils.HttpUtil.WebRequestListener;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class GamesFragment extends Fragment implements WebRequestListener,
		LoadMoreCallback {
	private static final int RES_COMPLETE = 0;
	private static final int REQ_COUNT = 12;

    private View mRootView;
	private Activity mActivity;
	private GridView gv_games;
	private static GameListAdapter mAdapter;

    private PtrClassicFrameLayout mRefreshLayout;

    private boolean isRefresh;
	private static int currentIndex = 1;
	private boolean noMore = false;

	private Handler mHandler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			if (msg.what == RES_COMPLETE) {
				if (msg.obj != null) {

					if (!isRefresh) {
						mAdapter.appendData((GameInfo[]) msg.obj);
					} else {
                        mRefreshLayout.refreshComplete();
						mAdapter.setData((GameInfo[]) msg.obj);
					}
				}
			}

			return true;
		}
	});

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return mRootView = inflater.inflate(R.layout.fragment_games, container, false);
	}

	@Override
	public void onAttach(Activity activity) {
		mActivity = activity;
		mAdapter = new GameListAdapter(activity);
		mAdapter.setCallback(this);
		super.onAttach(activity);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

        mRefreshLayout = (PtrClassicFrameLayout) mRootView;
        mRefreshLayout.setLastUpdateTimeRelateObject(this);
        mRefreshLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                noMore = false;
                isRefresh = true;
                currentIndex = 1;
                getGames();
            }
        });
		gv_games = (GridView) mRootView.findViewById(R.id.gv_games);
		gv_games.setAdapter(mAdapter);
        isRefresh = true;
		getGames();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	private boolean isBusy;

	private void getGames() {
		if (isBusy) {
			return;
		}
		isBusy = true;
		HttpUtil.get(String.format(Locale.getDefault(),
				"http://www.zhanqi.tv/api/static/game.lists/%d-%d.json",
				REQ_COUNT, currentIndex), HttpAction.HOME_GAME, this);
	}

	@Override
	public void loadMore() {
		if (!noMore) {
            isRefresh = false;
			getGames();
		}
	}

	@Override
	public void onComplete(MyWebResponse result) {
		isBusy = false;
		if (result == null || result.getResponseString() == "") {
			return;
		}
		try {
			GameListResponse resp = JsonUtil.fromJson(result.getResponseString(),
					new TypeToken<GameListResponse>() {
					}.getType());
			if (resp != null) {
				mHandler.obtainMessage(RES_COMPLETE, resp.getData().getGames())
						.sendToTarget();

				if (resp.getData().getGames().length < REQ_COUNT) {
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
}
