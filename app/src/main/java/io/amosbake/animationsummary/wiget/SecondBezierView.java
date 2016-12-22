package io.amosbake.animationsummary.wiget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Author: mopel
 * Date : 2016/12/21
 */
public class SecondBezierView extends View {
    private float startPointX;
    private float startPointY;
    private float endPointX;
    private float endPointY;
    private float flagPointX;
    private float flagPointY;
    private Paint mPathPaint;
    private Paint mPointPaint;
    private Paint mLinePaint;
    private Path mPath;
    private static final int POINT_RADIUS = 5;

    public SecondBezierView(Context context) {
        this(context, null);
    }

    public SecondBezierView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SecondBezierView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        mLinePaint.setColor(Color.argb(75,77,77,77));
        mLinePaint.setStrokeWidth(4);
        mPath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        startPointX = w / 4;
        startPointY = h * 3 / 4;

        endPointX = w * 3 / 4;
        endPointY = h * 3 / 4;

        flagPointX = w / 2;
        flagPointY = h / 4;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        mPath.moveTo(startPointX, startPointY);
        mPath.quadTo(flagPointX, flagPointY, endPointX, endPointY);
        canvas.drawPath(mPath, mPathPaint);
        canvas.drawCircle(startPointX, startPointY, POINT_RADIUS, mPointPaint);
        canvas.drawCircle(endPointX, endPointY, POINT_RADIUS, mPointPaint);
        canvas.drawCircle(flagPointX, flagPointY, POINT_RADIUS, mPointPaint);

        canvas.drawLine(startPointX,startPointY,flagPointX,flagPointY,mLinePaint);
        canvas.drawLine(endPointX,endPointY,flagPointX,flagPointY,mLinePaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:{
                flagPointX = event.getX(0);
                flagPointY = event.getY(0);
                postInvalidate();
                break;
            }
        }
        return true;
    }
}
