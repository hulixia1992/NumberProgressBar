package com.example.drum.numberprogressbar.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.example.drum.numberprogressbar.R;


/**
 * Created by hulixia on 2016/8/19.
 * 数字加载进度条
 */
public class NumberPorgressBar extends View {
    private int reachedAreaColor;//已加载完成的进度颜色
    private int unreachedAreaColor;//没有加载到的颜色
    private int numColor;
    private int progressNum;//当前进度数据
    private int numSize;
    private int mWidth;//进度条的长度
    private int mHeight;
    private int numHeigth = 10;//进度条的高度
    private int segmentWidth;//分成100段。每一段的长度
    private int textPadding = 5;
    private static int onePrecentTime = 100;
    private Rect textRect;
    private int textLeftPadding;

    private int w;//整个view的高度

    private Paint reachedPaint;
    private Paint unreachedPaint;
    private Paint textPaint;

    public NumberPorgressBar(Context context) {
        super(context);
    }

    public NumberPorgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttrs(context, attrs);
        init();
    }

    public NumberPorgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


    }

    int itemWidth;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        String progressText = "00%";

        this.w = w;
        textRect = new Rect();
        textPaint.getTextBounds(progressText, 0, progressText.length(), textRect);
        itemWidth = (textRect.right - textRect.left) / 3;
        mWidth = w - (textRect.right - textRect.left);
        segmentWidth = mWidth / 100;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(0, mHeight / 2);
        String progressText = progressNum + "%";

        int middleWidth = progressNum * segmentWidth;
      //  Log.i(Constants.TAG, "onDraw: middleWidth=" + middleWidth);
        canvas.drawLine(0, 0, middleWidth, 0, reachedPaint);
        canvas.drawText(progressText, middleWidth, (textRect.bottom - textRect.top) / 2, textPaint);
        if ((middleWidth + (textRect.right - textRect.left)) < mWidth && progressNum != 100)
            canvas.drawLine(middleWidth + (textRect.right - textRect.left), 0, mWidth+itemWidth, 0, unreachedPaint);
//        if (progressNum == 100)
//            Log.i(Constants.TAG, "onDraw: 前后两个长度:" + (middleWidth + (textRect.right - textRect.left) + "" + (mWidth)));
    }

    /**
     * 得到属性值
     *
     * @param context
     * @param attrs
     */
    private void getAttrs(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.NumberPorgressBar);
        reachedAreaColor = array.getColor(R.styleable.NumberPorgressBar_reached_color, Color.BLACK);
        unreachedAreaColor = array.getColor(R.styleable.NumberPorgressBar_unreached_color, Color.parseColor("#999999"));
        progressNum = array.getInteger(R.styleable.NumberPorgressBar_progress_num, 0);
        numSize = (int) array.getDimension(R.styleable.NumberPorgressBar_num_size, 56);
        numColor = array.getColor(R.styleable.NumberPorgressBar_num_color, Color.RED);
    }

    private void init() {
        // progressNum = 20;
        reachedPaint = new Paint();
        unreachedPaint = new Paint();
        textPaint = new Paint();
        reachedPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        reachedPaint.setColor(reachedAreaColor);
        unreachedPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        unreachedPaint.setColor(unreachedAreaColor);
        textPaint.setColor(numColor);
        textPaint.setTextSize(numSize);
        reachedPaint.setStrokeWidth(numHeigth);
        unreachedPaint.setStrokeWidth(numHeigth);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                valueAnimation();
                break;
        }
        return true;
    }

    /**
     * @param progressNum
     */
    public void setProgressNum(int progressNum) {
        valueAnimation(progressNum);
    }

    private void valueAnimation() {
        valueAnimation(100);
    }

    private void valueAnimation(int currentProgressNum) {
        ValueAnimator animator = new ValueAnimator().ofInt(progressNum, currentProgressNum)
                .setDuration((100 - progressNum) * onePrecentTime);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(0);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                progressNum = (int) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();
    }
}
