package com.akadev.hyeonmin.eq_sys_android.NaverMapTool

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.widget.ImageView
import android.widget.ListView
import com.akadev.hyeonmin.eq_sys_android.R
import com.nhn.android.maps.NMapOverlayItem
import com.nhn.android.maps.overlay.NMapPOIitem
import com.nhn.android.mapviewer.overlay.NMapResourceProvider

class ResourceProvider(context: Context): NMapResourceProvider(context), NMapCalloutCustomOldOverlay.ResourceProvider {

    fun getDrawable(markerId: Int, focused: Boolean, item: NMapOverlayItem): Drawable? {
        var marker: Drawable? = null

        var resourceId = findResourceIdForMarker(markerId, focused)
        if (resourceId > 0) {
//            marker = mContext.resources.getDrawable(resourceId)
            marker = ContextCompat.getDrawable(mContext, resourceId)
        } else {
            resourceId = 4 * markerId
            if (focused) {
                resourceId += 1
            }

            marker = getDrawableForMarker(markerId, focused, item)
        }

        // set bounds
        if (marker != null) {
            setBounds(marker, markerId, item)
        }

        return marker
    }

    fun getBitmap(marker: Drawable?): Bitmap {
        var bitmap: Bitmap? = null

        if (marker != null) {
            val width = marker.intrinsicWidth
            val height = marker.intrinsicHeight
            bitmap = Bitmap.createBitmap(width, height, BITMAP_CONFIG_DEFAULT)

            marker.setBounds(0, 0, width, height)

            val canvas = Canvas(bitmap!!)
            canvas.drawColor(0x00000000)

            marker.draw(canvas)
        }

        return bitmap!!
    }

    private inner class ResourceIdsOnMap internal constructor(internal var markerId: Int, internal var resourceId: Int, internal var resourceIdFocused: Int)

    // Resource Ids for single icons
    private val mResourceIdsForMarkerOnMap = arrayOf(
            // Spot, Pin icons
            ResourceIdsOnMap(PoiFlag.BRANCH, R.drawable.branch, R.drawable.branch),

            ResourceIdsOnMap(PoiFlag.IC, R.drawable.ic, R.drawable.ic),
            ResourceIdsOnMap(PoiFlag.JC, R.drawable.jc, R.drawable.jc),

            ResourceIdsOnMap(PoiFlag.TM_0, R.drawable.tm_0, R.drawable.tm_0),
            ResourceIdsOnMap(PoiFlag.TM_1, R.drawable.tm_1, R.drawable.tm_1),
            ResourceIdsOnMap(PoiFlag.TM_2, R.drawable.tm_2, R.drawable.tm_2),
            ResourceIdsOnMap(PoiFlag.TM_3, R.drawable.tm_3, R.drawable.tm_3),
            ResourceIdsOnMap(PoiFlag.TM_4, R.drawable.tm_4, R.drawable.tm_4),
            ResourceIdsOnMap(PoiFlag.TM_5, R.drawable.tm_5, R.drawable.tm_5),
            ResourceIdsOnMap(PoiFlag.TM_6, R.drawable.tm_6, R.drawable.tm_6),
            ResourceIdsOnMap(PoiFlag.TM_7, R.drawable.tm_7, R.drawable.tm_7),
            ResourceIdsOnMap(PoiFlag.TM_8, R.drawable.tm_8, R.drawable.tm_8)

    )

    override fun findResourceIdForMarker(markerId: Int, focused: Boolean): Int {
        var resourceId = 0

        if (DEBUG) {
//			Log.i(LOG_TAG, "getResourceIdForMarker: markerId=$markerId, focused=$focused")
        }

        resourceId = getResourceIdOnMapView(markerId, focused, mResourceIdsForMarkerOnMap)
        if (resourceId > 0) {
            return resourceId
        }

        return resourceId
    }

    private fun getResourceIdOnMapView(markerId: Int, focused: Boolean, resourceIdsArray: Array<ResourceIdsOnMap>): Int {
        var resourceId = 0

        for (resourceIds in resourceIdsArray) {
            if (resourceIds.markerId == markerId) {
                resourceId = if (focused) resourceIds.resourceIdFocused else resourceIds.resourceId
                break
            }
        }

        return resourceId
    }

    override fun getCalloutRightButtonText(item: NMapOverlayItem): String {
        if (item is NMapPOIitem) {

            if (item.showRightButton()) {
                return "완료"
            }
        }

        return ""
    }

