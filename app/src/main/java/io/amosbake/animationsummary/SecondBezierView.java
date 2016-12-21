package io.amosbake.animationsummary;

import android.content.Context;
import android.util.AttributeSet;
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

    public SecondBezierView(Context context) {
        this(context, null);
    }

    public SecondBezierView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SecondBezierView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        startPointX = w / 4;
        startPointY = h / 2 - 200;

        endPointX = w * 3 / 4;
        endPointY = h / 2 - 200;

        flagPointX = w / 2;
        flagPointX = h / 2 - 300;
    }
}
