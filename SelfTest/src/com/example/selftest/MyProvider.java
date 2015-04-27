package com.example.selftest;

import java.util.HashMap;

import com.example.selftest.entity.AnchorInfo;
import com.example.selftest.utils.DBHelper;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class MyProvider extends ContentProvider {
	private static final String TAG = "ContentProvider";
	private DBHelper dbHelper;
	private static final UriMatcher sUriMatcher;
	
	// 查询、更新条件
    private static final int ANCHOR = 1;
    private static final int ANCHOR_ID = 2;
    // 查询列集合
    private static HashMap<String, String> empProjectionMap;
    static {
        // Uri匹配工具类
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AnchorInfo.AUTHORITY, "anchor", ANCHOR);
        sUriMatcher.addURI(AnchorInfo.AUTHORITY, "anchor/#", ANCHOR_ID);
        // 实例化查询列集合
        empProjectionMap = new HashMap<String, String>();
        // 添加查询列
        empProjectionMap.put(AnchorInfo._ID, AnchorInfo._ID);
        empProjectionMap.put(AnchorInfo.NICKNAME, AnchorInfo.NICKNAME);
        empProjectionMap.put(AnchorInfo.AVATAR, AnchorInfo.AVATAR);
    }

	@Override
	public boolean onCreate() {
		if (dbHelper == null) {
			Log.d(TAG, "onCreate");
			dbHelper = new DBHelper(getContext());
		}
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Log.d(TAG, "query");
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch (sUriMatcher.match(uri)) {
        // 查询所有
        case ANCHOR:
            qb.setTables(DBHelper.ANCHORS_TABLE_NAME);
            qb.setProjectionMap(empProjectionMap);
            break;
        // 根据ID查询
        case ANCHOR_ID:
            qb.setTables(DBHelper.ANCHORS_TABLE_NAME);
            qb.setProjectionMap(empProjectionMap);
            qb.appendWhere(AnchorInfo._ID + "=" + uri.getPathSegments().get(1));
            break;
        default:
            throw new IllegalArgumentException("Uri错误！ " + uri);
        }

        // 使用默认排序
        String orderBy;
        if (TextUtils.isEmpty(sortOrder)) {
            orderBy = AnchorInfo.DEFAULT_SORT_ORDER;
        } else {
            orderBy = sortOrder;
        }

        // 获得数据库实例
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // 返回游标集合
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, orderBy);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
	}

	@Override
	public String getType(Uri uri) {
		Log.d(TAG, "getType");
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Log.d(TAG, "insert");
		// 获得数据库实例
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // 插入数据，返回行ID
        long rowId = db.insert(DBHelper.ANCHORS_TABLE_NAME, AnchorInfo.NICKNAME, values);
        // 如果插入成功返回uri
        if (rowId > 0) {
            Uri empUri = ContentUris.withAppendedId(AnchorInfo.CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(empUri, null);
            return empUri;
        }
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		Log.d(TAG, "delete");
		// 获得数据库实例
        SQLiteDatabase db = dbHelper.getWritableDatabase();
         // 获得数据库实例
        int count;
        switch (sUriMatcher.match(uri)) {
        // 根据指定条件删除
        case ANCHOR:
            count = db.delete(DBHelper.ANCHORS_TABLE_NAME, selection, selectionArgs);
            break;
        // 根据指定条件和ID删除
        case ANCHOR_ID:
            String noteId = uri.getPathSegments().get(1);
            count = db.delete(DBHelper.ANCHORS_TABLE_NAME, AnchorInfo._ID + "=" + noteId
                    + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
            break;

        default:
            throw new IllegalArgumentException("错误的 URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		Log.d(TAG, "update");
		// 获得数据库实例
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
        // 根据指定条件更新
        case ANCHOR:
            count = db.update(DBHelper.ANCHORS_TABLE_NAME, values, selection, selectionArgs);
            break;
        // 根据指定条件和ID更新
        case ANCHOR_ID:
            String noteId = uri.getPathSegments().get(1);
            count = db.update(DBHelper.ANCHORS_TABLE_NAME, values, AnchorInfo._ID + "=" + noteId
                    + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
            break;
        default:
            throw new IllegalArgumentException("错误的 URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
	}

}
