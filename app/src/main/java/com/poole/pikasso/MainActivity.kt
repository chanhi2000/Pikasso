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
	private val alert: AlertDialog by lazy {
		AlertDialog.Builder(this)
			.setView(dialogLayout)
			.create()
	}
	private val dialogLayout: View by lazy { layoutInflater.inflate(R.layout.dialog_width, null) }
	private val ivEdit: ImageView by lazy { dialogLayout.find(R.id.ivEdit)}
	private val btnSet: Button by lazy { dialogLayout.find(R.id.btnSet) }
	private val sbWidth: SeekBar by lazy { dialogLayout.find(R.id.sbWidth) }

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
			R.id.colorId -> {}
			R.id.lineWidthId -> {showLineWidthDialog()}
		}
		return super.onOptionsItemSelected(item)
	}


	fun showLineWidthDialog() {
		alert.show()
		btnSet.click {
			toast("Hello")
		}
		sbWidth.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
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

