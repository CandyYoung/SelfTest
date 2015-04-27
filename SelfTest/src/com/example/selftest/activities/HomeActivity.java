package com.example.selftest.activities;

import com.example.selftest.R;
import com.example.selftest.fragments.GamesFragment;
import com.example.selftest.fragments.LiveFragment;
import com.example.selftest.fragments.RecommendFragment;
import com.example.selftest.widgets.BottomTabControl;
import com.example.selftest.widgets.BottomTabControl.OnTabSelected;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;

public class HomeActivity extends FragmentActivity implements OnTabSelected,
		OnClickListener {
	private static final String TAG = "HomeActivity";

	private FragmentManager mFragmentManager;
	private Fragment currentFragment;

	private RecommendFragment recommendFrag;
	private GamesFragment gamesFrag;
	private LiveFragment liveFrag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		findViewById(R.id.iv_follow).setOnClickListener(this);
		findViewById(R.id.iv_search).setOnClickListener(this);
		((BottomTabControl) findViewById(R.id.bottom_bar)).setTabSelected(this);
		mFragmentManager = getSupportFragmentManager();
		currentFragment = recommendFrag = new RecommendFragment();
		mFragmentManager.beginTransaction()
				.add(R.id.frag_content, recommendFrag).commit();
	}

	@Override
	public void tabSelected(int selectedIndex) {

		FragmentTransaction transaction = mFragmentManager.beginTransaction();
		if (currentFragment != null) {
			transaction.hide(currentFragment);
		}

		switch (selectedIndex) {
		case 0:
			if (recommendFrag == null) {
				recommendFrag = new RecommendFragment();
				transaction.add(R.id.frag_content, recommendFrag);
			} else {
				transaction.show(recommendFrag);
			}
			currentFragment = recommendFrag;
			break;

		case 1:
			if (liveFrag == null) {
				liveFrag = new LiveFragment();
				transaction.add(R.id.frag_content, liveFrag);
			} else {
				transaction.show(liveFrag);
			}
			currentFragment = liveFrag;
			break;

		case 2:
			if (gamesFrag == null) {
				gamesFrag = new GamesFragment();
				transaction.add(R.id.frag_content, gamesFrag);
			} else {
				transaction.show(gamesFrag);
			}
			currentFragment = gamesFrag;
			break;

		default:
			currentFragment = null;
			break;
		}

		transaction.commitAllowingStateLoss();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_search:
			Intent intent = new Intent(this, SearchActivity.class);
			startActivity(intent);
			break;

		case R.id.iv_follow:

			break;

		default:
			break;
		}

	}
}
