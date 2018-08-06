package com.imogene.android.carcase.number_drawable;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.annotation.NonNull;

import static com.imogene.android.carcase.commons.util.AppUtils.Commons.checkApiLevel;
import static com.imogene.android.carcase.commons.util.AppUtils.Graphics.convertDpsInPixels;

/**
 * Created by Admin on 28.07.2017.
 */

public class NumberDrawable extends Drawable {

    private final Paint paint;
    private final RectF rectF;
    private final Paint textPaint;
    private final int maxNumber;
    private final Rect padding = new Rect();
    private String number;

    private NumberDrawable(Builder builder){
        maxNumber = builder.maxNumber;
        number = covertNumberToString(builder.number);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(builder.circleColor);
        if(builder.filled){
            paint.setStyle(Paint.Style.FILL);
        }else {
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(builder.strokeWidth);
        }
        float circleSize = builder.circleSize;
        rectF = new RectF(0, 0, circleSize, circleSize);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(builder.textColor);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(builder.textSize);
    }

    @Override
    public int getIntrinsicWidth() {
        return (int) rectF.width();
    }

    @Override
    public int getIntrinsicHeight() {
        return (int) rectF.height();
    }

    @Override
    public int getMinimumWidth() {
        getPadding(padding);
        return getIntrinsicWidth() + padding.width();
    }

    @Override
    public int getMinimumHeight() {
        getPadding(padding);
        return getIntrinsicHeight() + padding.height();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        // draw circle
        canvas.drawOval(rectF, paint);

        // draw text
        final int width = getIntrinsicWidth();
        final int height = getIntrinsicHeight();
        float x = width / 2;
        float y = height / 2 - (textPaint.ascent() + textPaint.descent()) / 2;
        canvas.drawText(number, x, y, textPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
        textPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);
        textPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public void setNumber(int number){
        this.number = covertNumberToString(number);
    }

    private String covertNumberToString(int number){
        if(number > maxNumber){
            return maxNumber + "+";
        }else {
            return String.valueOf(number);
        }
    }

    public static class Builder{

        private static final float DEFAULT_CIRCLE_SIZE_DP = 24f;
        private static final float DEFAULT_TEXT_SIZE_SP = 10f;
        private static final float DEFAULT_STROKE_WIDTH_DP = 1f;

        private int maxNumber;
        private int number;
        private float circleSize;
        private float textSize;
        private int circleColor;
        private int textColor;
        private boolean filled;
        private float strokeWidth;

        @SuppressWarnings("deprecation")
        public Builder(Context context){
            // defaults
            maxNumber = 99;
            number = 0;
            filled = true;

            // default sizes
            circleSize = convertDpsInPixels(context, DEFAULT_CIRCLE_SIZE_DP);
            textSize = convertDpsInPixels(context, DEFAULT_TEXT_SIZE_SP);
            strokeWidth = convertDpsInPixels(context, DEFAULT_STROKE_WIDTH_DP);

            // default colors
            circleColor = getAccentColor(context);
            textColor = Color.WHITE;
        }

        @SuppressWarnings("ResourceType")
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        private int getAccentColor(Context context){
            boolean lollipop = checkApiLevel(Build.VERSION_CODES.LOLLIPOP);

            int[] attrs = !lollipop ? new int[]{R.attr.colorAccent} :
                    new int[]{android.R.attr.colorAccent, R.attr.colorAccent};

            TypedArray array = context.getTheme().obtainStyledAttributes(attrs);
            try {
                int color = array.getColor(0, 0);

                if(color == 0){
                    if(!lollipop){
                        return Color.CYAN;
                    }else {
                        color = array.getColor(1,0);
                    }
                }else {
                    return color;
                }

                if(color == 0){
                    color = Color.CYAN;
                }

                return color;
            }finally {
                array.recycle();
            }
        }

        public Builder maxNumber(int maxNumber){
            this.maxNumber = maxNumber;
            return this;
        }

        public Builder number(int number){
            this.number = number;
            return this;
        }

        public Builder circleSize(float circleSize){
            this.circleSize = circleSize;
            return this;
        }

        public Builder textSize(float textSize){
            this.textSize = textSize;
            return this;
        }

        public Builder circleColor(int circleColor){
            this.circleColor = circleColor;
            return this;
        }

        public Builder textColor(int textColor){
            this.textColor = textColor;
            return this;
        }

        public Builder filled(boolean filled){
            this.filled = filled;
            return this;
        }

        public Builder strokeWidth(float strokeWidth){
            this.strokeWidth = strokeWidth;
            return this;
        }

        public NumberDrawable build(){
            return new NumberDrawable(this);
        }
    }
}
