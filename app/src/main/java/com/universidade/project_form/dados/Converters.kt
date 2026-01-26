package com.universidade.project_form.dados

import androidx.room.TypeConverter
import java.util.Date

class Converters {

    // Converte Date para Long (timestamp)
    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

    // Converte Long (timestamp) para Date
    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }
}
