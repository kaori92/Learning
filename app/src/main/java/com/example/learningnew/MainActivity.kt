package com.example.learningnew

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learningnew.flows.FlowsViewModel
import com.example.learningnew.flows.flowsDemo

class MainActivity : ComponentActivity() {
	
	private lateinit var viewModel: FlowsViewModel
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		
		viewModel = ViewModelProvider(this).get(FlowsViewModel::class.java)
		
		flowsDemo()
		
		setContent {
			val snackbarHostState = remember { SnackbarHostState() }
			
			LaunchedEffect(Unit) {
				viewModel.eventFlow.collect { event ->
					when(event) {
						is FlowsViewModel.MyEvent.ErrorEvent -> {
							snackbarHostState.showSnackbar(event.message)
						}
					}
				}
			}
			
			Scaffold(
				snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
			) { contentPadding ->
				Text(
					modifier = Modifier.padding(contentPadding),
					text = viewModel.numberString
				)
				Button(onClick = viewModel::triggerEvent) {
					Text("Send error event")
				}
			}
			//LaunchedEffectScreen()
		}
	}
}
