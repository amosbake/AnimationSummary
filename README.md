# Android 动画总结
## VectorDrawable 
### 基本概念
`VectorDrawable` 实现了SVG语法的Path标签

- M = moveto(M X,Y) 移动光标到指定位置
- L = lineto(L X,Y) 画直线到指定位置
- H = horizontal lineto 水平直线
- V = vertical lineto 垂直直线
- C = curveto 
- S = smooth curveto
- Q = quadratic Belzier curve
- T = smooth quadratic Belzier curveto
- A = elliptical Arc
- Z = closepath() 关闭路径

### Vector工具
[在线svg转vector](http://inloop.github.io/svg2android/)

AndroidStudio自带 VectorAssets

### 项目引用VectorDrawable方法
目前`VectorDrawable`已经可以兼容3.0以上版本

只用在项目的`gradle.build`文件中添加以下代码即可
(`build:gradle` 版本大于1.5 且`support:appcompat` 版本高于23.2)

```gradle
 defaultConfig {
        ...
        vectorDrawables.useSupportLibrary = true
    }
```

在`imageView/ImageButton`中使用`app:srcCompat`来引用`VectorDrawable`
```xml
    <ImageView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:srcCompat ="@drawable/bg_btn_progress"/>
```

`Button` 不能直接引用 `VectorDrawable` 但可以通过引用`Selector`的方式来使用`VectorDrawable`,
而且必须在所处的`Actitivity`中添加以下代码
```java
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
```

## 动态VectorDrawable


### 简介
animated-vector 是链接 vector 与 animator 的 胶水
animator 定义属性变化的动画
vector 定义矢量图
animated-vector 定义矢量图中的那些部分和属性运行怎样的动画


### 兼容性
1. 路径变换动画(`pathData`)  **`pre-L`版本**以下无法使用 **`L`版本以上**需要使用代码配置(xml中需要用src属性引入动画文件)
2. 路径插值器  `pre-L`版本下只能使用系统的插值器
3. 不支持从 `strings.xml` 中读取`<PathData/>`


### vector的属性标签
- pathData 绘制路径 可以动画变形
- fillColor 填充色 可以填充色渐变
- strokeColor 路径色 可以路径色渐变
- strokeWidth 路径宽度 可动画
- strokeAlpha 路径透明度 可动画
- fillAlpha 填充色透明度 可动画
- trimPathStart 从路径开始截取的不展示百分比(0~1.0) 产生从结束点的展开,收回动画
- trimPathEnd 从路径结束截取的不展示百分比(0~1.0) 产生从开始点的展开,收回动画
- trimPathOffset 不明含义
- strokeLineCap 路径断点的形态  不可动画
- strokeLineJoin 不可动画
- strokeMiterLimit 不可动画
- fillType 不可动画

### group的属性标签
- rotation 旋转角度  产生旋转动画
- pivotX 旋转角度中心x坐标  配合产生旋转动画,也可动画偏移
- pivotY 旋转角度中心y坐标  配合产生旋转动画,也可动画偏移
- scaleX x坐标轴方向拉伸 产生缩放动画
- scaleY y坐标轴方向拉伸 产生缩放动画
- translateX x坐标轴方向位移
- translateY y坐标轴方向位移


### 使用场景
1. 小图标应用上可以取代png 减少apk大小 渲染效率较png低 
2. 不能做频繁的重绘,在Gpu中没有缓存
3. vector加载速度快于png 渲染速度慢于png.对于图像过于复杂的情况,应使用png

## 贝塞尔曲线
贝塞尔曲线是计算机图形学中相当重要的参数曲线,它的特点是仅用少量的控制点就可以绘制出复杂的连续平滑曲线.
在Android中经常用到的是二阶贝塞尔曲线和三阶贝塞尔曲线.

二次方贝塞尔曲线的路径由给定点P0、P1、P2的函数B（t）追踪
![二次方贝塞尔曲线](http://upload.wikimedia.org/math/0/5/c/05c4210c69ffb1358ceb8eb83a1a06fe.png?_=3035541)

其中p1为控制点

![绘制图形](http://upload.wikimedia.org/wikipedia/commons/thumb/2/2d/Bezier_2_big.gif/240px-Bezier_2_big.gif?_=3035541)

对应的Android绘制代码为:
```java
    mPath.reset();
    mPath.moveTo(startPointX, startPointY);
    mPath.quadTo(flagPointX, flagPointY, endPointX, endPointY);
    canvas.drawPath(mPath, mPathPaint);
```

三次方贝塞尔曲线较二阶的多一个控制点,曲线在感官上更平滑.

![二次方贝塞尔曲线](http://upload.wikimedia.org/math/5/9/7/597ecc5022fa7ab65509d5edfa9c148c.png?_=3035541)

其中p1,p2为控制点

![绘制动画](http://upload.wikimedia.org/wikipedia/commons/thumb/f/ff/Bezier_3_big.gif/240px-Bezier_3_big.gif?_=3035541)

对应的Android绘制代码为:
```java
    mPath.reset();
    mPath.moveTo(startPointX, startPointY);
    mPath.cubicTo(flag1PointX, flag1PointY, flag2PointX, flag2PointY, endPointX, endPointY);
    canvas.drawPath(mPath, mPathPaint);
```

### 贝塞尔曲线插值器
在实际应用中,可能遇到追踪贝塞尔曲线的坐标变化轨迹.根据贝塞尔曲线的方程可以很方便的做到这一点.
同时也可以依据其特性,应用在属性动画插值器上
```java
public class BezierUtil {

    /**
     * B(t) = (1 - t)^2 * P0 + 2t * (1 - t) * P1 + t^2 * P2, t ∈ [0,1]
     *
     * @param t  曲线长度比例
     * @param p0 起始点
     * @param p1 控制点
     * @param p2 终止点
     * @return t对应的点
     */
    public static PointF CalculateBezierPointForQuadratic(float t, PointF p0, PointF p1, PointF p2) {
        PointF point = new PointF();
        float temp = 1 - t;
        point.x = temp * temp * p0.x + 2 * t * temp * p1.x + t * t * p2.x;
        point.y = temp * temp * p0.y + 2 * t * temp * p1.y + t * t * p2.y;
        return point;
    }

    /**
     * B(t) = P0 * (1-t)^3 + 3 * P1 * t * (1-t)^2 + 3 * P2 * t^2 * (1-t) + P3 * t^3, t ∈ [0,1]
     *
     * @param t  曲线长度比例
     * @param p0 起始点
     * @param p1 控制点1
     * @param p2 控制点2
     * @param p3 终止点
     * @return t对应的点
     */
    public static PointF CalculateBezierPointForCubic(float t, PointF p0, PointF p1, PointF p2, PointF p3) {
        PointF point = new PointF();
        float temp = 1 - t;
        point.x = p0.x * temp * temp * temp + 3 * p1.x * t * temp * temp + 3 * p2.x * t * t * temp + p3.x * t * t * t;
        point.y = p0.y * temp * temp * temp + 3 * p1.y * t * temp * temp + 3 * p2.y * t * t * temp + p3.y * t * t * t;
        return point;
    }
}


public class BezierEvaluator implements TypeEvaluator<PointF> {
    private PointF flagPoint;

    public BezierEvaluator(PointF flagPoint) {
        this.flagPoint = flagPoint;
    }

    @Override
    public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
        return BezierUtil.CalculateBezierPointForQuadratic(fraction,startValue,flagPoint,endValue);
    }
}

```

### 贝塞尔曲线模拟水波纹
由于贝塞尔曲线的特性,可以很好地拟合三角函数.
一般的,对于一个标准正弦函数(~AD),可以选择BC点作为控制点,其中`H = π-2`

![sin](http://p1.bpimg.com/503341/a90096d1668389be.png)

那么对于一个波长为L的正弦波来说,控制点偏移量`H = L*(π-2)/2π`,那么通过不断改变起始点的位置,在视觉效果上就能得到水波纹不断行进的效果了.
```java
    public static final double PARMS = (PI - 2) / (2 * PI);
   @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        startPointX = 0 - waveLength + mOffset;
        startPointY = getHeight() / 2 + waveHeight;
        mPath.reset();
        mPath.moveTo(startPointX, startPointY);
        for (int i = 0; startPointX + waveLength * i < getWidth(); i++) {
            float flagpoint1X = (float) (startPointX + PARMS *waveLength + waveLength*i);
            float flagpoint1Y = startPointY ;
            float endpointX = startPointX + waveLength/2 + waveLength*i;
            float endpointY = startPointY  - waveHeight;
            float flagpoint2X = (float) (endpointX - PARMS *waveLength);
            float flagpoint2Y = endpointY ;

            mPath.cubicTo(flagpoint1X,flagpoint1Y,flagpoint2X,flagpoint2Y,endpointX,endpointY);

            float flagpoint3X = (float) (endpointX + PARMS *waveLength );
            float flagpoint3Y = endpointY ;
            float endpoint2X = startPointX + waveLength + waveLength*i;
            float endpoint2Y = startPointY ;
            float flagpoint4X = (float) (endpoint2X - PARMS *waveLength);
            float flagpoint4Y = endpoint2Y ;

            mPath.cubicTo(flagpoint3X,flagpoint3Y,flagpoint4X,flagpoint4Y,endpoint2X,endpoint2Y);
        }
        mPath.lineTo(getWidth(),getHeight());
        mPath.lineTo(0,getHeight());
        mPath.close();
        canvas.drawPath(mPath, mPathPaint);

    }

     @Override
    public void onClick(View v) {
        mAnimator = ValueAnimator.ofFloat(0,waveLength);
        mAnimator.setDuration(1000);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mOffset = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.start();
    }
```

