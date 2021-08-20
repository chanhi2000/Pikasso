import org.gradle.api.artifacts.dsl.DependencyHandler

object Dep {
	val androidx = listOf(
		"androidx.core:core-ktx:1.6.0",
    	"androidx.appcompat:appcompat:1.3.1",
		"androidx.lifecycle:lifecycle-runtime-ktx:2.3.1",
    	"androidx.activity:activity-compose:1.3.0-alpha06",
	)

	val material = "com.google.android.material:material:1.4.0"


	val androidxTest = listOf(
		"androidx.test.ext:junit:1.1.3",
    	"androidx.test.espresso:espresso-core:3.4.0",
    	"androidx.compose.ui:ui-test-junit4:${Version.compose}",
	)

	val compose = listOf(
    	"androidx.compose.ui:ui:${Version.compose}",
    	"androidx.compose.material:material:${Version.compose}",
    	"androidx.compose.ui:ui-tooling-preview:${Version.compose}",
	)

	val composeDebug = listOf(
		"androidx.compose.ui:ui-tooling:${Version.compose}"
	)

	val junit = "junit:junit:4.+"
}

//util functions for adding the different type dependencies from build.gradle file
fun DependencyHandler.kapt(list: List<String>) {
	list.forEach { dependency ->
		add("kapt", dependency)
	}
}

fun DependencyHandler.implementation(list: List<String>) {
	list.forEach { dependency ->
		add("implementation", dependency)
	}
}

fun DependencyHandler.androidTestImplementation(list: List<String>) {
	list.forEach { dependency ->
		add("androidTestImplementation", dependency)
	}
}

fun DependencyHandler.testImplementation(list: List<String>) {
	list.forEach { dependency ->
		add("testImplementation", dependency)
	}
}

fun DependencyHandler.debugImplementation(list: List<String>) {
	list.forEach { dependency ->
		add("debugImplementation", dependency)
	}
}
