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
public class ThirdBezierView extends View {
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
    private boolean isSecondPoint = false;
    private static final int POINT_RADIUS = 5;

    public ThirdBezierView(Context context) {
        this(context, null);
    }

    public ThirdBezierView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ThirdBezierView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        startPointY = h * 3 / 4;

        endPointX = w * 4 / 5;
        endPointY = h * 3 / 4;

        flag1PointX = w / 2;
        flag1PointY = h / 4;

        flag2PointX = w * 3 / 4;
        flag2PointY = h / 4;
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
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_POINTER_DOWN:{
                isSecondPoint = true;
                break;
            }
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE: {
                if (isSecondPoint){
                    flag1PointX = event.getX(0);
                    flag1PointY =  event.getY(0);
                    flag2PointX = event.getX(1);
                    flag2PointY =  event.getY(1);
                }else {
                    float _x = event.getX(0);
                    float _y = event.getY(0);
                    if (_x < getWidth() / 2) {
                        flag1PointX = _x;
                        flag1PointY = _y;
                    } else {
                        flag2PointX = _x;
                        flag2PointY = _y;
                    }
                }


                postInvalidate();
                break;
            }
        }
        return true;
    }
}
