package io.amosbake.animationsummary.wiget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.BounceInterpolator;

/**
 * Author: mopel
 * Date : 2016/12/21
 */
public class PathMorphView extends View implements View.OnClickListener{
    private float startPointX;
    private float startPointY;
    private float endPointX;
    private float endPointY;
    private float flag1PointX;
    private float flag1PointY;
    private float flag2PointX;
    private float flag2PointY;
    private Paint mPathPaint;
    private Paint mPointPaint;
    private Paint mLinePaint;
    private Path mPath;
    private static final int POINT_RADIUS = 5;
    private ValueAnimator mValueAnimator;

    public PathMorphView(Context context) {
        this(context, null);
    }

    public PathMorphView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PathMorphView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPathPaint.setStyle(Paint.Style.STROKE);
        mPathPaint.setColor(Color.MAGENTA);
        mPathPaint.setStrokeWidth(4);

        mPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointPaint.setStyle(Paint.Style.FILL);
        mPointPaint.setColor(Color.RED);
        mPointPaint.setStrokeWidth(3);

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setColor(Color.argb(75, 77, 77, 77));
        mLinePaint.setStrokeWidth(4);
        mPath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        startPointX = w / 5;
        startPointY = h / 4;

        endPointX = w * 4 / 5;
        endPointY = h  / 4;

        flag1PointX =startPointX;
        flag1PointY = startPointY;

        flag2PointX = endPointX;
        flag2PointY = endPointY;
        mValueAnimator = ValueAnimator.ofFloat(startPointY,h*8/9);
        mValueAnimator.setInterpolator(new BounceInterpolator());
        mValueAnimator.setDuration(1000);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                flag1PointY = (float) animation.getAnimatedValue();
                flag2PointY = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        this.setOnClickListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        mPath.moveTo(startPointX, startPointY);
        mPath.cubicTo(flag1PointX, flag1PointY, flag2PointX, flag2PointY, endPointX, endPointY);
        canvas.drawPath(mPath, mPathPaint);
        canvas.drawCircle(startPointX, startPointY, POINT_RADIUS, mPointPaint);
        canvas.drawCircle(endPointX, endPointY, POINT_RADIUS, mPointPaint);
        canvas.drawCircle(flag1PointX, flag1PointY, POINT_RADIUS, mPointPaint);
        canvas.drawCircle(flag2PointX, flag2PointY, POINT_RADIUS, mPointPaint);

        canvas.drawLine(startPointX, startPointY, flag1PointX, flag1PointY, mLinePaint);
        canvas.drawLine(endPointX, endPointY, flag2PointX, flag2PointY, mLinePaint);
        canvas.drawLine(flag1PointX, flag1PointY, flag2PointX, flag2PointY, mLinePaint);
    }


    @Override
    public void onClick(View v) {
        mValueAnimator.start();
    }
}
