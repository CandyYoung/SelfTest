package com.example.selftest.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;


/**
 * 嵌套在ScrollView中，可做到ListView在其自身高度内可滑动，同时滑到顶或底时ScrollView部分继续滑动。
 * 使用时须指定控件高度为确认值
 * Created by 海强 on 2015/4/23.
 */
public class InnerListView extends ListView {
    public InnerListView(Context context) {
        super(context);
    }

    public InnerListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InnerListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private ScrollView parentScrollView;
    private int currentY;

    private ScrollView getParentScrollView(){
        if (parentScrollView == null) {
            if (getParent().getParent() instanceof  ScrollView) {
                parentScrollView = (ScrollView) getParent().getParent();
            }
        }

        return parentScrollView;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (getParentScrollView() == null) {
            return super.onInterceptTouchEvent(ev);
        } else {

            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    currentY = (int) ev.getY();
                    setParentScrollAble(false);
                    return super.onInterceptTouchEvent(ev);
                case MotionEvent.ACTION_MOVE:

                    break;
                case MotionEvent.ACTION_UP:

                case MotionEvent.ACTION_CANCEL:
                    setParentScrollAble(true);
                    break;
            }

        }
        return super.onInterceptTouchEvent(ev);

    }

    /**
     * 获取listView的滚动位置，如果直接用getScrollY();一直是0。所以要自己算下<br>
     *
     * @return
     */
    private int getTrueScrollY() {
        View c = getChildAt(0);
        if (c == null) {
            return 0;
        }
        int firstVisiblePosition = getFirstVisiblePosition();// listView中 当前可视的 第一个item的序号
        int top = c.getTop();
        return -top + firstVisiblePosition * c.getHeight();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (getParentScrollView() != null) {
            if (ev.getAction() == MotionEvent.ACTION_MOVE) {
                int itemCount = getAdapter().getCount();
                View child = getChildAt(0);
                int height = child.getMeasuredHeight() * itemCount;
                height = height - getMeasuredHeight();

                // System.out.println("height=" + height);
                int scrollY = getTrueScrollY();
                // System.out.println("scrollY" + scrollY);
                int y = (int) ev.getY();

                // 手指向下滑动
                if (currentY < y) {
                    if (scrollY <= 0) {
                        // 如果向下滑动到头，就把滚动交给父Scrollview
                        setParentScrollAble(true);
                        return false;
                    } else {
                        setParentScrollAble(false);

                    }
                } else if (currentY > y) {
                    if (scrollY >= height) {
                        // 如果向上滑动到头，就把滚动交给父Scrollview
                        setParentScrollAble(true);
                        return false;
                    } else {
                        setParentScrollAble(false);

                    }

                }
                currentY = y;
            }
        }

        return super.onTouchEvent(ev);
    }

    /**
     * 是否把滚动事件交给父scrollview
     *
     * @param flag
     */
    private void setParentScrollAble(boolean flag) {
        if (getParentScrollView() != null) {
            parentScrollView.requestDisallowInterceptTouchEvent(!flag);//这里的parentScrollView就是listview外面的那个scrollview
        }
    }


    private int maxHeight;
    public int getMaxHeight() {
        return maxHeight;
    }
    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (maxHeight > 0) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
