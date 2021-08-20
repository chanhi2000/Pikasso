package com.poole.pikasso

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poole.pikasso.ui.theme.PikassoTheme

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			PikassoTheme {
				Surface(color = MaterialTheme.colors.background) {
					Greeting("Android")
				}
			}
		}
	}
}

@Composable
fun SampleCanvas() {
	Canvas(modifier = Modifier.clipToBounds()
		.fillMaxWidth()
		.height(100.dp)
		.border(1.dp, MaterialTheme.colors.primaryVariant, shape = RoundedCornerShape(4.dp))
		.pointerInput(Unit) {

        }
	) {
		val canvasW = size.width
		val canvasH = size.height

		drawLine(
			start = Offset(x = canvasW, y = 0f),
			end = Offset(x = 0f, y = canvasH),
			color = Color.Blue,
			strokeWidth = 5f
		)
	}
}

@Composable
fun Greeting(name: String) {
	Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
	PikassoTheme {
		Greeting("Android")
	}
}

@Preview("Canvas위에 대각선 그리기", showBackground = true)
@Composable
fun CanvasPreview() {
	PikassoTheme {
		SampleCanvas()
	}
}