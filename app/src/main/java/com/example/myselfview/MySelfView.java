package com.example.myselfview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.Random;

/**
 * @创建者 mingyan.su
 * @创建时间 2019/7/30 12:18
 * @类描述 ${TODO}自定义控件
 */
public class MySelfView extends View {

    private int mColor;
    private int mTextSize;
    private String mText;
    private Paint mPaint;
    private Rect mRect;

    /**
     * 在java代码里new的时候会用到
     *
     * @param context 上下文
     */
    public MySelfView(Context context) {
//        super(context);
        this(context, null);
    }

    /**
     * 在xml布局文件中使用时自动调用
     *
     * @param context 上下文
     * @param attrs   属性集合
     */
    public MySelfView(Context context, AttributeSet attrs) {
//        super(context, attrs);
        this(context, attrs, 0);
    }

    /**
     * 不会自动调用，如果有默认style时，在第二个构造函数中调用
     *
     * @param context      上下文
     * @param attrs        属性集合
     * @param defStyleAttr
     */
    public MySelfView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //加载自定义属性集合
        // 第二个参数为res/values文件夹下attrs.xml文件中的<declare-styleable>标签
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MySelfView);

        //根据属性名称获取实际的属性值
        //第一个参数为属性集合的属性，命名规则：R.styleable.+属性集合名称+下划线+属性名称
        mText = typedArray.getString(R.styleable.MySelfView_titleText);
        // 默认颜色设置为红色
        mColor = typedArray.getColor(R.styleable.MySelfView_titleColor, Color.RED);
        // 默认设置为16sp，TypeValue也可以把sp转化为px
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.MySelfView_titleSize, (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
        //对象回收
        typedArray.recycle();

        init();
    }

    private void init() {
        //创建画笔
        mPaint = new Paint();
        //设置字体大小
        mPaint.setTextSize(mTextSize);
        //创建矩形
        mRect = new Rect();
        //将字体放入到矩形中
        mPaint.getTextBounds(mText, 0, mText.length(), mRect);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mText = ranDomText();
                postInvalidate();
            }
        });
    }

    private String ranDomText() {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 4; i++) {
            int anInt = random.nextInt(10);
            sb.append(anInt);
        }
        return sb.toString();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取宽高的测量模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        //获取宽高的值
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //设置宽度
        //如果布局里面设置的是固定值或者是match_parent则取测量模式里面的尺寸
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {//如果是其他(warp_content或者无限制)则重新计算
            mPaint.setTextSize(mTextSize);
            mPaint.getTextBounds(mText, 0, mText.length(), mRect);
            int textWidth = mRect.width();
            width = getPaddingLeft() + getPaddingRight() + textWidth;
        }

        //设置高度
        //如果布局里面设置的是固定值或者是match_parent则取测量模式里面的尺寸
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {//如果是其他(warp_content或者无限制)则重新计算
            mPaint.setTextSize(mTextSize);
            mPaint.getTextBounds(mText, 0, mText.length(), mRect);
            int textHeight = mRect.height();
            height = getPaddingTop() + getPaddingBottom() + textHeight;
        }

        //调用父类方法，将view的大小告诉父布局
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //设置画笔颜色
        mPaint.setColor(Color.RED);
        //在画布上绘制矩形
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);
        mPaint.setColor(mColor);
        //在画布上绘制文字
        canvas.drawText(mText, (float) getWidth() / 2 - (float) mRect.width() / 2, (float) getHeight() / 2 + (float) mRect.height() / 2, mPaint);
    }
}
