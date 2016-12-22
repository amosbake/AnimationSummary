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