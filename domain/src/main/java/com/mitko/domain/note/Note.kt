package com.mitko.domain.note

import java.util.Date

data class Note(
    val id: Int,
    val date: Date,
    val text: String
)