package com.codezzz.clocklockscreen.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by codez on 2017/7/6.
 * Description:
 */

public class ClockView extends View{
    private Matrix mMatrix = new Matrix();
    private Camera mCamera = new Camera();

    private Paint mPaintBg;
    private Paint mPaintText;
    private float SPACING_PERCENT = (float) 1/20;
    private float BAR_HEIGHT_PERCENT = (float) 1/200;

    private String strHour = "00";
    private String strMin = "00";
    private String strAPM = "AM";
    public ClockView(Context context) {
        this(context,null);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //设置背景颜色为黑色
        setBackgroundColor(0xff000000);
        //初始化两个画笔：mPaintBg、mPaintText
        mPaintBg = new Paint();
        mPaintBg.setAntiAlias(true);

        mPaintText = new Paint();
        mPaintText.setAntiAlias(true);
        mPaintText.setColor(0xffffffff);
        mPaintText.setTextAlign(Paint.Align.CENTER);
//        Typeface font = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD);
//        mPaintText.setTypeface(font);
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/HelveticaBold.ttf");
        mPaintText.setTypeface(font);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        getContext().registerReceiver(receiver, filter);

        //初始时间：hour、min
        getHourAndMin();
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
                getHourAndMin();
                invalidate();
            }
        }
    };
    private void getHourAndMin(){
        Date date = new Date();
        DateFormat formatAPM = new SimpleDateFormat("HH");
        DateFormat formatOfHour = new SimpleDateFormat("hh");
        DateFormat formatOfMin = new SimpleDateFormat("mm");
        int hour_24 = Integer.parseInt(formatAPM.format(date));
        strAPM = hour_24>12?"PM":"AM";
        strHour = Integer.parseInt(formatOfHour.format(date))+"";
        strMin = formatOfMin.format(date);
    }
    float offsetY = 0;
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.save();
        canvas.rotate(0,0,40);

        float width = getWidth();
        float height = getHeight();

        offsetY+=1;
        Log.e("ZSX", "offsetY:" + offsetY);

        RectF rectFL = new RectF(width*SPACING_PERCENT, height/2-width*(1-SPACING_PERCENT*3)/4+offsetY,width/2-width*SPACING_PERCENT/2,height/2+width*(1-SPACING_PERCENT*3)/4+offsetY);
        RectF rectFR = new RectF(width/2+width*SPACING_PERCENT/2, height/2-width*(1-SPACING_PERCENT*3)/4+offsetY,width*(1-SPACING_PERCENT),height/2+width*(1-SPACING_PERCENT*3)/4+offsetY);

        mPaintBg.setColor(0xff222222);
        //绘制左侧圆角矩形
        canvas.drawRoundRect(rectFL, width/12,width/12, mPaintBg);
        //绘制右侧圆角矩形
        canvas.drawRoundRect(rectFR, width/12,width/12, mPaintBg);

        //绘制小时数字
        Rect bounds = new Rect();
        mPaintText.setTextSize(rectFL.height()*5/6);
        mPaintText.getTextBounds(strHour, 0, strHour.length(), bounds);
        Paint.FontMetricsInt fontMetricsInt = mPaintText.getFontMetricsInt();
        int baseline = (int) ((rectFL.height() - fontMetricsInt.descent+ fontMetricsInt.ascent)/2
                -fontMetricsInt.ascent);
        canvas.drawText(strHour,
                rectFL.centerX(),
                height/2-width*(1-SPACING_PERCENT*3)/4+baseline+offsetY,
                mPaintText);
        //绘制AM/PM
        mPaintText.setTextSize(rectFL.height()/12);
        canvas.drawText(strAPM,
                rectFL.left+rectFL.height()/8,
                height/2-width*(1-SPACING_PERCENT*3)/4+baseline+offsetY,
                mPaintText);
        //绘制分钟数字
        mPaintText.setTextSize(rectFL.height()*5/6);
        mPaintText.getTextBounds(strMin, 0, strMin.length(), bounds);
        fontMetricsInt = mPaintText.getFontMetricsInt();
        baseline = (int) ((rectFL.height() - fontMetricsInt.descent+ fontMetricsInt.ascent)/2
                -fontMetricsInt.ascent);
        canvas.drawText(strMin,
                rectFR.centerX(),
                height/2-width*(1-SPACING_PERCENT*3)/4+baseline+offsetY,
                mPaintText);
        //绘制黑色中间横条
        mPaintBg.setColor(0xff000000);
        canvas.drawRect(new RectF(0, height/2-width*BAR_HEIGHT_PERCENT+offsetY,width,height/2+width*BAR_HEIGHT_PERCENT-offsetY), mPaintBg);


        canvas.restore();
        mHandler.sendEmptyMessageDelayed(ROTATE_SECOND, 1000);

        mMatrix.reset();
        mCamera.save();
        mCamera.rotateX(40);
        mCamera.rotateY(40);
        mCamera.getMatrix(mMatrix);
        mCamera.restore();
        mMatrix.preTranslate(-width/2, -height/2);
        mMatrix.postTranslate(width/2, height/2);
        canvas.concat(mMatrix);
    }

    private void rotateCanvas(Canvas canvas) {

    }

    private static final int ROTATE_SECOND = 1;
    private int flag = 0;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case ROTATE_SECOND:
                    flag++;
                    Log.e("ZSX", "handler:"+flag);
                    if (flag < 10) {
                        mHandler.sendEmptyMessageDelayed(ROTATE_SECOND,1000);
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onDetachedFromWindow() {
        getContext().unregisterReceiver(receiver);
        mHandler.removeMessages(ROTATE_SECOND);
        super.onDetachedFromWindow();
    }
}
