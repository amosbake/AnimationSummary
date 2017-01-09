package io.amosbake.animationsummary.drawable;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

/**
 * Author: mopel
 * Date : 2017/1/7
 */
public class RoundImageDrawable extends Drawable {
    private Paint mPaint;
    private Bitmap mBitmap;
    private RectF mRectF;

    public RoundImageDrawable(Bitmap bitmap) {
        mBitmap = bitmap;
        BitmapShader _bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setFlags(Paint.FILTER_BITMAP_FLAG);
        mPaint.setShader(_bitmapShader);
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        mRectF = new RectF(left,top,right,bottom);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRoundRect(mRectF,30,30,mPaint);
    }


    @Override
    public int getIntrinsicWidth()
    {
        return mBitmap.getWidth();
    }

    @Override
    public int getIntrinsicHeight()
    {
        return mBitmap.getHeight();
    }

    @Override
    public void setAlpha(int alpha)
    {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf)
    {
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity()
    {
        return PixelFormat.TRANSLUCENT;
    }
}
