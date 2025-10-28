package com.example.learningnew.flows

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

fun flowsDemo() {
	// cold flow: every subscribes gets SEPARATE set of emissions
	// callbackFlow is also cold
	// useful for e.x. when listening to user's location
	val flow = flow<Int> { // cold flow, does nothing until sth subscribes
		repeat(10) {
			emit(it)
			println("Emitted $it")
			delay(1000L)
		}
	}
	// this makes it a hot flow:
//		.shareIn( //SHAREDFLOW launches and collects, e.x. snack bar
//			GlobalScope,
//			SharingStarted.Eagerly
//		) // DOES NOT cache, may LOSE emissions (when no one subscribes)
		.stateIn( //STATEFLOW launches and collects
			GlobalScope,
			SharingStarted.Eagerly,
			null // initial value
		) // caches LATEST value, used in UI state
	
	// hot flows get SAME set of emissions
	
//	flow.launchIn(GlobalScope) // this subscribes
//
//	GlobalScope.launch {
//		flow.collect {
//			println("Collected $it")
//		}
//	}
}