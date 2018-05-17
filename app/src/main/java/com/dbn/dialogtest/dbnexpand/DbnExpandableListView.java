package com.dbn.dialogtest.dbnexpand;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

/**
 * Created by Administrator on 2018/5/16.
 */

public class DbnExpandableListView extends ExpandableListView  {
    private String TAG = DbnExpandableListView.class.getSimpleName();
    private View mHeaderView;////头部View
    private int marginTop = 0;
    private int mHeaderViewWidth;
    private int mHeaderViewHeight;

    public DbnExpandableListView(Context context) {
        super(context);
    }

    public DbnExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DbnExpandableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mHeaderView != null && mHeaderView.getVisibility() == VISIBLE) {
            drawChild(canvas, mHeaderView, getDrawingTime());
        }
    }

    public void setHeaderView(View headerView) {
        this.mHeaderView = headerView;
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        headerView.setLayoutParams(lp);

        if (mHeaderView != null) {
            setFadingEdgeLength(0);
            mHeaderView.setPadding(0,0, 0,getDividerHeight()*2);
        }
        requestLayout();
    }

    ///因为标题栏是绘制出来的，不可点击，需要拦截时间传递到子item
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN && mHeaderView != null && mHeaderView.getVisibility() == VISIBLE) {
            Rect rect = new Rect();
            mHeaderView.getDrawingRect(rect);
            if (rect.contains((int)ev.getX(), (int)ev.getY())) {
                return true;
            }
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mHeaderView != null && mHeaderView.getVisibility() == VISIBLE) {
            Rect rect = new Rect();
            mHeaderView.getDrawingRect(rect);
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (rect.contains((int)ev.getX(), (int)ev.getY())) {
                        // 阻止时间传递给子item
                        return true;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    long flatPostion = getExpandableListPosition(getFirstVisiblePosition());
                    int groupPos = ExpandableListView.getPackedPositionGroup(flatPostion);
                    if (rect.contains((int)ev.getX(), (int)ev.getY())) {
                        // 处理悬浮标题点击时间
                        if (isGroupExpanded(groupPos)) {
                            collapseGroup(groupPos);
                        } else {
                            expandGroup(groupPos);
                        }
                        return true;
                    }
                    break;
            }
        }

        return super.onTouchEvent(ev);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        final long flatPostion = getExpandableListPosition(getFirstVisiblePosition());
        final int groupPos = ExpandableListView.getPackedPositionGroup(flatPostion);
        final int childPos = ExpandableListView.getPackedPositionChild(flatPostion);
        if (mHeaderView != null) {
            mHeaderView.setBackground(getDivider());
            Log.e(TAG, "marginTop = " + marginTop);
            mHeaderView.layout(0, marginTop == 0?0:marginTop - mHeaderViewHeight , mHeaderViewWidth,  marginTop==0?mHeaderViewHeight :marginTop);
        }
        Drawable drawable = getDivider();


    }

    @Override
    public void setDivider(@Nullable Drawable divider) {
        super.setDivider(divider);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        int count = getChildCount();
        //Log.e(TAG, "childcount = " + count);

        if(mHeaderView != null) {
            if (count > 1 && getChildAt(0) instanceof DbnGroupView && getChildAt(1) instanceof DbnChildView) {
                mHeaderView.setVisibility(VISIBLE);
                marginTop = 0;
            } else if (count > 1 && getChildAt(0) instanceof DbnChildView && getChildAt(1) instanceof DbnChildView) {
                View childView = getChildAt(0);
                AbsListView.LayoutParams params = (LayoutParams) childView.getLayoutParams();
                View childView2 = getChildAt(1);
                AbsListView.LayoutParams params1 = (LayoutParams) childView.getLayoutParams();
                mHeaderView.setVisibility(VISIBLE);
                if(count>2 && getChildAt(2) instanceof DbnGroupView && getChildAt(2).getTop()< mHeaderViewHeight) {
                    marginTop = getChildAt(2).getTop() - params.height;
                } else {
                    marginTop = 0;
                }
            } else if (count > 1 && getChildAt(0) instanceof DbnChildView && getChildAt(1) instanceof DbnGroupView) {
                mHeaderView.setVisibility(VISIBLE);
                View groupView = getChildAt(1);
                AbsListView.LayoutParams params = (LayoutParams) groupView.getLayoutParams();
                marginTop = groupView.getTop() - params.height;
            } else if (count > 1 && getChildAt(0) instanceof DbnGroupView && getChildAt(1) instanceof DbnGroupView) {
                mHeaderView.setVisibility(GONE);
                marginTop = 0;
            }
        }
        requestLayout();
        final long flatPos = getExpandableListPosition(getFirstVisiblePosition());
        int groupPosition = ExpandableListView.getPackedPositionGroup(flatPos);
        int childPosition = ExpandableListView.getPackedPositionChild(flatPos);
        if(getExpandableListAdapter() != null) {
            ((DbnExpandableAdapter) getExpandableListAdapter()).onPositionChanged(mHeaderView ,groupPosition, childPosition);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mHeaderView != null) {
            measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);
            mHeaderViewWidth = mHeaderView.getMeasuredWidth();
            mHeaderViewHeight = mHeaderView.getMeasuredHeight();
        }
    }

    @Override
    public void setAdapter(ExpandableListAdapter adapter) {
        super.setAdapter(adapter);
    }

    ////返回当前分组信息
    public interface HeaderAdapter {

        void onPositionChanged(View headerView ,int groupPosition, int childPosition);
    }

}
