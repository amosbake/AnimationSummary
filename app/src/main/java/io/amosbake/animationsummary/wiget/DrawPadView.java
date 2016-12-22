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
 * Date : 2016/12/22
 */
public class DrawPadView extends View {
    private float startPointX;
    private float startPointY;
    private Paint mPathPaint;
    private Path mPath;
    private static final int POINT_RADIUS = 5;

    public DrawPadView(Context context) {
        this(context, null);
    }

    public DrawPadView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawPadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPathPaint.setStyle(Paint.Style.STROKE);
        mPathPaint.setColor(Color.MAGENTA);
        mPathPaint.setStrokeWidth(4);
        mPathPaint.setStrokeCap(Paint.Cap.ROUND);
        mPathPaint.setStrokeJoin(Paint.Join.ROUND);
        mPath = new Path();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath,mPathPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                startPointX = event.getX();
                startPointY = event.getY();
                mPath.reset();
                mPath.moveTo(startPointX,startPointY);
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                float _x = event.getX();
                float _y = event.getY();
                float cx = (_x+startPointX)/2;
                float cy = (_y+startPointY)/2;
                mPath.quadTo(_x,_y,cx,cy);
                startPointX = _x;
                startPointY = _y;
                getParent().requestDisallowInterceptTouchEvent(true);
                postInvalidate();
                break;
            }
            case MotionEvent.ACTION_UP:{
                getParent().requestDisallowInterceptTouchEvent(false);
            }
        }
        return true;
    }
}
