package io.amosbake.animationsummary.wiget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Author: mopel
 * Date : 16/12/24
 */
public class PathTracingView extends View implements View.OnClickListener {
  private Path path;
  private Path dstPath;
  private PathMeasure pathMeasure;
  private Paint paint;
  private float length;
  private float animValue;
  private ValueAnimator valueAnimator;

  public PathTracingView(Context context) {
    this(context, null);
  }

  public PathTracingView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public PathTracingView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    path = new Path();
    dstPath = new Path();
    paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    paint.setColor(Color.BLACK);
    paint.setStyle(Paint.Style.STROKE);
    paint.setStrokeWidth(5);
    setOnClickListener(this);
  }

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    path.reset();
    path.addCircle(w / 2, h / 2, (float) (0.3 * Math.min(w, h)), Path.Direction.CCW);
    pathMeasure = new PathMeasure(path, true);
    length = pathMeasure.getLength();
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    dstPath.reset();
    dstPath.lineTo(0, 0);
    float stop = length * animValue;
    float start = (float) (stop - ((0.5 - Math.abs(animValue - 0.5)) * length));
    pathMeasure.getSegment(start, stop, dstPath, true);
    canvas.drawPath(dstPath, paint);
  }

  @Override public void onClick(View view) {
    valueAnimator = ValueAnimator.ofFloat(0, 1);
    valueAnimator.setDuration(2_000);
    valueAnimator.setInterpolator(new LinearInterpolator());
    valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator valueAnimator) {
        animValue = (float) valueAnimator.getAnimatedValue();
        invalidate();
      }
    });
    valueAnimator.start();
  }
}
