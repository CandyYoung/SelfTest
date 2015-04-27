package com.example.selftest.utils;

import com.example.selftest.entity.AnchorInfo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
	private static final String TAG = "DBHelper";
	// 数据库名称常量
	private static final String DATABASE_NAME = "Anchors.db";
	// 数据库版本常量
	private static final int DATABASE_VERSION = 1;
	// 表名称常量
	public static final String ANCHORS_TABLE_NAME = "Anchors";

	public DBHelper(Context context) {
		// 创建数据库
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(TAG, "onCreate");
		db.execSQL("CREATE TABLE " + ANCHORS_TABLE_NAME + " ("
                + AnchorInfo._ID + " INTEGER PRIMARY KEY,"
                + AnchorInfo.NICKNAME + " TEXT,"
                + AnchorInfo.AVATAR + " TEXT,"
                + ");");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(TAG, "onUpgrade");
		db.execSQL("DROP TABLE IF EXISTS employee");
        onCreate(db);
	}

}
