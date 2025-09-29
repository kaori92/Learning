package com.example.learningnew.contactsroom

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(indices = [
    Index(value = ["first_name"], unique = true)
])
data class Contact(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    
    @ColumnInfo(name = "first_name")
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val createdAt: LocalDateTime,
    val gender: Gender
)

enum class Gender {
    MALE,
    FEMALE,
    NOT_PROVIDED
}