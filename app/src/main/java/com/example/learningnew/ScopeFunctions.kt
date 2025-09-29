package com.example.learningnew

import android.content.Intent

class ScopeFunctions {
	private var number: Int? = null
	private var i = 0
	
	fun test() {
		// null checks: LET
//		if(number != null) {
//			// here other thread might have set this to null
//			val number2 = number + 1 // Gives: Smart cast to 'Int' is impossible,
//			// because 'number' is a mutable property that could be mutated concurrently.
//		}
		//instead:
		val x = number?.let {
			val number2 = it + 1
			number2 // returns last line
		} ?: 3
		
		//apply: make many operations on an object
		val intent = Intent().apply { // this, we don't need to write it EVERY time
			putExtra("","")
			putExtra("x","y")
			putExtra("a","b")
		} // return Intent with changes
		
		//run: similar to apply
		val intent2 = Intent().run { // this
			putExtra("","")
			putExtra("x","y")
			putExtra("a","b")
			action = ""
		} // returns last line
		
		//with: like run, different signature
		with(Intent()) { //this
		
		}
	}
	
	// also: similar to let
	fun getSquaredI() = (i * i).also {
		i++
	} // does 2 operations on i
	// returns result of i*i, NOT the last line
	
}