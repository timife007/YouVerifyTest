package com.timife.youverifytest.data.local.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.timife.youverifytest.domain.model.Rating

class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromRating(rating: Rating?):String {
        return gson.toJson(rating)
    }

    @TypeConverter
    fun toRating(ratingString: String?): Rating? {
        if (ratingString == null) {
            return Rating(0.0, 0)
        }
        val ratingType = object : TypeToken<Rating?>() {}.type
        return gson.fromJson(ratingString, ratingType)
    }
}