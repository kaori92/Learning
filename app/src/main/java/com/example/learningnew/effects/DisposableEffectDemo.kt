package com.example.learningnew.effects

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun DisposableEffectDemo() {
	val lifecycleOwner = LocalLifecycleOwner.current
	
	DisposableEffect(key1 = lifecycleOwner) {
		val observer = LifecycleEventObserver { _, event ->
			if (event == Lifecycle.Event.ON_PAUSE) {
				println("On pause called")
			}
		}
		lifecycleOwner.lifecycle.addObserver(observer)
		
		onDispose {
			lifecycleOwner.lifecycle.removeObserver(observer)
		}
	}
}