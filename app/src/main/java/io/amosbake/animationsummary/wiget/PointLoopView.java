package io.amosbake.animationsummary.wiget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * Author: mopel
 * Date : 2017/1/5
 */
public class PointLoopView extends View {
    private Paint mPaint;
    private int paintColor = Color.BLACK;
    private int mPointCount = 6, mPointRadius = 25, mTranslateX, mTranslateY, mWidth, mHeight, mCenterY;
    private float mFraction = 0, mPathLength;
    private ValueAnimator mAnimator;
    private long AnimatorDuration = 700;
    private boolean mThroughAbove = true;

    //catch path point coordinate
    private Path mPath;
    private PathMeasure mPathMeasure;
    private float[] mPos = new float[2];


    public PointLoopView(Context context) {
        this(context, null);
    }

    public PointLoopView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PointLoopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        configAnim();
    }

    private void init() {
        //初始化画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(paintColor);
        mPaint.setStyle(Paint.Style.FILL);
        // 小球运动轨迹
        mPath = new Path();
        mPathMeasure = new PathMeasure();
    }

    private void configAnim() {
        //设置动画
        mAnimator = ValueAnimator.ofFloat(0, 1);
        mAnimator.setDuration(AnimatorDuration);
        mAnimator.setInterpolator(new DecelerateInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mFraction = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        //每次运动轨迹不相同
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
                mThroughAbove = !mThroughAbove;
            }
        });
        mAnimator.setRepeatCount(Integer.MAX_VALUE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(widthMeasureSpec);
        mWidth = (3 * mPointCount + 1) * mPointRadius;
        mHeight = mPointRadius * 2 * 10;
        if (widthMode != MeasureSpec.AT_MOST && heightMode != MeasureSpec.AT_MOST) {
            width = Math.max(mWidth, width);
            height = Math.max(mHeight, height);
        } else if (widthMode != MeasureSpec.AT_MOST) {
            width = Math.max(mWidth, width);
        } else if (heightMode != MeasureSpec.AT_MOST) {
            height = Math.max(mHeight, height);
        }
        mTranslateX = width / 2 - mWidth / 2;
        mTranslateY = height / 2 - mHeight / 2;
        mCenterY = mHeight / 2;
        mPath.moveTo(mPointRadius * 2, mCenterY);
        mPath.cubicTo(mPointRadius * 2, mCenterY, mWidth / 2, 0, mWidth - mPointRadius * 2, mCenterY);
        mPathMeasure.setPath(mPath, false);
        mPathLength = mPathMeasure.getLength();
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mTranslateX, mTranslateY);
        for (int i = 0; i < mPointCount; i++) {
            if (i == 0) {
                mPathMeasure.getPosTan(mPathLength*mFraction,mPos,null);
                canvas.drawCircle(mPos[0],mThroughAbove? mPos[1]:mHeight-mPos[1],mPointRadius,mPaint);
            } else {
                canvas.drawCircle((2 + 3 * i) * mPointRadius - mFraction * mPointRadius * 3, mCenterY, mPointRadius, mPaint);
            }
        }
    }

    public void startLoading(){
        if (!mAnimator.isRunning()){
            mAnimator.start();
        }
    }

    public void stopLoading() {
        if (mAnimator.isRunning())
            mAnimator.cancel();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startLoading();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopLoading();
    }
}
