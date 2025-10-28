package com.example.learningnew.effects

import androidx.compose.animation.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun LaunchedEffectScreen() {
	var counter by remember {
		mutableStateOf(0)
	}
	val animatable = remember {
		Animatable(Color.Gray)
	}
	
	LaunchedEffect(key1 = counter) {
		val randomColor = Color(
			red = Random.nextFloat(),
			green = Random.nextFloat(),
			blue = Random.nextFloat()
		)
		animatable.animateTo(randomColor)
	}
	
	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(16.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center
	) {
		Box(
			modifier = Modifier
				.size(200.dp)
				.background(animatable.value)
		) {
			Button(
				onClick = {
					counter++
				}
			) {
				Text("Change color")
			}
		}
	}
}