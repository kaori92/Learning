package com.example.learningnew.contactsroom

import androidx.room.TypeConverter

class GenderConverter {
	
	@TypeConverter
	fun genderToInt(gender: Gender): Int {
		return when(gender) {
			Gender.NOT_PROVIDED -> 0
			Gender.MALE -> 1
			Gender.FEMALE -> 2
		}
	}
	
	@TypeConverter
	fun intToGender(int: Int): Gender {
		return when(int) {
			0 -> Gender.NOT_PROVIDED
			1 -> Gender.MALE
			2 -> Gender.FEMALE
			else -> Gender.NOT_PROVIDED
		}
	}
}