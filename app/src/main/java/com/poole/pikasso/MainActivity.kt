package com.poole.pikasso

import android.graphics.Bitmap
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar


class MainActivity : AppCompatActivity() {
	private val pikassoView: PikassoView by lazy { find(R.id.pikassoView) }
	private val toolbar: Toolbar by lazy { find(R.id.toolbarMain) }
	private val dialogWidthChange: AlertDialog by lazy { AlertDialog.Builder(this).setView(dlWidth).create() }
	private val dlWidth: View by lazy { layoutInflater.inflate(R.layout.dialog_width, null) }
	private val ivEdit: ImageView by lazy { dlWidth.find(R.id.ivEdit)}
	private val btnLineWidthSet: Button by lazy { dlWidth.find(R.id.btnLineWidthSet) }
	private val sbLineWidth: SeekBar by lazy { dlWidth.find(R.id.sbLineWidth) }

	private val dialogColorChange: AlertDialog by lazy { AlertDialog.Builder(this).setTitle("Choose Color").setView(dlColor).create() }
	private val dlColor: View by lazy { layoutInflater.inflate(R.layout.dialog_color, null)}
	private val sbAlpha: SeekBar by lazy { dlColor.find(R.id.sbAlpha) }
	private val sbRed: SeekBar by lazy { dlColor.find(R.id.sbRed) }
	private val sbGreen: SeekBar by lazy { dlColor.find(R.id.sbGreen) }
	private val sbBlue: SeekBar by lazy { dlColor.find(R.id.sbBlue) }
	private val colorView: View by lazy { dlColor.find(R.id.colorView) }
	private val btnColorSet: Button by lazy { dlColor.find(R.id.btnColorSet) }


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		setSupportActionBar(toolbar)
//		actionBar?.setDisplayShowCustomEnabled(false)
//		actionBar?.setDisplayShowHomeEnabled(true)
//		actionBar?.displayOptions = ActionBar.DISPLAY_SHOW_HOME
//		setContent {
//			PikassoTheme {
//				Surface(color = MaterialTheme.colors.background) {
//					Greeting("Android")
//				}
//			}
//		}
	}

	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		menuInflater.inflate(R.menu.menu, menu)
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when(item.itemId) {
			R.id.clearId -> {pikassoView.clear()}
			R.id.saveId -> {}
			R.id.colorId -> {showColorDialog()}
			R.id.lineWidthId -> {showLineWidthDialog()}
		}
		return super.onOptionsItemSelected(item)
	}

	fun showColorDialog() {
		dialogColorChange.show()
		btnColorSet.click {
			pikassoView.drawingColor = Color.argb(sbAlpha.progress, sbRed.progress, sbBlue.progress, sbGreen.progress)
			dialogColorChange.dismiss()
		}
		sbAlpha.setOnSeekBarChangeListener(colorSeekBarChangeListener)
		sbRed.setOnSeekBarChangeListener(colorSeekBarChangeListener)
		sbBlue.setOnSeekBarChangeListener(colorSeekBarChangeListener)
		sbGreen.setOnSeekBarChangeListener(colorSeekBarChangeListener)

		with(pikassoView.drawingColor) {
			sbAlpha.progress = Color.alpha(this)
			sbRed.progress = Color.red(this)
			sbBlue.progress = Color.blue(this)
			sbGreen.progress = Color.green(this)
		}
	}

	private var colorSeekBarChangeListener = object : SeekBar.OnSeekBarChangeListener {
		override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
			colorView.setBackgroundColor(Color.argb(sbAlpha.progress, sbRed.progress, sbBlue.progress, sbGreen.progress))
		}
		override fun onStartTrackingTouch(seekBar: SeekBar?) {}
		override fun onStopTrackingTouch(seekBar: SeekBar?) {}
	}

	fun showLineWidthDialog() {
		dialogWidthChange.show()
		btnLineWidthSet.click {
//			toast("Hello")
			pikassoView.lineWidth = sbLineWidth.progress.toFloat()
			dialogWidthChange.dismiss()
		}
		sbLineWidth.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
			val bitmap = Bitmap.createBitmap(400, 100, Bitmap.Config.ARGB_8888)
			val canvas = Canvas(bitmap)

			override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
				val p = Paint().apply {
					color = pikassoView.drawingColor
					strokeCap = Paint.Cap.ROUND
					strokeWidth = progress.toFloat()
				}
				bitmap.eraseColor(Color.WHITE)
				canvas.drawLine(30f, 50f, 370f ,50f, p)
				ivEdit.setImageBitmap(bitmap)
			}
			override fun onStartTrackingTouch(seekBar: SeekBar?) {}
			override fun onStopTrackingTouch(seekBar: SeekBar?) {}
		})
	}
}

