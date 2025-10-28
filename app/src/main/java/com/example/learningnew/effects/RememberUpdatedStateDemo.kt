package com.example.learningnew.effects

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.coroutines.delay
import androidx.compose.runtime.getValue

@Composable
fun RememberUpdatedStateDemo(
	onTimeout: () -> Unit
) { // for example Splash screen
	// it will consider new values to the onTimeout function
	val updatedOnTimeout by rememberUpdatedState(onTimeout)
	
	LaunchedEffect(true) {
		delay(3000L)
		updatedOnTimeout()
	}
	
}