package com.example.selftest.utils;

import java.util.ArrayList;
import java.util.List;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

public class EnphasizeUtil {
	private static String START_MARK;
	private static String END_MARK;

	public static SpannableString transformText(String plain, String startMark,
			String endMark, int enphasizeColor) {
		EnphasizeUtil.START_MARK = startMark;
		EnphasizeUtil.END_MARK = endMark;

		List<Marker> markers = findMarker(plain);
		String trimed = plain.replace(START_MARK, "").replace(END_MARK, "");
		SpannableString text = new SpannableString(trimed);
		for (Marker marker : markers) {
			text.setSpan(new ForegroundColorSpan(enphasizeColor), marker.start,
					marker.end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		
		return text;
	}

	private static List<Marker> findMarker(String plain) {
		List<Marker> markers = new ArrayList<Marker>();
		int start = plain.indexOf(START_MARK);
		int end = plain.indexOf(END_MARK);
		while (start >= 0 && end > start + START_MARK.length()) {
			markers.add(new Marker(start
					- (START_MARK.length() + END_MARK.length())
					* markers.size(), end - START_MARK.length()
					* (markers.size() + 1) - END_MARK.length() * markers.size()));

			start = plain.indexOf(START_MARK, end + END_MARK.length());
			end = plain.indexOf(END_MARK, end + END_MARK.length());
		}

		return markers;
	}

	static class Marker {
		private int start;
		private int end;

		Marker(int start, int end) {
			this.start = start;
			this.end = end;
		}
	}
}
