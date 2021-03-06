package com.imogene.android.carcase.view.guide;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * Created by Admin on 06.06.2017.
 */

public class CircleGuideStepView extends View implements GuideStepView {

    public static final int STYLE_FILLED = 0;
    public static final int STYLE_BORDERED = 1;

    public static final int ANIMATION_NONE = 0;
    public static final int ANIMATION_MOVE = 1;
    public static final int ANIMATION_FADE = 2;
    public static final int ANIMATION_BORDER_WIDTH = 3;

    private int size;
    private int stepsCount;
    private int stepMargin;
    private int colorInactive;
    private int colorActive;
    private int styleInactive;
    private int styleActive;
    private float borderWidthInactive;
    private float borderWidthActive;
    private int animationType;

    private int currentStep;
    private Drawer drawer;
    private float maxBorderSize;

    public CircleGuideStepView(Context context) {
        this(context, null);
    }

    public CircleGuideStepView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleGuideStepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(
                attrs, R.styleable.CircleGuideStepView,
                defStyleAttr, R.style.Widget_CircleGuideStepView_Filled);

        try {
            size = array.getDimensionPixelSize(R.styleable.CircleGuideStepView_cgsv_size, 0);
            stepsCount = array.getInteger(R.styleable.CircleGuideStepView_cgsv_stepsCount, 0);
            stepMargin = array.getDimensionPixelSize(R.styleable.CircleGuideStepView_cgsv_stepMargin, 0);
            colorInactive = array.getColor(R.styleable.CircleGuideStepView_cgsv_colorInactive, 0);
            colorActive = array.getColor(R.styleable.CircleGuideStepView_cgsv_colorActive, 0);
            styleInactive = array.getInteger(R.styleable.CircleGuideStepView_cgsv_styleInactive, 0);
            styleActive = array.getInteger(R.styleable.CircleGuideStepView_cgsv_styleActive, 0);
            animationType = array.getInteger(R.styleable.CircleGuideStepView_cgsv_animation, 0);
            borderWidthActive = array.getDimensionPixelSize(R.styleable.CircleGuideStepView_cgsv_borderWidthActive, 0);
            borderWidthInactive = array.getDimensionPixelSize(R.styleable.CircleGuideStepView_cgsv_borderWidthInactive, 0);
        }finally {
            array.recycle();
        }

