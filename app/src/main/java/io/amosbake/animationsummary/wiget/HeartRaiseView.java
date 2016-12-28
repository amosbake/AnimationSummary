package io.amosbake.animationsummary.wiget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Random;

import io.amosbake.animationsummary.R;
import io.amosbake.animationsummary.utils.CubicBezierEvaluator;

/**
 * Author: mopel
 * Date : 2016/12/26
 */
public class HeartRaiseView extends RelativeLayout {
    private Interpolator line = new LinearInterpolator();
    private Interpolator acc = new AccelerateInterpolator();
    private Interpolator dce = new DecelerateInterpolator();
    private Interpolator accdce = new AccelerateDecelerateInterpolator();
    private Interpolator[] mInterpolators;
    private int mHeight;
    private int mWidth;
    private LayoutParams lp;
    private Drawable[] drawables;
    private Random random = new Random();
    private int dHeight;
    private int dWidth;

    public HeartRaiseView(Context context) {
        this(context, null);
    }

    public HeartRaiseView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeartRaiseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        drawables = new Drawable[]{ContextCompat.getDrawable(context, R.drawable.vector_heart_blue),
                ContextCompat.getDrawable(context, R.drawable.vector_heart_red),
                ContextCompat.getDrawable(context, R.drawable.vector_heart_yellow)};
        dHeight = drawables[0].getIntrinsicHeight() / 4;
        dWidth = drawables[0].getIntrinsicWidth() / 4;
        lp = new RelativeLayout.LayoutParams(dWidth, dHeight);
        lp.addRule(CENTER_HORIZONTAL, TRUE);
        lp.addRule(ALIGN_PARENT_BOTTOM, TRUE);

        // 初始化插补器
        mInterpolators = new Interpolator[4];
        mInterpolators[0] = line;
        mInterpolators[1] = acc;
        mInterpolators[2] = dce;
        mInterpolators[3] = accdce;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    public void addHeart() {
        ImageView imageView = new ImageView(getContext());
        // 随机选一个图像
        imageView.setImageDrawable(drawables[random.nextInt(3)]);
        imageView.setLayoutParams(lp);
        addView(imageView);
        // 执行随机动画
        Animator set = getAnimator(imageView);
        set.addListener(new AnimEndListener(imageView));
        set.start();
    }


    /**
     * 动画生成器，包含两个部分，一个是进入的动画，另一个是移动的动画
     *
     * @param target
     * @return
     */
    private Animator getAnimator(View target) {
        // 进入动画
        AnimatorSet set = getEnterAnimtor(target);
        // 移动动画
        ValueAnimator bezierValueAnimator = getBezierAnimator(target);
        AnimatorSet finalSet = new AnimatorSet();
        finalSet.playSequentially(set);
        finalSet.playSequentially(set, bezierValueAnimator);
        finalSet.setInterpolator(mInterpolators[random.nextInt(4)]);
        finalSet.setTarget(target);
        return finalSet;
    }

    private AnimatorSet getEnterAnimtor(final View target) {
        // 进入动画就是一个透明的+放大的动画
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, View.ALPHA, 0.2f, 1f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, View.SCALE_X, 0.2f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, View.SCALE_Y, 0.2f, 1f);
        AnimatorSet enter = new AnimatorSet();
        enter.setDuration(500);
        enter.setInterpolator(new LinearInterpolator());
        enter.playTogether(alpha, scaleX, scaleY);
        enter.setTarget(target);
        return enter;
    }

    private ValueAnimator getBezierAnimator(View target) {
        CubicBezierEvaluator _cubicBezierEvaluator = new CubicBezierEvaluator(getPointF(2), getPointF(1));
        ValueAnimator _animator = ValueAnimator.ofObject(_cubicBezierEvaluator, new PointF((mWidth - dWidth) / 2, mHeight - dHeight), new PointF(random.nextInt(getWidth()), 0));
        _animator.addUpdateListener(new BezierListener(target));
        _animator.setTarget(target);
        _animator.setDuration(3_000);
        return _animator;
    }

    /**
     * 获取中间的两个点
     *
     * @param scale
     */
    private PointF getPointF(int scale) {
        PointF pointF = new PointF();
        // 减去100 是为了控制 x轴活动范围,看效果 随意
        pointF.x = random.nextInt((mWidth - 100));
        // 在Y轴上 为了确保第二个点 在第一个点之上,我把Y分成了上下两半 这样动画效果好一些  也可以用其他方法
        pointF.y = random.nextInt((mHeight - 100)) / scale;
        return pointF;
    }


    private class BezierListener implements ValueAnimator.AnimatorUpdateListener {

        private View target;

        public BezierListener(View target) {
            this.target = target;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            // 这里获取到贝塞尔曲线计算出来的的x y值 赋值给view 这样就能让爱心随着曲线走啦
            PointF pointF = (PointF) animation.getAnimatedValue();
            target.setX(pointF.x);
            target.setY(pointF.y);
            // 这里顺便做一个alpha动画
            target.setAlpha(1 - animation.getAnimatedFraction());
        }
    }


    private class AnimEndListener extends AnimatorListenerAdapter {
        private View target;

        public AnimEndListener(View target) {
            this.target = target;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            // 因为不停的add 导致子view数量只增不减,所以在view动画结束后remove掉
            removeView((target));
        }
    }
}
