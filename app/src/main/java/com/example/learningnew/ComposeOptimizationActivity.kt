package com.example.learningnew

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.learningnew.contactsroom.ContactList
import com.example.learningnew.contactsroom.ContactListState
import com.example.learningnew.contactsroom.ContactScreen
import com.example.learningnew.ui.theme.LearningNewTheme

class ComposeOptimizationActivity: ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			LearningNewTheme {
				var selected by remember { mutableStateOf(false)}
				Column {
					Checkbox(
						checked = selected,
						onCheckedChange = { selected = it }
					)
					ContactList(
						state = ContactListState(
							isLoading = false,
							names = listOf("Peter")
						)
					)
				}
			}
		}
	}
}