package org.mainsoft.base.lib.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import org.mainsoft.base.R.styleable

class MotionImageView : AppCompatImageView {
    private var m = FloatArray(20)
    private var mColorMatrix = ColorMatrix()
    private var mTmpColorMatrix = ColorMatrix()
    private var mSaturation = 1.0f
    private var mContrast = 1.0f
    private var mWarmth = 1.0f
    private var mCrossfade = 0.0f
    private var attributeSet: AttributeSet? = null
    private var mLayers: Array<Drawable?> = arrayOf()
    private var mLayer: LayerDrawable? = null
    private var bgRes: Int = 0

    constructor(context: Context) : super(context) {
        init(context, null as AttributeSet?)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        attributeSet = attrs
        if (attrs != null) {
            val a: TypedArray = context.obtainStyledAttributes(attrs, styleable.MotionImageView)
            val N = a.indexCount
            val drawable: Drawable? = a.getDrawable(styleable.MotionImageView_customDrawable)
            for (i in 0 until N) {
                val attr = a.getIndex(i)
                if (attr == styleable.ImageFilterView_crossfade) {
                    mCrossfade = a.getFloat(attr, 0.0f)
                }
            }
            a.recycle()
            if (drawable != null) {
                mLayers = arrayOfNulls<Drawable?>(2)
                mLayers[0] = drawable
                mLayers[1] = drawable
                mLayer = LayerDrawable(mLayers)
                mLayer?.getDrawable(1)?.alpha = (255.0f * mCrossfade).toInt()
                super.setImageDrawable(mLayer)
            }
        }
    }

    fun setBackgroundDrawable(res: Int) {
        this.bgRes = res
    }

    fun setAltDrawable(res: Int) {
        val drawable = ContextCompat.getDrawable(context, res)
        val a: TypedArray = this.context.obtainStyledAttributes(attributeSet, styleable.MotionImageView)
        val N = a.indexCount
        for (i in 0 until N) {
            val attr = a.getIndex(i)
            if (attr == styleable.ImageFilterView_crossfade) {
                mCrossfade = a.getFloat(attr, 0.0f)
            }
        }
        a.recycle()
        if (drawable != null) {
            mLayers = arrayOfNulls<Drawable?>(2)
            mLayers[0] = drawable
            mLayers[1] = drawable
            mLayer = LayerDrawable(mLayers)
            mLayer!!.getDrawable(1).alpha = (255.0f * mCrossfade).toInt()
            super.setImageDrawable(mLayer)
        }
    }

    var saturation: Float
        get() = mSaturation
        set(saturation) {
            mSaturation = saturation
            updateMatrix()
        }

    var contrast: Float
        get() = mContrast
        set(contrast) {
            mContrast = contrast
            updateMatrix()
        }

    var warmth: Float
        get() = mWarmth
        set(warmth) {
            mWarmth = warmth
            updateMatrix()
        }

    var crossfade: Float
        get() = mCrossfade
        set(crossfade) {
            mCrossfade = crossfade
            val value = (255.0f * mCrossfade)
            mLayer?.getDrawable(1)?.alpha = value.toInt()
            super.setImageDrawable(mLayer)
            super.setBackgroundResource(if (value >= 0.5f) bgRes else 0)
        }

    private fun saturation(saturationStrength: Float) {
        val Rf = 0.2999f
        val Gf = 0.587f
        val Bf = 0.114f
        val MS = 1.0f - saturationStrength
        val Rt = Rf * MS
        val Gt = Gf * MS
        val Bt = Bf * MS
        m[0] = Rt + saturationStrength
        m[1] = Gt
        m[2] = Bt
        m[3] = 0.0f
        m[4] = 0.0f
        m[5] = Rt
        m[6] = Gt + saturationStrength
        m[7] = Bt
        m[8] = 0.0f
        m[9] = 0.0f
        m[10] = Rt
        m[11] = Gt
        m[12] = Bt + saturationStrength
        m[13] = 0.0f
        m[14] = 0.0f
        m[15] = 0.0f
        m[16] = 0.0f
        m[17] = 0.0f
        m[18] = 1.0f
        m[19] = 0.0f
    }

