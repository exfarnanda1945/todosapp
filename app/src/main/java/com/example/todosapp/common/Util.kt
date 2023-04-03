package com.example.todosapp.common

import java.text.SimpleDateFormat
import java.util.*

object Utils {
    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("dd.MMMM.yyyy", Locale.getDefault())
        return format.format(date)
    }

}