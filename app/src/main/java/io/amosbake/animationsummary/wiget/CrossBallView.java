package io.amosbake.animationsummary.wiget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * Author: mopel
 * Date : 2017/1/4
 */
public class CrossBallView extends View implements View.OnClickListener {

    // 默认小球最大半径
    private final static int DEFAULT_MAX_RADIUS = 75;
    // 默认小球最小半径
    private final static int DEFAULT_MIN_RADIUS = 25;
    // 默认两个小球运行轨迹直径距离
    private final static int DEFAULT_DISTANCE = 100;
    // 默认第一个小球颜色
    private final static int DEFAULT_ONE_BALL_COLOR = Color.parseColor("#40df73");
    // 默认第二个小球颜色
    private final static int DEFAULT_TWO_BALL_COLOR = Color.parseColor("#ffdf3e");
    // 默认动画执行时间
    private final static int DEFAULT_ANIMATOR_DURATION = 1000;
    // 画笔
    private Paint mPaint;
    // 球的最大半径
    private float maxRadius = DEFAULT_MAX_RADIUS;
    // 球的最小半径
    private float minRadius = DEFAULT_MIN_RADIUS;
    // 两球旋转的范围距离
    private int distance = DEFAULT_DISTANCE;
    // 动画的时间
    private long duration = DEFAULT_ANIMATOR_DURATION;
    private Ball mOneBall;
    private Ball mTwoBall;
    private float mCenterX;
    private float mCenterY;
    private AnimatorSet animatorSet;


    public CrossBallView(Context context) {
        this(context,null);
    }

    public CrossBallView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CrossBallView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mOneBall = new Ball();
        mTwoBall = new Ball();
        mOneBall.setColor(DEFAULT_ONE_BALL_COLOR);
        mTwoBall.setColor(DEFAULT_TWO_BALL_COLOR);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        configAnimator();
        setOnClickListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mCenterX = getMeasuredWidth()/2;
        mCenterY = getMeasuredHeight()/2;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w/2;
        mCenterY = h/2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mOneBall.getRadius()>mTwoBall.getRadius()){
            mPaint.setColor(mTwoBall.getColor());
            canvas.drawCircle(mTwoBall.getCenterX(),mCenterY,mTwoBall.getRadius(),mPaint);
            mPaint.setColor(mOneBall.getColor());
            canvas.drawCircle(mOneBall.getCenterX(),mCenterY,mOneBall.getRadius(),mPaint);
        }else{
            mPaint.setColor(mOneBall.getColor());
            canvas.drawCircle(mOneBall.getCenterX(),mCenterY,mOneBall.getRadius(),mPaint);
            mPaint.setColor(mTwoBall.getColor());
            canvas.drawCircle(mTwoBall.getCenterX(),mCenterY,mTwoBall.getRadius(),mPaint);
        }
    }

    private void configAnimator(){
        // 中间半径大小
        float centerRadius = (maxRadius + minRadius) * 0.5f;
        // 第一个小球缩放动画，通过改变小球的半径
        // 半径变化规律：中间大小->最大->中间大小->最小->中间大小
        ObjectAnimator oneScaleAnimator = ObjectAnimator.ofFloat(mOneBall,"radius",centerRadius,maxRadius,centerRadius,minRadius,centerRadius);
        //无限循环
        oneScaleAnimator.setRepeatCount(ValueAnimator.INFINITE);
        // 第一个小球位移动画，通过改变小球的圆心
        ValueAnimator oneCenterAnimator = ValueAnimator.ofFloat(-1,0,1,0,-1);
        oneCenterAnimator.setRepeatCount(ValueAnimator.INFINITE);
        oneCenterAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                float x = mCenterX + (distance) * value;
                mOneBall.setCenterX(x);
                invalidate();
            }
        });
        // 第二个小球缩放动画
        // 变化规律：中间大小->最小->中间大小->最大->中间大小
        ObjectAnimator twoScaleAnimator = ObjectAnimator.ofFloat(mTwoBall, "radius", centerRadius, minRadius,
                centerRadius, maxRadius, centerRadius);
        twoScaleAnimator.setRepeatCount(ValueAnimator.INFINITE);
        // 第二个小球位移动画
        ValueAnimator twoCenterAnimator = ValueAnimator.ofFloat(1, 0, -1, 0, 1);
        twoCenterAnimator.setRepeatCount(ValueAnimator.INFINITE);
        twoCenterAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                float x = mCenterX + (distance) * value;
                mTwoBall.setCenterX(x);
            }
        });
        // 属性动画集合
        animatorSet = new AnimatorSet();
        // 四个属性动画一块执行
        animatorSet.playTogether(oneScaleAnimator, oneCenterAnimator, twoScaleAnimator, twoCenterAnimator);
        // 动画一次运行时间
        animatorSet.setDuration(DEFAULT_ANIMATOR_DURATION);
        // 时间插值器，这里表示动画开始最快，结尾最慢
        animatorSet.setInterpolator(new DecelerateInterpolator());

    }

    @Override
    public void onClick(View v) {
        if (animatorSet.isRunning()){
            animatorSet.cancel();
        }else {
            animatorSet.start();
        }

    }

    /**
     * 小球
     */
    private class Ball {

        private float radius;// 半径
        private float centerX;// 圆心
        private int color;// 颜色

        float getRadius() {
            return radius;
        }

        public void setRadius(float radius) {
            this.radius = radius;
        }

        float getCenterX() {
            return centerX;
        }

        void setCenterX(float centerX) {
            this.centerX = centerX;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }
    }

    @Override
    public void setVisibility(int v) {
        if (getVisibility() != v) {
            super.setVisibility(v);
            if (v == GONE || v == INVISIBLE) {
                stopAnimator();
            } else {
                startAnimator();
            }
        }
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int v) {
        super.onVisibilityChanged(changedView, v);
        if (v == GONE || v == INVISIBLE) {
            stopAnimator();
        } else {
            startAnimator();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnimator();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimator();
    }

    /**
     * 开始动画
     */
    public void startAnimator() {
        if (getVisibility() != VISIBLE) return;

        if (animatorSet.isRunning()) return;

        if (animatorSet != null) {
            animatorSet.start();
        }
    }

    /**
     * 结束停止动画
     */
    public void stopAnimator() {
        if (animatorSet != null) {
            animatorSet.end();
        }
    }
}