    override fun getCalloutRightButton(item: NMapOverlayItem): Array<Drawable> {
        if (item is NMapPOIitem) {

            if (item.showRightButton()) {
                val drawable = arrayOfNulls<Drawable>(3)

                drawable[0] = ContextCompat.getDrawable(mContext, R.drawable.btn_green_normal)
                drawable[1] = ContextCompat.getDrawable(mContext, R.drawable.btn_green_pressed)
                drawable[2] = ContextCompat.getDrawable(mContext, R.drawable.btn_green_highlight)

                return drawable as Array<Drawable>
            }
        }

        return arrayOf()
    }

    override fun getCalloutRightAccessory(item: NMapOverlayItem): Array<Drawable> {
        if (item is NMapPOIitem) {
            val poiItem = item

            if (poiItem.hasRightAccessory() && poiItem.rightAccessoryId > 0) {
                val drawable = arrayOfNulls<Drawable>(3)

                when (poiItem.rightAccessoryId) {
                    PoiFlag.CLICKABLE_ARROW -> {
                        drawable[0] = ContextCompat.getDrawable(mContext, R.drawable.pin_ballon_arrow)
                        drawable[1] = ContextCompat.getDrawable(mContext, R.drawable.pin_ballon_on_arrow)
                        drawable[2] = ContextCompat.getDrawable(mContext, R.drawable.pin_ballon_on_arrow)
                    }
                }

                return drawable as Array<Drawable>
            }
        }

        return arrayOf()
    }

    override fun getCalloutBackground(item: NMapOverlayItem): Drawable {
        if (item is NMapPOIitem) {

            if (item.showRightButton()) {
                val drawable = mContext.resources.getDrawable(R.drawable.bg_speech)
                return drawable
            }
        }

        val drawable = ContextCompat.getDrawable(mContext, R.drawable.pin_ballon_bg) as Drawable

        return drawable
    }



    private val mTempRect = Rect()
    private val mTextPaint = Paint()

    init {

        mTextPaint.isAntiAlias = true
    }

    override fun getOverlappedListViewId(): Int {
        return 0
    }

    override fun setOverlappedListViewLayout(p0: ListView?, p1: Int, p2: Int, p3: Int) {
    }

