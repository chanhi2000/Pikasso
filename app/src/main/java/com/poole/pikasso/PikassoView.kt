package com.poole.pikasso

import android.content.Context
import android.content.ContextWrapper
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import java.io.*
import kotlin.math.abs

class PikassoView @JvmOverloads constructor(
	val ctx: Context,
	attrs: AttributeSet? = null,
	defStyle: Int = 0
): View(ctx, attrs, defStyle) {

	init {initPikasso()}
	fun initPikasso() {
	}

	private val imageStorePath: String by lazy {
		"${ctx.getExternalFilesDir("imagesPikasso")}".also { pth ->
			File(pth).let {
				if (!it.exists()){
					val mkdir = it.mkdir()
					Log.d("initPikass", "new directory `$mkdir` is created ...")
				}
			}
		}
	}
	private var bitmap: Bitmap? = null
	private var bitmapCanvas: Canvas? = null
	private var paintScreen: Paint = Paint()
	private var paintLine: Paint = Paint().apply {
		isAntiAlias = true
		color = DEFAULT_COLOR_DRAWING
		strokeWidth = DEFAULT_LINE_WIDTH
		style = Paint.Style.STROKE
		strokeCap = Paint.Cap.ROUND
	}
	private val pathMap: MutableMap<Int, Path> = mutableMapOf()
	private val previousPointMap: MutableMap<Int, Point> = mutableMapOf()

	override fun onDraw(canvas: Canvas?) {
		super.onDraw(canvas)
		bitmap?.let { bmp ->
			canvas?.drawBitmap(bmp, 0f, 0f, paintScreen)
		}
		/* draw circle at center of screen*/
//		canvas?.drawCircle(measuredWidth/2f, measuredHeight/2f, 78f, paintLine)

		pathMap.entries.forEach {
			canvas?.drawPath(it.value, paintLine)
		}
	}

	override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
		super.onSizeChanged(w, h, oldw, oldh)
		bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)?.also { bmp ->
			bitmapCanvas = Canvas(bmp)
			bmp.eraseColor(Color.WHITE)
		}
	}

	override fun onTouchEvent(event: MotionEvent?): Boolean {
		event?.let { evt ->
			val action = evt.actionMasked				// event type
			val actionIdx = evt.actionIndex				// point ( finger, mouse, etc ..)

			when(action) {
				MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_UP -> {
					touchStarted(
						evt.getX(actionIdx),
						evt.getY(actionIdx),
						evt.getPointerId(actionIdx)
					)
				}
				MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
					touchEnded(evt.getPointerId(actionIdx))
				}
				else -> {
					touchMoved(evt)
				}
			}
			Log.d("Screen Touched !", "Heyy! ... ${evt.actionMasked}")
		}
		invalidate()
		return true
	}

	private fun touchStarted(x: Float, y: Float, pointerId: Int) {
		Log.d("touchStarted", "function invoked ... ")
		val path: Path = pathMap[pointerId] ?: Path()
		val point: Point = previousPointMap[pointerId] ?: Point()
		path.moveTo(x,y)
		point.x = x.toInt()
		point.y = y.toInt()
		Log.d("touchStarted", "moved at ($x, $y)")
		pathMap[pointerId] = path
		previousPointMap[pointerId] = point
	}


	private fun touchMoved(event: MotionEvent) {
		Log.d("touchMoved", "function invoked ... ")
		(0 until event.pointerCount).forEach { i ->
			val pointerId = event.getPointerId(i)
			val pointerIdx = event.findPointerIndex(pointerId)

			if (pathMap.containsKey(pointerId)) {
				val newX = event.getX(pointerIdx)
				val newY = event.getY(pointerIdx)
				Log.d("touchMoved ...","new x : $newX, new y : $newY")
				pathMap[pointerId]?.let { path ->
					previousPointMap[pointerId]?.also { point ->
						// calculate the path length
						val dX: Float = abs(newX - point.x)
						val dY: Float = abs(newY - point.y)

						if (dX >= TOUCH_TOLERANCE || dY >= TOUCH_TOLERANCE) {
							// move the path to the new location
							path.quadTo(point.x.toFloat(), point.y.toFloat(), (newX + point.x) / 2f, (newY + point.y) / 2f)

							// store the new coordinates
							point.x = newX.toInt()
							point.y = newY.toInt()
						}
					}
				}

			}
		}
	}

	private fun touchEnded(pointerId: Int) {
		Log.d("touchEnded", "function invoked ... ")
		pathMap[pointerId]?.let {
			bitmapCanvas?.drawPath(it, paintLine)
			it.reset()
		}
	}

	fun clear()  {
		pathMap.clear()
		previousPointMap.clear()
		bitmap?.eraseColor(Color.WHITE)
		invalidate()
	}

	fun save() {
		val cw = ContextWrapper(ctx)
		val current = System.currentTimeMillis()
		val fileName = "Pikasso${current}"
		val myPath = File(imageStorePath, "${fileName}.png")
		var fos: FileOutputStream? = null
		CoroutineScope(IO).launch {
			try {
				fos = FileOutputStream(myPath).also {
					bitmap?.compress(Bitmap.CompressFormat.PNG, 100, it)
				}
			} catch (e: FileNotFoundException) {
				e.printStackTrace()
			} finally {
				try {
					fos?.flush()
					fos?.close()
					Log.d("saveImage", imageStorePath)
					CoroutineScope(Main).launch {
						ctx.customToast("✅ Image Saved to ${imageStorePath}", Toast.LENGTH_LONG) {
							setGravity(Gravity.CENTER, this.xOffset / 2, this.yOffset / 2)
						}
					}
				} catch (e: IOException) {
					e.printStackTrace()
				}
			}
		}
	}

	fun load(from: String) {
		try {
			val f = File(from, "profile.png")
			val b = BitmapFactory.decodeStream(FileInputStream(f))
		} catch(e: FileNotFoundException) {
			e.printStackTrace()
		}
	}

	var drawingColor: Int = DEFAULT_COLOR_DRAWING
		set(value) {
			field = value
			paintLine.color = value
		}

	var lineWidth: Float = DEFAULT_LINE_WIDTH
		set(value) {
			field = value
			paintLine.strokeWidth = value

		}



	companion object {
		const val TOUCH_TOLERANCE = 10
		const val DEFAULT_COLOR_DRAWING: Int = Color.BLACK
		const val DEFAULT_LINE_WIDTH: Float = 7f

	}
}