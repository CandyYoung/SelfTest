package com.example.selftest.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.PopupWindow;

/**
 * Created by 海强 on 2015/5/7.
 */
public class MyCustomView extends View {

    private final String TEXT = "AaYyJj哈哈";
    private Paint mPaint;
    private Paint linePaint;
    private Paint.FontMetrics mFontMetrics;

    private int mViewWidth, mViewHeight;// 控件宽高

    public MyCustomView(Context context) {
        super(context);
        init();
    }

    public MyCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
//        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(50);
        mPaint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
        mFontMetrics = mPaint.getFontMetrics();

        linePaint = new Paint();
        linePaint.setColor(Color.RED);

        Log.d("Aige", "ascent：" + mFontMetrics.ascent);
        Log.d("Aige", "top：" + mFontMetrics.top);
        Log.d("Aige", "leading：" + mFontMetrics.leading);
        Log.d("Aige", "descent：" + mFontMetrics.descent);
        Log.d("Aige", "bottom：" + mFontMetrics.bottom);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        /*
         * 获取控件宽高
         */
        mViewWidth = w;
        mViewHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /*// 计算Baseline绘制的起点X轴坐标
        int baseX = (int) (canvas.getWidth() / 2 - mPaint.measureText(TEXT) / 2);

        // 计算Baseline绘制的Y坐标
        int baseY = (int) ((canvas.getHeight() / 2 - (mPaint.descent() + mPaint.ascent()) / 2));

        canvas.drawText(TEXT, baseX, baseY, mPaint);

        // 为了便于理解我们在画布中心处绘制一条中线
        canvas.drawLine(0, canvas.getHeight() / 2, canvas.getWidth(), canvas.getHeight() / 2, linePaint);*/

         /*
     * 绘制一个红色矩形
     */
        mPaint.setColor(Color.RED);
        canvas.drawRect(0, 0, 200, 200, mPaint);
//        canvas.drawRect(mViewWidth / 2F - 200, mViewHeight / 2F - 200, mViewWidth / 2F + 200, mViewHeight / 2F + 200, mPaint);

    /*
     * 保存画布并绘制一个蓝色的矩形
     */
//        canvas.saveLayer(0, 0, mViewWidth, mViewHeight, null, Canvas.ALL_SAVE_FLAG);
//        canvas.save();
//        mPaint.setColor(Color.BLUE);

        // 旋转画布
//        canvas.rotate(30);
//        canvas.drawRect(mViewWidth / 2F - 100, mViewHeight / 2F - 100, mViewWidth / 2F + 100, mViewHeight / 2F + 100, mPaint);
//        canvas.restore();
    }
}
