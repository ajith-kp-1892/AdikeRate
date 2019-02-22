package com.ajithkp.application.appdatabase

import android.arch.persistence.room.TypeConverter
import java.util.Date

/**
 * Created by kpajith on 24-08-2018 on 11:35.
 */

class DateConverter {

    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return if (timestamp == null) null else Date(timestamp)
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return date?.time
    }
}
