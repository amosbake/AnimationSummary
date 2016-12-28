package io.amosbake.animationsummary.utils;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

/**
 * Author: mopel
 * Date : 2016/12/23
 */
public class CubicBezierEvaluator implements TypeEvaluator<PointF> {
    private PointF flagPoint1;
    private PointF flagPoint2;

    public CubicBezierEvaluator(PointF flagPoint1, PointF flagPoint2) {
        this.flagPoint1 = flagPoint1;
        this.flagPoint2 = flagPoint2;
    }

    @Override
    public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
        return BezierUtil.CalculateBezierPointForCubic(fraction,startValue,flagPoint1,flagPoint2,endValue);
    }
}
