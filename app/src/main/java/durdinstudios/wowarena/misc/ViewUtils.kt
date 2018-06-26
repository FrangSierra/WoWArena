/*
 * Copyright (c) 2017 Mundo Reader S.L.
 * All Rights Reserved.
 * Confidential and Proprietary - Mundo Reader S.L.
 */

package durdinstudios.wowarena.misc

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager

private val DISPLAY_METRICS = DisplayMetrics()
private val VISIBILITY_INTERPOLATOR = FastOutSlowInInterpolator()
private val LOCATION_TMP = IntArray(2)

fun View.makeVisible() = run { visibility = View.VISIBLE }
fun View.makeInvisible() = run { visibility = View.INVISIBLE }
fun View.makeGone() = run { visibility = View.GONE }
fun View.isVisible() = visibility == View.VISIBLE
fun View.isInvisible() = visibility == View.INVISIBLE
fun View.isGone() = visibility == View.GONE

/**
 * Animate visible with alpha. Duration will be 0 if view is already visible.
 */
fun View.makeVisibleAnim(duration: Int): ObjectAnimator {
    visibility = View.VISIBLE
    alpha = 0f
    val alphaAnim = ObjectAnimator.ofFloat<View>(this, View.ALPHA, 1f)
    alphaAnim.interpolator = VISIBILITY_INTERPOLATOR
    alphaAnim.duration = if (isVisible()) 0 else duration.toLong()
    return alphaAnim
}

/**
 * Animate invisible / gone. Duration will be 0 if view is already invisible.
 */
fun View.makeInvisibleAnim(duration: Int, endVisibility: Int = View.INVISIBLE): ObjectAnimator {
    val startingAlpha = alpha
    val alphaAnim = ObjectAnimator.ofFloat<View>(this, View.ALPHA, startingAlpha, 0f)
    alphaAnim.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            visibility = endVisibility
            alpha = startingAlpha
        }
    })
    alphaAnim.interpolator = VISIBILITY_INTERPOLATOR
    alphaAnim.duration = if (!isVisible()) 0 else duration.toLong()
    return alphaAnim
}

/**
 * Capture a view into a bitmap with the specified width, size and base color (fill).
 */
fun View.createBitmap(width: Int, height: Int, baseColor: Int): Bitmap {
    val b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val c = Canvas(b)
    c.drawColor(baseColor)
    layout(0, 0, layoutParams.width, layoutParams.height)
    draw(c)
    return b
}

/**
 * Get the current view bounds relative to the screen. This includes hardware buttons.
 */
fun View.getViewScreenBounds(outRect: Rect = Rect()): Rect {
    val location = LOCATION_TMP
    getLocationOnScreen(location)
    outRect.left = location[0]
    outRect.top = location[1]
    outRect.right = outRect.left + width
    outRect.bottom = outRect.top + height
    return outRect
}

/**
 * Get the current view bounds relative to the window. This
 */
fun View.getViewWindowBounds(outRect: Rect = Rect()): Rect {
    val location = LOCATION_TMP
    getLocationInWindow(location)
    outRect.left = location[0]
    outRect.top = location[1]
    outRect.right = outRect.left + width
    outRect.bottom = outRect.top + height
    return outRect
}

/**
 * Determines if given points are inside view.
 *
 * @param x    - x coordinate of point
 * @param y    - y coordinate of point
 * @return true if the points are within view bounds, false otherwise
 */
fun View.containsPoint(x: Float, y: Float): Boolean {
    val location = LOCATION_TMP
    getLocationOnScreen(location)
    val viewX = location[0]
    val viewY = location[1]

    // point is inside view bounds
    return x > viewX && x < viewX + width && y > viewY && y < viewY + height
}

/**
 * Converts a given pixel size to a device-specific dp size.
 *
 * @param context A [Context] reference
 * @param pixels  Pixels to convert to DP
 * @return DP
 */
fun convertPixelsToDp(context: Context, pixels: Int): Int {
    val realDisplayMetrics = getDisplayMetrics(context)
    return Math.round(pixels / realDisplayMetrics.density)
}

/**
 * Converts a given DP size to a device-specific pixel size.
 *
 * @param context A [Context] reference
 * @param dp      DP to convert to pixels
 * @return pixels
 */
fun convertDpToPixels(context: Context, dp: Int): Int {
    val realDisplayMetrics = getDisplayMetrics(context)
    return Math.round(dp * realDisplayMetrics.density)
}

/**
 * @param context
 * @return DisplayMetrics
 */
fun getDisplayMetrics(context: Context): DisplayMetrics {
    val wm = context.applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    wm.defaultDisplay.getRealMetrics(DISPLAY_METRICS)
    return DISPLAY_METRICS
}
