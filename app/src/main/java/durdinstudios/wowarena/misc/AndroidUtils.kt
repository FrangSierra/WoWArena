package durdinstudios.wowarena.misc

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.support.annotation.*
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import java.util.*


//Arguments

@Suppress("UNCHECKED_CAST")
fun <T> Fragment.argument(key: String): Lazy<T> = lazy { this.arguments?.get(key) as T }

@Suppress("UNCHECKED_CAST")
fun <T> Activity.argument(key: String): Lazy<T> = lazy { this.intent.extras[key] as T }


//Toasting

fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) = Toast.makeText(this, message, duration).show()

fun Context.toast(@StringRes message: Int, duration: Int = Toast.LENGTH_SHORT) = Toast.makeText(this, getString(message), duration).show()

fun Fragment.toast(message: String, duration: Int = Toast.LENGTH_SHORT) = this.activity?.toast(message, duration)

fun Fragment.toast(@StringRes message: Int, duration: Int = Toast.LENGTH_SHORT) = this.activity?.toast(message, duration)

//Bundle

fun Map<String, *>.toBundle(outBundle: Bundle = Bundle()): Bundle {
    val values = this.entries.map { it.key to it.value }
    return bundleOf(pairs = *values.toTypedArray(), outBundle = outBundle)
}

fun bundleOf(vararg pairs: Pair<String, *>, outBundle: Bundle = Bundle()): Bundle {
    outBundle.apply {
        pairs.forEach {
            val (k, v) = it
            when (v) {
                is Bundle -> putBundle(k, v)
                is Byte -> putByte(k, v)
                is ByteArray -> putByteArray(k, v)
                is Char -> putChar(k, v)
                is CharArray -> putCharArray(k, v)
                is CharSequence -> putCharSequence(k, v)
                is Float -> putFloat(k, v)
                is FloatArray -> putFloatArray(k, v)
                is Parcelable -> putParcelable(k, v)
                is Int -> putInt(k, v)
                is Long -> putLong(k, v)
                is Short -> putShort(k, v)
                is ShortArray -> putShortArray(k, v)
                is Boolean -> putBoolean(k, v)
                else -> throw IllegalArgumentException("$v is of a type that is not currently supported")
            }
        }
    }
    return outBundle
}


//Resources

fun String.toUri(): Uri = Uri.parse(this)


fun Context.formatPlural(@PluralsRes textId: Int, quantity: Int): String = resources.getQuantityString(textId, quantity, quantity)
fun Context.drawableCompat(@DrawableRes drawableResource: Int): Drawable? = ContextCompat.getDrawable(this, drawableResource)
fun Context.colorCompat(@ColorRes colorResource: Int): Int = ContextCompat.getColor(this, colorResource)
fun Fragment.colorCompat(@ColorRes colorResource: Int): Int = activity!!.colorCompat(colorResource)
fun View.colorCompat(@ColorRes colorResource: Int): Int = context.colorCompat(colorResource)
fun Context.getAttribute(@AttrRes attr: Int): TypedValue {
    val outValue = TypedValue()
    theme.resolveAttribute(attr, outValue, true)
    return outValue
}

val random by lazy { Random() }
fun randomColor(alpha: Int = 255): Int {
    return Color.argb(alpha, random.nextInt(256), random.nextInt(256), random.nextInt(256))
}

private val point by lazy { Point() }
fun Context.calculateScreenSize(): Pair<Int, Int> {
    val display = (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
    display.getSize(point)
    val width = point.x
    val height = point.y
    return width to height
}
//Keyboard

fun Activity.hideKeyboard(focusView: View? = this.currentFocus, force: Boolean = true) {
    if (focusView == null) return
    val flags = if (force) 0 else InputMethodManager.HIDE_IMPLICIT_ONLY
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(focusView.windowToken, flags)
}

fun Long.toMonthlyDay(): String {
    val date = Date(this)
    val cal = Calendar.getInstance()
    cal.time = date
    val month = cal.get(Calendar.MONTH).plus(1)
    val day = cal.get(Calendar.DAY_OF_MONTH)
    return "$day/$month"
}

fun List<Long>.mapToDate() = this.map { it.toMonthlyDay() }

fun List<Long>.mapToDateAndTime() = this.map { it.toMonthlyDay() to it }