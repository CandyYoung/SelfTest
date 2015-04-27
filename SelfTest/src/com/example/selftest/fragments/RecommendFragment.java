package com.example.selftest.fragments;

import com.example.selftest.R;
import com.example.selftest.adapters.RecommendListAdapter;
import com.example.selftest.entity.HttpAction;
import com.example.selftest.entity.MyWebResponse;
import com.example.selftest.entity.RecommendItem;
import com.example.selftest.entity.RecommendResponse;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class RecommendFragment extends Fragment implements WebRequestListener {
	private static final String TAG = "RecommendFragment";

	private Activity mActivity;
	private View mViewRoot;

	private static final int RES_COMPLETE = 0;
	private ListView mListView;
	private static RecommendListAdapter mAdapter;
    private PtrClassicFrameLayout mFrameLayout;

	private static Handler mHandler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			if (msg.what == RES_COMPLETE) {
				if (msg.obj != null) {
					mAdapter.setData((RecommendItem[]) msg.obj);
				}
			}

			return true;
		}
	});

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
		return mViewRoot = inflater.inflate(R.layout.fragment_recommend,
				container, false);
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

		mListView = (ListView) mViewRoot.findViewById(R.id.list);
		mAdapter = new RecommendListAdapter(null, mActivity);
		mListView.setAdapter(mAdapter);

        mFrameLayout = (PtrClassicFrameLayout) mViewRoot.findViewById(R.id.frame_layout);
        mFrameLayout.setLastUpdateTimeRelateObject(this);
        mFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                getRecommend();
            }
        });

		getRecommend();
	}

	@Override
	public void onPause() {
		Log.i(TAG, "onPause");
		super.onPause();
		if (mHandler != null) {
			mHandler.removeCallbacksAndMessages(null);
		}
	}

	private void getRecommend() {
		HttpUtil.get(
				"http://www.zhanqi.tv/api/static/live.index/recommend-apps.json",
				HttpAction.HOME_RECOMMEND, this);
	}

	@Override
	public void onComplete(MyWebResponse result) {
        mFrameLayout.refreshComplete();

		if (result == null || result.getResponseString() == "") {
			return;
		}
		try {
			RecommendResponse resp = JsonUtil.fromJson(
					result.getResponseString(),
					new TypeToken<RecommendResponse>() {
					}.getType());
			if (resp != null) {
				mHandler.obtainMessage(RES_COMPLETE, resp.getData())
						.sendToTarget();

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
