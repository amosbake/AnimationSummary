package io.amosbake.animationsummary.utils;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

/**
 * Author: mopel
 * Date : 2016/12/23
 */
public class QuadBezierEvaluator implements TypeEvaluator<PointF> {
    private PointF flagPoint;

    public QuadBezierEvaluator(PointF flagPoint) {
        this.flagPoint = flagPoint;
    }

    @Override
    public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
        return BezierUtil.CalculateBezierPointForQuadratic(fraction,startValue,flagPoint,endValue);
    }
}
