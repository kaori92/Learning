package com.example.learningnew.flows

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learningnew.profiles.Post
import com.example.learningnew.profiles.ProfileState
import com.example.learningnew.profiles.User
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class FlowsViewModel : ViewModel() {
	private val isAuthenticated = MutableStateFlow(true)
	private val user = MutableStateFlow<User?>(null)
	private val posts = MutableStateFlow(emptyList<Post>())
	
	private val _profileState = MutableStateFlow<ProfileState?>(null)
	val profileState = _profileState.asStateFlow()
	
	private val flow1 = (1..10).asFlow().onEach { delay(1000L) }
	private val flow2 = (10..20).asFlow().onEach { delay(300L) }
	var numberString by mutableStateOf("")
		private set
	
	private val eventChannel = Channel<MyEvent>()
	val eventFlow = eventChannel.receiveAsFlow()
	
	init { // combine is called when either flow emits
		isAuthenticated.combine(user) { isAuthenticated, user ->
			if(isAuthenticated) user else null
		}.combine(posts) { user, posts ->
			user?.let {
				_profileState.value = _profileState.value?.copy(
					user = user,
					posts = posts
				)
			}
		}
			.launchIn(viewModelScope)
		// equivalent to
//		viewModelScope.launch {
//			user.combine(posts) { user, posts ->
//				user?.let {
//					_profileState.value = _profileState.value?.copy(
//						user = user,
//						posts = posts
//					)
//				}
//			}.collect()
//		}
		
		// waits for emission from flow1 AND flow2
//		flow1.zip(flow2) { number1, number2 ->
//			numberString += "($number1, $number2)\n"
//		}.launchIn(viewModelScope)
		
		// merges into ONE flow, appends values as they come
		merge(flow1, flow2).onEach {
			numberString += "$it\n"
		}.launchIn(viewModelScope)
		
	}
	
	fun triggerEvent() = viewModelScope.launch {
		eventChannel.send(MyEvent.ErrorEvent("Error"))
	}
	
	sealed class MyEvent {
		data class ErrorEvent(val message: String) : MyEvent()
	}
}