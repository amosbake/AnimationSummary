package io.amosbake.animationsummary.wiget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Author: mopel
 * Date : 2016/12/21
 */
public class WaveBezierView extends View implements View.OnClickListener {
    private float startPointX;
    private float startPointY;


    private float waveLength = 50;
    private float waveHeight = 30;
    private Paint mPathPaint;
    private Path mPath;

    public WaveBezierView(Context context) {
        this(context, null);
    }

    public WaveBezierView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveBezierView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPathPaint.setStyle(Paint.Style.STROKE);
        mPathPaint.setColor(Color.MAGENTA);
        mPathPaint.setStrokeWidth(4);

        mPath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        startPointX = 0;
        startPointY = h / 2;
        this.setOnClickListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        mPath.moveTo(startPointX, startPointY);
        for (int i = 0;  startPointX+waveLength*(2*i+1)/4 < getWidth() ; i++) {
            if (i%2 == 0){
                mPath.quadTo(startPointX+waveLength*(2*i+1)/4,startPointY-waveHeight,startPointX+waveLength*(i+1)/2,startPointY);
            }else{
                mPath.quadTo(startPointX+waveLength*(2*i+1)/4,startPointY+waveHeight,startPointX+waveLength*(i+1)/2,startPointY);
            }

        }


        canvas.drawPath(mPath, mPathPaint);

    }


    @Override
    public void onClick(View v) {
    }
}