        prepareDrawer();
    }

    public void setSize(int size) {
        this.size = size;
        requestLayout();
    }

    public int getSize() {
        return size;
    }

    @Override
    public void setStepsCount(int stepsCount) {
        this.stepsCount = stepsCount;
        requestLayout();
    }

    @Override
    public int getStepsCount() {
        return stepsCount;
    }

    public void setStepMargin(int stepMargin) {
        this.stepMargin = stepMargin;
        requestLayout();
    }

    public int getStepMargin() {
        return stepMargin;
    }

    public void setColorInactive(int colorInactive) {
        this.colorInactive = colorInactive;
        invalidate();
    }

    public int getColorInactive() {
        return colorInactive;
    }

    public void setColorActive(int colorActive) {
        this.colorActive = colorActive;
        invalidate();
    }

    public int getColorActive() {
        return colorActive;
    }

    public void setStyleInactive(int styleInactive) {
        this.styleInactive = styleInactive;
        invalidate();
    }

    public int getStyleInactive() {
        return styleInactive;
    }

    public void setStyleActive(int styleActive) {
        this.styleActive = styleActive;
        invalidate();
    }

    public int getStyleActive() {
        return styleActive;
    }

    public void setBorderWidthInactive(float borderWidthInactive) {
        this.borderWidthInactive = borderWidthInactive;
        if(styleInactive == STYLE_BORDERED){
            requestLayout();
        }
    }

    public float getBorderWidthInactive() {
        if(styleInactive == STYLE_BORDERED){
            return borderWidthInactive;
        } else {
            return 0F;
        }
    }

    public void setBorderWidthActive(float borderWidthActive) {
        this.borderWidthActive = borderWidthActive;
        if(styleActive == STYLE_BORDERED){
            requestLayout();
        }
    }

    public float getBorderWidthActive() {
        if(styleActive == STYLE_BORDERED){
            return borderWidthActive;
        } else {
            return 0F;
        }
    }

    @Override
    public void setCurrentStep(int step){
        if(step != currentStep){
            if(step >= stepsCount){
                throw new IllegalArgumentException(
                        "Step must be less than steps count.");
            }

            if(isAnimationEnabled()){
                drawer.animate(step);
            } else {
                currentStep = step;
                invalidate();
            }
        }
    }

    @Override
    public int getCurrentStep(){
        return currentStep;
    }

    @Override
    public boolean isAnimationEnabled() {
        return animationType != ANIMATION_NONE;
    }

    public void setAnimationType(int animationType) {
        this.animationType = animationType;
        prepareDrawer();
    }

    public int getAnimationType() {
        return animationType;
    }

    private void prepareDrawer(){
        switch (animationType){
            case ANIMATION_MOVE:
                drawer = new MoveDrawer();
                break;
            case ANIMATION_FADE:
                drawer = new FadeDrawer();
                break;
            case ANIMATION_BORDER_WIDTH:
                drawer = new BorderWidthDrawer();
                break;
            case ANIMATION_NONE:
                drawer = new Drawer();
                break;
            default:
                throw new IllegalStateException(
                        "Unsupported animation type: " + animationType);
        }
    }

    private class Drawer implements ValueAnimator.AnimatorUpdateListener{

        final Paint paint;
        final RectF circleBounds;
        boolean drawingActiveStep;
        private int newStep;
        private ValueAnimator animator;
        boolean animatingCurrentStepChange;

        Drawer(){
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            circleBounds = new RectF();
        }

        void onDraw(Canvas canvas){
            for(int step = 0; step < stepsCount; step++){
                updatePaint(step);
                updateCircleBounds(step);
                canvas.drawOval(circleBounds, paint);
            }
        }

        void updatePaint(int step){
            drawingActiveStep = step == currentStep;
            if(drawingActiveStep){
                updatePaintActive();
            }else {
                updatePaintInactive();
            }
        }

        void updatePaintActive(){
            paint.setColor(colorActive);
            if(styleActive == STYLE_FILLED){
                paint.setStyle(Paint.Style.FILL);
            }else {
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(borderWidthActive);
            }
        }

        void updatePaintInactive(){
            paint.setColor(colorInactive);
            if(styleInactive == STYLE_FILLED){
                paint.setStyle(Paint.Style.FILL);
            }else {
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(borderWidthInactive);
            }
        }

        final void updateBounds(int step, RectF bounds){
            bounds.left = step * size + step * stepMargin;
            bounds.right = bounds.left + size;
            bounds.top = 0;
            bounds.bottom = size;
            bounds.offset(maxBorderSize, maxBorderSize);
        }

        final void updateCircleBounds(int step){
            updateBounds(step, circleBounds);
        }

        final void animate(int newStep){
            if(animatingCurrentStepChange){
                animator.cancel();
            }

            this.newStep = newStep;

            animator = createAnimator(newStep);
            animator.addListener(animatorListener);
            animator.addUpdateListener(this);
            animator.start();
        }

        ValueAnimator createAnimator(int newStep){
            return null;
        }

        private final Animator.AnimatorListener animatorListener = new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                animatingCurrentStepChange = true;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                onAnimationFinished();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                onAnimationFinished();
            }

            private void onAnimationFinished(){
                animatingCurrentStepChange = false;
                currentStep = newStep;
            }
        };

        @Override
        public final void onAnimationUpdate(ValueAnimator animation) {
            onUpdateProperty(animation);
            invalidate();
        }

        void onUpdateProperty(ValueAnimator animation){}
    }

    private class MoveDrawer extends Drawer {

        private float distance;
        private float initialLeft;
        private RectF animatedCircleBounds;

        @Override
        void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if(animatingCurrentStepChange){
                updatePaintActive();
                canvas.drawOval(animatedCircleBounds, paint);
            }
        }

        @Override
        void updatePaint(int step) {
            if(animatingCurrentStepChange){
                if(drawingActiveStep){
                    updatePaintInactive();
                    return;
                }
            }
            super.updatePaint(step);
        }

        @Override
        ValueAnimator createAnimator(int newStep) {
            distance = calculateDistance(newStep);
            initialLeft = animatedCircleBounds.left;
            return ValueAnimator.ofFloat(0, distance);
        }

        private float calculateDistance(int newStep){
            if(!animatingCurrentStepChange){
                updateAnimatedCircleBounds(currentStep);
            }
            updateCircleBounds(newStep);
            float x1 = animatedCircleBounds.centerX();
            float x2 = circleBounds.centerX();
            return x2 - x1;
        }

        private void updateAnimatedCircleBounds(int step){
            ensureAnimatedCircleBounds();
            updateBounds(step, animatedCircleBounds);
        }

        private void ensureAnimatedCircleBounds(){
            if(animatedCircleBounds == null){
                animatedCircleBounds = new RectF();
                animatedCircleBounds.top = 0;
                animatedCircleBounds.bottom = size;
            }
        }

        @Override
        void onUpdateProperty(ValueAnimator animation) {
            float fraction = animation.getAnimatedFraction();
            float offset = fraction * distance;
            float left = initialLeft + offset;
            float top = animatedCircleBounds.top;
            animatedCircleBounds.offsetTo(left, top);
        }
    }

    private class FadeDrawer extends Drawer {

        private final ArgbEvaluator evaluator;
        private final int firstColor;
        private final int secondColor;
        private int newStep;
        private int outColor;
        private int inColor;

        FadeDrawer(){
            evaluator = new ArgbEvaluator();
            firstColor = colorActive;
            secondColor = colorInactive;
            outColor = firstColor;
            inColor = firstColor;
        }

        @Override
        ValueAnimator createAnimator(int newStep) {
            this.newStep = newStep;
            ValueAnimator animator = new ValueAnimator();
            animator.setEvaluator(evaluator);
            animator.setIntValues(firstColor, secondColor);
            return animator;
        }

        @Override
        void onUpdateProperty(ValueAnimator animation) {
            float fraction = animation.getAnimatedFraction();
            outColor = (int) evaluator.evaluate(fraction, firstColor, secondColor);
            inColor = (int) evaluator.evaluate(fraction, secondColor, firstColor);
        }

        @Override
        void updatePaint(int step) {
            super.updatePaint(step);
            if(step == newStep){
                paint.setColor(inColor);
            }else if(step == currentStep){
                paint.setColor(outColor);
            }
        }
    }

    private class BorderWidthDrawer extends Drawer{

        private final float startWidth;
        private final float endWidth;
        private int newStep;
        private float outWidth;
        private float inWidth;

        BorderWidthDrawer(){
            startWidth = borderWidthActive;
            endWidth = borderWidthInactive;
            outWidth = startWidth;
            inWidth = startWidth;
        }

        @Override
        ValueAnimator createAnimator(int newStep) {
            this.newStep = newStep;
            return ValueAnimator.ofFloat(startWidth, endWidth);
        }

        @Override
        void onUpdateProperty(ValueAnimator animation) {
            outWidth = (float) animation.getAnimatedValue();
            if(startWidth > endWidth){
                inWidth = Math.max(startWidth - outWidth, endWidth);
            }else {
                inWidth = Math.min(Math.max(endWidth - outWidth, startWidth), endWidth);
            }
        }

        @Override
        void updatePaint(int step) {
            super.updatePaint(step);
            if(step == newStep){
                paint.setStrokeWidth(inWidth);
            }else if(step == currentStep){
                paint.setStrokeWidth(outWidth);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();
        final int paddingTop = getPaddingTop();
        final int paddingBottom = getPaddingBottom();

        int width = paddingLeft + paddingRight;
        width += size * stepsCount;
        width += stepMargin * (stepsCount - 1);

        maxBorderSize = 0;
        if(styleActive == STYLE_BORDERED){
            maxBorderSize = borderWidthActive;
        }
        if(styleInactive == STYLE_BORDERED && borderWidthInactive > maxBorderSize){
            maxBorderSize = borderWidthInactive;
        }
        float borders = maxBorderSize * 2;
        width += borders;

        int height = paddingTop + paddingBottom;
        height += size;
        height += borders;

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawer.onDraw(canvas);
    }
}