    private fun warmth(warmth: Float) {
        var warmth = warmth
        val baseTemprature = 5000.0f
        if (warmth <= 0.0f) {
            warmth = 0.01f
        }
        val kelvin = baseTemprature / warmth
        var color_r = kelvin / 100.0f
        var color_g: Float
        var color_b: Float
        var colorR: Float
        if (color_r > 66.0f) {
            colorR = color_r - 60.0f
            color_g = 329.69873f * Math.pow(colorR.toDouble(), -0.13320475816726685).toFloat()
            color_b = 288.12216f * Math.pow(colorR.toDouble(), 0.07551484555006027).toFloat()
        } else {
            color_b = 99.4708f * Math.log(color_r.toDouble()).toFloat() - 161.11957f
            color_g = 255.0f
        }
        var centiKelvin: Float
        centiKelvin = if (color_r < 66.0f) {
            if (color_r > 19.0f) {
                138.51773f * Math.log((color_r - 10.0f).toDouble()).toFloat() - 305.0448f
            } else {
                0.0f
            }
        } else {
            255.0f
        }
        var tmpColor_r = Math.min(255.0f, Math.max(color_g, 0.0f))
        var tmpColor_g = Math.min(255.0f, Math.max(color_b, 0.0f))
        var tmpColor_b = Math.min(255.0f, Math.max(centiKelvin, 0.0f))
        color_r = tmpColor_r
        color_g = tmpColor_g
        color_b = tmpColor_b
        centiKelvin = baseTemprature / 100.0f
        val colorG: Float
        if (centiKelvin > 66.0f) {
            val tmp = centiKelvin - 60.0f
            colorR = 329.69873f * Math.pow(tmp.toDouble(), -0.13320475816726685).toFloat()
            colorG = 288.12216f * Math.pow(tmp.toDouble(), 0.07551484555006027).toFloat()
        } else {
            colorG = 99.4708f * Math.log(centiKelvin.toDouble()).toFloat() - 161.11957f
            colorR = 255.0f
        }
        val colorB: Float
        colorB = if (centiKelvin < 66.0f) {
            if (centiKelvin > 19.0f) {
                138.51773f * Math.log((centiKelvin - 10.0f).toDouble()).toFloat() - 305.0448f
            } else {
                0.0f
            }
        } else {
            255.0f
        }
        tmpColor_r = Math.min(255.0f, Math.max(colorR, 0.0f))
        tmpColor_g = Math.min(255.0f, Math.max(colorG, 0.0f))
        tmpColor_b = Math.min(255.0f, Math.max(colorB, 0.0f))
        color_r /= tmpColor_r
        color_g /= tmpColor_g
        color_b /= tmpColor_b
        m[0] = color_r
        m[1] = 0.0f
        m[2] = 0.0f
        m[3] = 0.0f
        m[4] = 0.0f
        m[5] = 0.0f
        m[6] = color_g
        m[7] = 0.0f
        m[8] = 0.0f
        m[9] = 0.0f
        m[10] = 0.0f
        m[11] = 0.0f
        m[12] = color_b
        m[13] = 0.0f
        m[14] = 0.0f
        m[15] = 0.0f
        m[16] = 0.0f
        m[17] = 0.0f
        m[18] = 1.0f
        m[19] = 0.0f
    }

    private fun updateMatrix() {
        mColorMatrix.reset()
        var filter = false
        if (mSaturation != 1.0f) {
            this.saturation(mSaturation)
            mColorMatrix.set(m)
            filter = true
        }
        if (mContrast != 1.0f) {
            mTmpColorMatrix.setScale(mContrast, mContrast, mContrast, 1.0f)
            mColorMatrix.postConcat(mTmpColorMatrix)
            filter = true
        }
        if (mWarmth != 1.0f) {
            this.warmth(mWarmth)
            mTmpColorMatrix.set(m)
            mColorMatrix.postConcat(mTmpColorMatrix)
            filter = true
        }
        if (filter) {
            this.colorFilter = ColorMatrixColorFilter(mColorMatrix)
        } else {
            clearColorFilter()
        }
    }
}