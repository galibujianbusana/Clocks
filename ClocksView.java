package com.guoxw.clocks;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

/***
 *  1. 该view 的寛高必须一样，便于设置绘制圆形表盘
 *
 *  2.
 */

public class ClocksView extends View {


    /*** view 高 */
    private float viewH = 200;

    /*** view 宽度 */
    private float viewW = 200;

    /**** view 圆环描边宽度 */
    private float PaintStrokeWidth = 3;
    /*** 秒针  描宽*/
    private float SecondStrokeWidth = 3;
    /*** 分针描宽*/
    private float MstrokeWidth = 5;
    /*** 时针 描宽*/
    private float ClockwiseWidth = 7;

    /*** 表盘刻度 描宽*/
    private float clockWidth = 1;

    /*** 钟表圆环颜色 */
    private int clockRoundColor = Color.BLACK;

    /*** 钟表 秒针颜色 */
    private int clockSecondHandColor = Color.RED;

    /*** 钟表时针颜色*/
    private int clockClockwiseColor = Color.BLACK;

    /*** 钟表分针颜色*/
    private int clockMinuteHandColor = Color.BLUE;

    /*** 画笔 */
    private Paint paint;

    /*** textSize */
    private int textSize = 16;

    /*** 中心点的半径*/
    private int centerRadius = 10;


    /*** 小刻度的总数*/
    private int count = 60;

    /***  整点 小刻度的长度*/
    private int KeduLength = 8;

    /*** 非整点 小刻度长度 */
    private int NOKeduLength = 6;

    /*** 时间数值的大小*/
    private int timeCodeSize = 28;

    private  boolean isFrist=true;

    /*** 圆点坐标点*/
    private int centerX, centerY;


    public ClocksView(Context context) {
        super(context);
    }

