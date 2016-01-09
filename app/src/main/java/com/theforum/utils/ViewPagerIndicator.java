package com.theforum.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author DEEPANKAR
 * @since 15-12-2015.
 */
public class ViewPagerIndicator extends View {

    int totalNoOfPages;
    int activeDot;
    int dotSpacing;
    int horizontalSpace = 5;

    int dotRadius;
    int activeDotColor;
    int normalDotColor;
    int x=0;



    private Paint mPaint;

    public ViewPagerIndicator(Context context) {
        super(context);
        initialize();
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawDot(canvas);
    }


    private void drawDot(Canvas canvas){
        for(int i=0;i<totalNoOfPages;i++){
            if(i==activeDot){
                mPaint.setColor(activeDotColor);
                canvas.drawCircle(x+dotRadius, getHeight()/2, dotRadius, mPaint);
            }else{
                mPaint.setColor(normalDotColor);
                canvas.drawCircle(x+dotRadius, getHeight()/2, dotRadius, mPaint);
            }
            x = x + 2*dotRadius+horizontalSpace + dotSpacing;
        }
        x=0;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width = totalNoOfPages * ((2*dotRadius) + horizontalSpace + getDotSpacing());
        width = resolveSize(width, widthMeasureSpec);
        int height = 2*dotRadius;
        height = resolveSize(height, heightMeasureSpec);

        setMeasuredDimension(width, height);
    }

    public int getTotalNoOfPages() {
        return totalNoOfPages;
    }

    public void setTotalNoOfPages(int totalNoOfPages) {
        this.totalNoOfPages = totalNoOfPages;
        x = 0;
        invalidate();
    }

    public int getActiveDot() {
        return activeDot;
    }

    public void setActiveDot(int activeDot) {
        this.activeDot = activeDot;
        x=0;
        invalidate();
    }

    public int getDotSpacing() {
        return dotSpacing;
    }

    public void setDotSpacing(int dotSpacing) {
        this.dotSpacing = dotSpacing;
        x=0;
        invalidate();
    }

    private void initialize(){
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dotRadius = (int)CommonUtils.convertDpToPixel(4,getContext());
        activeDotColor = Color.parseColor("#adadad");
        normalDotColor = Color.parseColor("#5b5b5b");
    }
}
