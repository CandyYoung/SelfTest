package com.example.selftest.utils;

import android.content.Context;
import android.content.SharedPreferences.Editor;

public class SharedPreferenceManager {
	private static final String TAG = "SharedReferenceManager";
	private static final String PREFERENCE_NAME = "_selftest";
	private Context mContext;

	SharedPreferenceManager(Context context) {
		mContext = context;
	}

	public void putString(String key, String value) {
		Editor editor = mContext.getSharedPreferences(PREFERENCE_NAME, 0)
				.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public void putBoolean(String key, boolean value) {
		Editor editor = mContext.getSharedPreferences(PREFERENCE_NAME, 0)
				.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public void putInt(String key, int value) {
		Editor editor = mContext.getSharedPreferences(PREFERENCE_NAME, 0)
				.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public void putFloat(String key, float value) {
		Editor editor = mContext.getSharedPreferences(PREFERENCE_NAME, 0)
				.edit();
		editor.putFloat(key, value);
		editor.commit();
	}

	public void putLong(String key, long value) {
		Editor editor = mContext.getSharedPreferences(PREFERENCE_NAME, 0)
				.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	public String getString(String key) {
		return mContext.getSharedPreferences(PREFERENCE_NAME, 0).getString(key,
				"");
	}

	public int getInt(String key) {
		return mContext.getSharedPreferences(PREFERENCE_NAME, 0).getInt(key, 0);
	}

	public boolean getBoolean(String key) {
		return mContext.getSharedPreferences(PREFERENCE_NAME, 0).getBoolean(
				key, false);
	}

	public long getLong(String key) {
		return mContext.getSharedPreferences(PREFERENCE_NAME, 0).getLong(key,
				0l);
	}

	public float getFloat(String key) {
		return mContext.getSharedPreferences(PREFERENCE_NAME, 0).getFloat(key,
				0f);
	}
}