    public ClocksView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ClocksView);
        viewH = array.getDimension(R.styleable.ClocksView_viewHeight, 0);
        viewW = array.getDimension(R.styleable.ClocksView_viewWidth, 0);
        clockClockwiseColor = array.getColor(R.styleable.ClocksView_clockClockwiseColor, clockClockwiseColor);
        clockMinuteHandColor = array.getColor(R.styleable.ClocksView_clockMinuteHandColor, clockMinuteHandColor);
        clockSecondHandColor = array.getColor(R.styleable.ClocksView_clockSecondHandColor, clockSecondHandColor);
        array.recycle();
        timer = new Timer();
        timer.schedule(timerTask, 1000, 1000);
    }

    public ClocksView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ClocksView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("Tag:", "onDraw1: " + getLeft() + "--" + getTop());
        centerX = (int) (getLeft() + viewW / 2);
        centerY = (int) (getTop() + viewH / 2);
        if(isFrist){
            endSecondX = centerX;
            endSecondY = centerY;
            endMX = centerX;
            endMY = centerY;
            endWiseX = centerX;
            endWiseY = centerY;
            isFrist=false;
        }

        Log.d("Tag:", "onDraw2: " + centerX + "--" + centerY);
        paint = new Paint();
        drawDialPlate(canvas, centerX, centerY, (int) viewH / 2);
    }

    /**** 时，分 秒 针的初始顶点位置 全部初始为center 点*/
    private float endSecondX , endSecondY, endMX , endMY, endWiseX, endWiseY;

    /***示例中是传参是200,200，表示的表盘的中心点*/
    private void drawDialPlate(Canvas canvas, int centerX, int centerY, int radius) {
        canvas.save();
        /*** 初始化 paint */

        paint.setColor(clockRoundColor);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(PaintStrokeWidth);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(centerX, centerY, radius, paint);
        canvas.save();
        /**** drawtextOnPath 绘制文字 */
        paint.setTextSize(textSize);
        /*** 绘制中心点圆心*/
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(centerX, centerY, centerRadius, paint);
        canvas.save();
        /*** 绘制红色线段，充当 秒针 */
        paint.setColor(clockSecondHandColor);
        paint.setStrokeWidth(SecondStrokeWidth);
        canvas.drawLine(centerX, centerY, endSecondX, endSecondY, paint);
        canvas.save();
        /*** 绘制蓝颜线段，充当分针 */
        paint.setColor(clockMinuteHandColor);
        paint.setStrokeWidth(MstrokeWidth);
        canvas.drawLine(centerX, centerY, endMX, endMY, paint);
        canvas.save();
        /*** 绘制黑色线段，充当时针 */
        paint.setColor(clockClockwiseColor);
        paint.setStrokeWidth(ClockwiseWidth);
        canvas.drawLine(centerX, centerY, endWiseX, endWiseY, paint);
        canvas.save();

        /*** 画表盘刻度  ****/
        Paint timePaint = new Paint(paint);
        timePaint.setStrokeWidth(clockWidth);
        timePaint.setColor(Color.BLACK);
        timePaint.setTextSize(timeCodeSize);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(PaintStrokeWidth);
        for (int i = 0; i <= count; i++) {
            if (i % 5 == 0) {
                /*** 整点刻度 */
                canvas.drawLine(centerX, getTop(), centerY, getTop() + KeduLength, paint);
                if (i != 0) {
                    canvas.drawText((String.valueOf(i / 5)), centerX - timeCodeSize / 2, getTop() + 2 + timeCodeSize, timePaint);
                }
            } else {
                /*** 小刻度 */
                canvas.drawLine(centerX, getTop(), centerY, getTop() + NOKeduLength, timePaint);
            }
            /*** 旋转坐标系 */
            canvas.rotate(360 / count, centerX, centerY);
        }


    }


    /*** 秒针的长度*/
    private int RadiuSecond = 140;
    /*** 时针的长度*/
    private int RadiusWise = 90;
    /*** 分针的长度*/
    private int RadiusM = 120;

    private int Num = 0;
    /*** 设置时针初始角度*/
    private double wiseAlgle = 0;
    private double wiseNum = 0;

    /*** 设置分针初始角度*/
    private double mAngle = 0;
    private double mNum = 0;

    /*** 设置秒针初始角度*/
    private double secondAngle = 0;
    private double secondNum = 0;


    private Timer timer;
    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            if(Num==60*60*12){
                Num=0;
            }
            Num++;
            secondNum = Num % 60;
            double secondNow=180 - (6 * secondNum+secondAngle);
            endSecondX = RadiuSecond * (float) Math.sin(changeAngle(secondNow)) + centerX;
            endSecondY = RadiuSecond * (float) Math.cos(changeAngle(secondNow)) + centerY;

            mNum = Num % (3600);
            double mNow=180 - (360 * mNum / 3600+mAngle);
            endMX = RadiusM * (float) Math.sin(changeAngle(mNow)) + centerX;
            endMY = RadiusM * (float) Math.cos(changeAngle(mNow)) + centerY;

            wiseNum = Num % (3600 * 12);
            double wiseNow=180 - (360 * wiseNum / 3600 / 12+wiseAlgle);
            endWiseX = RadiusWise * (float) Math.sin(changeAngle(wiseNow)) + centerX;
            endWiseY = RadiusWise * (float) Math.cos(changeAngle(wiseNow)) + centerY;
            postInvalidate();
            Log.d("Tag:", "run: ");

        }
    };


    /*** 角度转弧度*/
    private double changeAngle(double angle) {
        return angle * Math.PI / 180;
    }


    /***
     *
     * @param wiseAlgle 设置当前显示时间的 小时  (0-12 )
     * @param mAngle   设置当前显示时间的  分钟  (0-60 )
     * @param secondAngle 设置当前显示时间的 秒数 (0-60 )
     */
    public void setNowTime(int wiseAlgle,int mAngle,int secondAngle){
        this.secondAngle=secondAngle*6;
        this.mAngle=mAngle*360/60+secondAngle/10;
        this.wiseAlgle=wiseAlgle*360/12+mAngle*360/12/60+secondAngle*360/12/60/60;
    }


    public void setClockwiseWidth(float clockwiseWidth) {
        ClockwiseWidth = clockwiseWidth;
    }

    public void setClockWidth(float clockWidth) {
        this.clockWidth = clockWidth;
    }

    public void setClockRoundColor(int clockRoundColor) {
        this.clockRoundColor = clockRoundColor;
    }

    public void setClockSecondHandColor(int clockSecondHandColor) {
        this.clockSecondHandColor = clockSecondHandColor;
    }

    public void setClockClockwiseColor(int clockClockwiseColor) {
        this.clockClockwiseColor = clockClockwiseColor;
    }

    public void setClockMinuteHandColor(int clockMinuteHandColor) {
        this.clockMinuteHandColor = clockMinuteHandColor;
    }


    public void setPaintStrokeWidth(float paintStrokeWidth) {
        PaintStrokeWidth = paintStrokeWidth;
    }

    public void setSecondStrokeWidth(float secondStrokeWidth) {
        SecondStrokeWidth = secondStrokeWidth;
    }

    public void setMstrokeWidth(float mstrokeWidth) {
        MstrokeWidth = mstrokeWidth;
    }


    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public void setCenterRadius(int centerRadius) {
        this.centerRadius = centerRadius;
    }


    public void setKeduLength(int keduLength) {
        KeduLength = keduLength;
    }

    public void setNOKeduLength(int NOKeduLength) {
        this.NOKeduLength = NOKeduLength;
    }

    public void setTimeCodeSize(int timeCodeSize) {
        this.timeCodeSize = timeCodeSize;
    }


    public void setRadiuSecond(int radiuSecond) {
        RadiuSecond = radiuSecond;
    }

    public void setRadiusWise(int radiusWise) {
        RadiusWise = radiusWise;
    }

    public void setRadiusM(int radiusM) {
        RadiusM = radiusM;
    }

}
