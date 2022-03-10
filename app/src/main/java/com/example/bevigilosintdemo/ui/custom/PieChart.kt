package com.example.bevigilosintdemo.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.example.bevigilosintdemo.R
import com.example.bevigilosintdemo.api.model.AssetModel
import com.example.bevigilosintdemo.core.AssetKeyType.getColorForAssetKey
import com.example.bevigilosintdemo.utils.ResourceUtils.getColorResource

class PieChart : View {

    private lateinit var rectF: RectF
    private var parentHeight: Int = 0
    private var parentWidth: Int = 0
    private lateinit var greenPaint:Paint
    private lateinit var redPaint:Paint
    private lateinit var innerPaint: Paint
    var assetAngleList:HashMap<String,Int> = hashMapOf()
    private var isInitialized = false


    constructor(context: Context) : super(context) { init(context) }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) { init(context) }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { init(context) }

    private fun init(context: Context) {}

    fun setupChart(assetModel: AssetModel) {
        assetAngleList.clear()
        var totalAssetComponents = assetModel.getTotalAssetCount()

        assetModel.assets.forEach { asset ->
            val angle = ((assetModel.getAssetCountAt(asset.key).toFloat() / totalAssetComponents.toFloat()) * 360).toInt()
            assetAngleList[asset.key] = angle
        }

        innerPaint = Paint()
        innerPaint.isAntiAlias = true
        innerPaint.color = Color.WHITE
        innerPaint.style = Paint.Style.FILL

        isInitialized = true
        invalidate()
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        var currentAngle = 0f
        if(isInitialized) {
            assetAngleList.forEach { angle ->
                canvas?.drawArc(rectF, currentAngle, angle.value.toFloat(), true, getPaintForAsset(angle.key))
                currentAngle += angle.value.toFloat()
            }
        }
        canvas?.drawCircle(parentWidth.toFloat()/2, parentHeight.toFloat()/2, (parentWidth/4).toFloat(), innerPaint)
    }

    private fun getPaintForAsset(key: String): Paint {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.color = getColorForAssetKey(key) ?: Color.BLACK
        paint.style = Paint.Style.FILL
        return paint
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (widthMeasureSpec < heightMeasureSpec)
            super.onMeasure(heightMeasureSpec, heightMeasureSpec)
        else
            super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }

    override fun onSizeChanged(xNew: Int, yNew: Int, xOld: Int, yOld: Int) {
        super.onSizeChanged(xNew, yNew, xOld, yOld)
        parentWidth = xNew
        parentHeight = yNew

        rectF = RectF(0f,0f, parentWidth.toFloat(), parentHeight.toFloat())
    }
}
