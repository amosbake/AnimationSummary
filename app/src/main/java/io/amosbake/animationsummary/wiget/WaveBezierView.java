package io.amosbake.animationsummary.wiget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import static java.lang.Math.PI;

/**
 * Author: mopel
 * Date : 2016/12/21
 */
public class WaveBezierView extends View implements View.OnClickListener {
    public static final double PARMS = (PI - 2) / (2 * PI);
    private float startPointX;
    private float startPointY;
    private float mOffset;

    private float waveLength = 100;
    private float waveHeight = 50;
    private Paint mPathPaint;
    private Paint mSecondPathPaint;
    private Path mPath;
    private ValueAnimator mAnimator;

    public WaveBezierView(Context context) {
        this(context, null);
    }

    public WaveBezierView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveBezierView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPathPaint.setStyle(Paint.Style.FILL);
        mPathPaint.setColor(Color.MAGENTA);
        mPathPaint.setStrokeWidth(4);
        mPath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        this.setOnClickListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        startPointX = 0 - waveLength + mOffset;
        startPointY = getHeight() / 2 + waveHeight;
        mPath.reset();
        mPath.moveTo(startPointX, startPointY);
        for (int i = 0; startPointX + waveLength * i < getWidth(); i++) {
            float flagpoint1X = (float) (startPointX + PARMS *waveLength + waveLength*i);
            float flagpoint1Y = startPointY ;
            float endpointX = startPointX + waveLength/2 + waveLength*i;
            float endpointY = startPointY  - waveHeight;
            float flagpoint2X = (float) (endpointX - PARMS *waveLength);
            float flagpoint2Y = endpointY ;

            mPath.cubicTo(flagpoint1X,flagpoint1Y,flagpoint2X,flagpoint2Y,endpointX,endpointY);

            float flagpoint3X = (float) (endpointX + PARMS *waveLength );
            float flagpoint3Y = endpointY ;
            float endpoint2X = startPointX + waveLength + waveLength*i;
            float endpoint2Y = startPointY ;
            float flagpoint4X = (float) (endpoint2X - PARMS *waveLength);
            float flagpoint4Y = endpoint2Y ;

            mPath.cubicTo(flagpoint3X,flagpoint3Y,flagpoint4X,flagpoint4Y,endpoint2X,endpoint2Y);
        }
        mPath.lineTo(getWidth(),getHeight());
        mPath.lineTo(0,getHeight());
        mPath.close();
        canvas.drawPath(mPath, mPathPaint);

    }


    @Override
    public void onClick(View v) {
        mAnimator = ValueAnimator.ofFloat(0,waveLength);
        mAnimator.setDuration(1000);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mOffset = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.start();
    }
}
