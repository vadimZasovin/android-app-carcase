package com.imogene.android.carcase.view.progress

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.support.v4.view.animation.LinearOutSlowInInterpolator
import android.support.v7.widget.AppCompatTextView
import android.text.SpannableString
import android.text.Spanned
import android.util.AttributeSet
import com.imogene.waitingdots.JumpingSpan
import com.imogene.waitingdots.SinEvaluator

/**
 * This is the view that can be used as an progress
 * indicator. It is a [TextView] with animated three
 * dots at the end. There are three things that can
 * be customized: duration of one cycle of the
 * animation, height of the jumping and an boolean
 * flag that controls whether the animation of the
 * dots must be started automatically. In the rest
 * this view behaves like ordinary TextView.
 */
class ProgressTextView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) :
        AppCompatTextView(context, attrs, defStyleAttr) {

    companion object {

        private val DURATION_DEFAULT = 1000

        private val COLLAPSING_DURATION = 400L

        private val EXPANDING_DURATION = 800L
    }

    /**
     * Duration of one cycle of the animation.
     */
    private val duration: Long

    /**
     * Height of the jumping in pixels.
     */
    private val jumpHeight : Float

    /**
     * Controls whether the jumping animation
     * must be started automatically.
     */
    private val autoStart : Boolean

    private val spanOne = JumpingSpan()
    private val spanTwo = JumpingSpan()
    private val spanThree = JumpingSpan()

    /**
     * To set the new text to this view, use this
     * property instead of [setText] and it's overloads.
     * This property additionally updates spans that
     * are used to animate dots.
     */
    var progressText : CharSequence = ""
        set(value) {
            field = value
            updateText(value)
        }

    /**
     * If the specified [CharSequence] is empty just sets it
     * as a TextView's text. Otherwise appends three dots to
     * the specified text, configures spans and set the
     * resulting [SpannableString] as text.
     */
    private fun updateText(text: CharSequence){
        if(text.isEmpty()){
            setText(text)
        } else {
            updateTextAndSpans(text)
        }
    }

    /**
     * Appends three dots to the specified text, configures
     * spans and set the resulting [SpannableString] as text.
     */
    private fun updateTextAndSpans(text: CharSequence){
        val length = text.length
        val source = text.toString() + "..."
        val spannable = SpannableString(source)
        val flag = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        spannable.setSpan(spanOne, length, length + 1, flag)
        spannable.setSpan(spanTwo, length + 1, length + 2, flag)
        spannable.setSpan(spanThree, length + 2, length + 3, flag)
        setText(spannable, BufferType.SPANNABLE)
    }

    /**
     * The width of a single dot.
     */
    private val dotWidth : Float

    /**
     * TypeEvaluator to be used by animators.
     */
    private val evaluator = SinEvaluator()

    /**
     * AnimatorSet that contains animators for
     * three dots.
     */
    private val animators = AnimatorSet()

    /**
     * Indicates whether the dots jumping animation
     * is started or not.
     */
    var isStarted: Boolean = false
        private set

    /**
     * Indicates whether the dots are expanded or not.
     */
    var isExpanded: Boolean = false
        private set

    init {
        val array = context.obtainStyledAttributes(attrs, R.styleable.ProgressTextView)
        try {
            duration = array.getInt(
                    R.styleable.ProgressTextView_ptv_duration,
                    DURATION_DEFAULT).toLong()

            val defaultJumpHeight = textSize.toInt() / 4
            jumpHeight = array.getDimensionPixelSize(
                    R.styleable.ProgressTextView_ptv_jumpHeight,
                    defaultJumpHeight).toFloat()

            autoStart = array.getBoolean(
                    R.styleable.ProgressTextView_ptv_autoStart,
                    true)
        } finally {
            array.recycle()
        }

        progressText = text
        dotWidth = paint.measureText(".")

        val first = createAnimator(spanOne, 0)
        first.addUpdateListener{ invalidate() }
        val second = createAnimator(spanTwo, duration / 6)
        val third = createAnimator(spanThree, duration / 3)
        animators.playTogether(first, second, third)
    }

    private fun createAnimator(jumpingSpan: JumpingSpan, delay: Long) : ObjectAnimator {
        val values = floatArrayOf(0F, -jumpHeight)
        val animator = ObjectAnimator.ofFloat(jumpingSpan, "translationY", *values)
        return animator.apply {
            setEvaluator(evaluator)
            duration = this@ProgressTextView.duration
            startDelay = delay
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.RESTART
        }
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if(autoStart && hasWindowFocus){
            start()
        } else if(!hasWindowFocus && isStarted){
            stop()
        }
    }

    fun start() {
        isStarted = true
        updateRepeatCount(ValueAnimator.INFINITE)
        animators.start()
    }

    private fun updateRepeatCount(repeatCount: Int) {
        animators.childAnimations
                .filterIsInstance<ObjectAnimator>()
                .forEach { it.repeatCount = repeatCount }
    }

    fun stop() {
        isStarted = false
        updateRepeatCount(0)
    }

    private lateinit var collapseAnimator : ValueAnimator

    private lateinit var expandAnimator : ValueAnimator

    fun collapse() {
        if(!isExpanded){
            return
        }
        // initialize lazily collapse animator
        ensureCollapseAnimator()
        // update all the properties (translationX,
        // alpha) of all three spans in a single
        // listener
        collapseAnimator.addUpdateListener {
            val fraction = it.animatedFraction
            val alpha = 1 - fraction
            updateSpansAlpha(alpha)
            moveSpans(fraction, false)
            invalidate()
            // to avoid adding an AnimatorListener
            // we can check the fraction to check
            // if the animation is finished
            if(fraction == 1F){
                isExpanded = false
            }
        }
        collapseAnimator.start()
    }

    private fun updateSpansAlpha(alpha: Float){
        spanOne.alpha = alpha
        spanTwo.alpha = alpha
        spanThree.alpha = alpha
    }

    private fun moveSpans(fraction: Float, expand: Boolean){
        val second : Float
        if(expand){
            second = -dotWidth + dotWidth * fraction
        } else {
            second = -dotWidth * fraction
        }
        val multiplier = 2
        val third = second * multiplier
        spanTwo.translationX = second
        spanThree.translationX = third
    }

    private fun ensureCollapseAnimator(){
        if(!this::collapseAnimator.isInitialized){
            initCollapseAnimator()
        }
    }

    private fun initCollapseAnimator(){
        collapseAnimator = ValueAnimator.ofFloat(0F, 1F)
        collapseAnimator.duration = COLLAPSING_DURATION
        collapseAnimator.interpolator = LinearOutSlowInInterpolator()
    }

    fun expand() {
        if(isExpanded){
            return
        }
        // initialize lazily expand animator
        ensureExpandAnimator()
        // update all the properties (translationX,
        // alpha) of all three spans in a single
        // listener
        expandAnimator.addUpdateListener {
            val fraction = it.animatedFraction
            updateSpansAlpha(fraction)
            moveSpans(fraction, true)
            invalidate()
            // to avoid adding an AnimatorListener
            // we can check the fraction to check
            // if the animation is finished
            if(fraction == 1F){
                isExpanded = true
            }
        }
        expandAnimator.start()
    }

    private fun ensureExpandAnimator(){
        if(!this::expandAnimator.isInitialized){
            initExpandAnimator()
        }
    }

    private fun initExpandAnimator(){
        expandAnimator = ValueAnimator.ofFloat(0F, 1F)
        expandAnimator.duration = EXPANDING_DURATION
        expandAnimator.interpolator = FastOutSlowInInterpolator()
    }
}