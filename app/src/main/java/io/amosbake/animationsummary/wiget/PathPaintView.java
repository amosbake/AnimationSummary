package io.amosbake.animationsummary.wiget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Author: mopel
 * Date : 16/12/24
 */
public class PathPaintView extends View implements View.OnClickListener {
  private Path path;
  private PathMeasure pathMeasure;
  private PathEffect pathEffect;
  private Paint paint;
  private float length;
  private float animValue;
  private ValueAnimator valueAnimator;

  public PathPaintView(Context context) {
    this(context, null);
  }

  public PathPaintView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public PathPaintView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    path = new Path();
    paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    paint.setColor(Color.BLACK);
    paint.setStyle(Paint.Style.STROKE);
    paint.setStrokeWidth(5);
    setOnClickListener(this);
  }

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    path.reset();
    path.moveTo(w / 4, h * 3 / 4);
    path.lineTo(w * 3 / 4, h * 3 / 4);
    path.lineTo(w / 2, h / 4);
    path.close();
    pathMeasure = new PathMeasure(path, true);
    length = pathMeasure.getLength();
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    canvas.drawPath(path, paint);
  }

  @Override public void onClick(View view) {
    valueAnimator = ValueAnimator.ofFloat(1, 0);
    valueAnimator.setDuration(2_000);
    valueAnimator.setInterpolator(new LinearInterpolator());
    valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator valueAnimator) {
        animValue = (float) valueAnimator.getAnimatedValue();
        pathEffect = new DashPathEffect(new float[] { length, length }, animValue * length);
        paint.setPathEffect(pathEffect);
        invalidate();
      }
    });
    valueAnimator.start();
  }
}