    override fun setOverlappedItemResource(p0: NMapPOIitem?, p1: ImageView?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLocationDot(): Array<Drawable> {
        val drawable = arrayOfNulls<Drawable>(2)

        drawable[0] = ContextCompat.getDrawable(mContext, R.drawable.pubtrans_ic_mylocation_off)
        drawable[1] = ContextCompat.getDrawable(mContext, R.drawable.pubtrans_ic_mylocation_on)

        for (i in drawable.indices) {
            val w = drawable[i]!!.intrinsicWidth / 2
            val h = drawable[i]!!.intrinsicHeight / 2

            drawable[i]!!.setBounds(-w, -h, w, h)
        }

        return drawable as Array<Drawable>
    }

    override fun getDrawableForMarker(markerId: Int, focused: Boolean, item: NMapOverlayItem?): Drawable {
        var drawable: Drawable? = null

        val resourceId = if (focused) R.drawable.ic_map_no_02 else R.drawable.ic_map_no_01
        val fontColor = if (focused) POI_FONT_COLOR_ALPHABET else POI_FONT_COLOR_NUMBER

        val strNumber = (markerId - PoiFlag.NUMBER_BASE).toString()

        drawable = getDrawableWithNumber(resourceId, strNumber, 0.0f, fontColor, POI_FONT_SIZE_NUMBER)

        return drawable!!
    }

    fun getDrawableWithNumber(resourceId: Int, strNumber: String, offsetY: Float, fontColor: Int, fontSize: Float): Drawable? {

        val textBitmap = getBitmapWithText(resourceId, strNumber, fontColor, fontSize, offsetY)

        //Log.i(LOG_TAG, "getDrawableWithNumber: width=" + textBitmap.getWidth() + ", height=" + textBitmap.getHeight() + ", density=" + textBitmap.getDensity());

        // set bounds
        val marker = BitmapDrawable(mContext.resources, textBitmap)
        if (marker != null) {
            NMapOverlayItem.boundCenter(marker)
        }

        //Log.i(LOG_TAG, "getDrawableWithNumber: width=" + marker.getIntrinsicWidth() + ", height=" + marker.getIntrinsicHeight());

        return marker
    }


    private fun getBitmapWithText(resourceId: Int, strNumber: String, fontColor: Int, fontSize: Float, offsetY: Float): Bitmap {
        var offsetY = offsetY
        val bitmapBackground = BitmapFactory.decodeResource(mContext.resources, resourceId)

        val width = bitmapBackground.width
        val height = bitmapBackground.height
        //Log.i(LOG_TAG, "getBitmapWithText: width=" + width + ", height=" + height + ", density=" + bitmapBackground.getDensity());

        val textBitmap = Bitmap.createBitmap(width, height, BITMAP_CONFIG_DEFAULT)

        val canvas = Canvas(textBitmap)

        canvas.drawBitmap(bitmapBackground, 0f, 0f, null)

        // set font style
        mTextPaint.color = fontColor
        // set font size
        mTextPaint.textSize = fontSize * NMapResourceProvider.mScaleFactor
        // set font type
        if (POI_FONT_TYPEFACE != null) {
            mTextPaint.typeface = POI_FONT_TYPEFACE
        }

        // get text offset
        mTextPaint.getTextBounds(strNumber, 0, strNumber.length, mTempRect)
        val offsetX = ((width - mTempRect.width()) / 2 - mTempRect.left).toFloat()
        if (offsetY == 0.0f) {
            offsetY = ((height - mTempRect.height()) / 2 + mTempRect.height()).toFloat()
        } else {
            offsetY = offsetY * NMapResourceProvider.mScaleFactor + mTempRect.height()
        }

        //Log.i(LOG_TAG, "getBitmapWithText: number=" + number + ", focused=" + focused);
        //Log.i(LOG_TAG, "getBitmapWithText: offsetX=" + offsetX + ", offsetY=" + offsetY + ", boundsWidth=" + mTempRect.width() + ", boundsHeight=" + mTempRect.height());

        // draw text
        canvas.drawText(strNumber, offsetX, offsetY, mTextPaint)

        return textBitmap
    }

    override fun getParentLayoutIdForOverlappedListView(): Int {
        return 0
    }

    override fun getListItemDividerId(): Int {
        return 0
    }

    override fun getListItemImageViewId(): Int {
        return 0
    }


    override fun getCalloutTextColors(p0: NMapOverlayItem?): IntArray {
        val colors = IntArray(4)
        colors[0] = CALLOUT_TEXT_COLOR_NORMAL
        colors[1] = CALLOUT_TEXT_COLOR_PRESSED
        colors[2] = CALLOUT_TEXT_COLOR_SELECTED
        colors[3] = CALLOUT_TEXT_COLOR_FOCUSED
        return colors
    }

    override fun getListItemTextViewId(): Int {
        return 0
    }

    override fun getListItemLayoutIdForOverlappedListView(): Int {
        return 0
    }

    override fun getListItemTailTextViewId(): Int {
        return 0
    }

    override fun getLayoutIdForOverlappedListView(): Int {
        return 0
    }

    override fun getDirectionArrow(): Drawable {
        val drawable = mContext.resources.getDrawable(R.drawable.ic_angle)

        if (drawable != null) {
            val w = drawable.intrinsicWidth / 2
            val h = drawable.intrinsicHeight / 2

            drawable.setBounds(-w, -h, w, h)
        }

        return drawable
    }

    companion object {
        private val LOG_TAG = "NMapViewerResourceProvider"
        private val DEBUG = false

        private val BITMAP_CONFIG_DEFAULT = Bitmap.Config.ARGB_8888

        private val POI_FONT_COLOR_NUMBER = 0xFF909090.toInt()
        private val POI_FONT_SIZE_NUMBER = 10.0f

        private val POI_FONT_COLOR_ALPHABET = 0xFFFFFFFF.toInt()
        private val POI_FONT_OFFSET_ALPHABET = 6.0f
        private val POI_FONT_TYPEFACE: Typeface? = null//Typeface.DEFAULT_BOLD;

        private val CALLOUT_TEXT_COLOR_NORMAL = 0xFFFFFFFF.toInt()
        private val CALLOUT_TEXT_COLOR_PRESSED = 0xFF9CA1AA.toInt()
        private val CALLOUT_TEXT_COLOR_SELECTED = 0xFFFFFFFF.toInt()
        private val CALLOUT_TEXT_COLOR_FOCUSED = 0xFFFFFFFF.toInt()
    }
}