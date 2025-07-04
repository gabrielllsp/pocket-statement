package com.gabriel.pocketstatement.data.local

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import com.gabriel.pocketstatement.domain.model.ReceiptItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDate

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromReceiptItemList(items: List<ReceiptItem>): String {
        return gson.toJson(items)
    }

    @TypeConverter
    fun toReceiptItemList(json: String): List<ReceiptItem> {
        val type = object : TypeToken<List<ReceiptItem>>() {}.type
        return gson.fromJson(json, type)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun fromLocalDate(date: LocalDate): Long {
        return date.toEpochDay()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toLocalDate(epochDay: Long): LocalDate {
        return LocalDate.ofEpochDay(epochDay)
    }
}
