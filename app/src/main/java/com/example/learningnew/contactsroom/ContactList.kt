package com.example.learningnew.contactsroom

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment

@Composable
fun ContactList(
	state: ContactListState,
) {
	Box(
		contentAlignment = Alignment.Center
	) {
		if (state.isLoading) {
			CircularProgressIndicator()
		} else {
			Text(state.names.toString())
		}
	}
}

// promises by the programmer, not enforced
//@Stable // more flexible, weaker, could contain mutable fields
// usually very minor improvements
@Immutable
data class ContactListState(
	val isLoading: Boolean,
	val names: List<String> // or use ImmutableList
)

// @Immutable means the properties
// can never change once initialized.
@Immutable
data class Profile(
	val name: String,
	val age: Int
)

//@Stable means the properties do not change unexpectedly,
// but they can change if explicitly updated.
@Stable
class UserSettings {
	var theme: String = "Light"
		private set
	
	fun changeTheme(newTheme: String) {
		theme = newTheme
	}
}