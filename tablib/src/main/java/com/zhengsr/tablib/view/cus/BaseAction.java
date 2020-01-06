package com.zhengsr.tablib.view.cus;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.zhengsr.tablib.Constants;
import com.zhengsr.tablib.R;
import com.zhengsr.tablib.view.TabFlowLayout;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe: 绘制基类
 */
public abstract class BaseAction implements ViewPager.OnPageChangeListener {
    protected Paint mPaint;
    protected RectF mRect;
    protected TabFlowLayout mParentView;

    /**
     * logic
     */
    protected int mScreenWidth;
    protected int mRightBound;
    private ValueAnimator mAnimator;
    protected float mOffset;
    /**
     * attrs
     */
    protected float mMarginLeft;
    protected float mMarginTop;
    protected float mMarginRight;
    protected float mMarginBottom;
    protected int mType;
    protected int mTabWidth;
    protected int mTabHeight;
    protected int mAnimTime;

    public BaseAction() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mRect = new RectF();
    }
    public void config(TabFlowLayout parentView){
        mParentView = parentView;
        mScreenWidth = mParentView.getResources().getDisplayMetrics().widthPixels;
        int childCount = mParentView.getChildCount();
        if (childCount > 0){
            View child = mParentView.getChildAt(childCount - 1);
            mRightBound = child.getRight() + mParentView.getPaddingRight();

        }

        View child = mParentView.getChildAt(0);
        if (child != null) {
            mOffset = mTabWidth * 1.0f / child.getMeasuredWidth();
        }
    }



    public void setViewPager(ViewPager viewPager){
        viewPager.addOnPageChangeListener(null);
        viewPager.addOnPageChangeListener(this);
    }


    /**
     * 点击事件
     * @param lastIndex
     * @param curIndex
     */
    public void onItemClick(int lastIndex,int curIndex){
        doAnim(lastIndex,curIndex);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        /**
         * position 当前第一页的索引，比较有意思的是，当右滑时，position 表示当前页面，当左滑时，为当前页面减1；
         * positionOffset 当前页面移动的百分比
         * positionOffsetPixels 当前页面移动的像素
         */

        View curView = mParentView.getChildAt(position);

        float offset = curView.getWidth() * positionOffset;
        int scrollX = (int) (curView.getLeft() + offset);
        if (position < mParentView.getChildCount() - 1) {
            //当前view
            final View transView = mParentView.getChildAt(position + 1);
            //左边偏移量
            float left = curView.getLeft() + positionOffset * (transView.getLeft() - curView.getLeft());
            //右边表示宽度变化
            float right = curView.getRight() + positionOffset * (transView.getRight() - curView.getRight());
            mRect.left = left;
            mRect.right = right;
            mParentView.postInvalidate();

            //超过中间了，让父控件也跟着移动
            if (scrollX > mScreenWidth / 2 - mParentView.getPaddingLeft() ) {
                scrollX -= mScreenWidth / 2 - mParentView.getPaddingLeft();
                //有边界提醒
                if (scrollX  < mRightBound - mScreenWidth) {
                    mParentView.scrollTo(scrollX, 0);
                }
            }else{
                mParentView.scrollTo(0,0);

            }
        }
    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }


    /**
     * 执行点击移动动画
     * @param lastIndex
     * @param curIndex
     */
    private void doAnim(int lastIndex, final int curIndex) {
        if (mAnimator != null) {
            mAnimator.cancel();
            mAnimator.end();
            mAnimator = null;
        }
        final View curView = mParentView.getChildAt(curIndex);
        final View lastView = mParentView.getChildAt(lastIndex);

        TabTypeValue lastValue = getValue(lastView);
        TabTypeValue curValue = getValue(curView);
        if (mTabWidth != -1){
            lastValue.left = mRect.left;
            lastValue.right = mRect.right;
            int width = curView.getMeasuredWidth();
            if (mType == Constants.RECT) {
                curValue.left = (1 - mOffset) * width / 2 + curView.getLeft();
                curValue.right = width * mOffset + curValue.left;
            }else{
                curValue.left = (width  - mTabWidth) / 2 + curView.getLeft();
                curValue.right = mTabWidth  + curValue.left;
            }
        }
        mAnimator = ObjectAnimator.ofObject(new TabType(), lastValue, curValue);
        mAnimator.setDuration(mAnimTime);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                TabTypeValue value = (TabTypeValue) animation.getAnimatedValue();
                  valueChange(value);
                  mParentView.postInvalidate();
            }
        });
        mAnimator.start();
    }

    private TabTypeValue getValue(View view) {
        TabTypeValue value = new TabTypeValue();
        value.left = view.getLeft() ;
        value.right = view.getRight();
        return value;
    }



    protected void valueChange(TabTypeValue value){
        mRect.left = value.left;
        mRect.right = value.right;
    }

    /**
     * 拿到自定义属性
     * @param ta
     */
    public  void configAttrs(TypedArray ta){
        mTabWidth = ta.getDimensionPixelSize(R.styleable.TabFlowLayout_tab_width,-1);
        mTabHeight = ta.getDimensionPixelSize(R.styleable.TabFlowLayout_tab_height,-1);
        int color = ta.getColor(R.styleable.TabFlowLayout_tab_color, Color.RED);
        mPaint.setColor(color);
        mType = ta.getInteger(R.styleable.TabFlowLayout_tab_style,-1);
        mMarginLeft = ta.getDimensionPixelSize(R.styleable.TabFlowLayout_tab_margin_l,0);
        mMarginTop = ta.getDimensionPixelSize(R.styleable.TabFlowLayout_tab_margin_t,0);
        mMarginRight = ta.getDimensionPixelSize(R.styleable.TabFlowLayout_tab_margin_r,0);
        mMarginBottom = ta.getDimensionPixelSize(R.styleable.TabFlowLayout_tab_margin_b,0);
        mAnimTime = ta.getInteger(R.styleable.TabFlowLayout_tab_click_animTime,300);
    }
    /**
     * 绘制不同的view
     */
    public abstract void draw(Canvas canvas);

    /**
     * 自定义 TypeEvaluator
     */
    class TabType implements TypeEvaluator<TabTypeValue> {
       // PointF pointF = new PointF();
        TabTypeValue value = new TabTypeValue();
        @Override
        public TabTypeValue evaluate(float fraction, TabTypeValue startValue, TabTypeValue endValue) {
            //这里都采用匀速
            value.left = startValue.left + fraction * (endValue.left - startValue.left);
            value.right = startValue.right + fraction * (endValue.right - startValue.right);
            return value;
        }
    }

    class TabTypeValue {
        public float left;
        public float right;

        @Override
        public String toString() {
            return "TabTypeValue{" +
                    "left=" + left +
                    ", right=" + right +
                    '}';
        }
    }
}
