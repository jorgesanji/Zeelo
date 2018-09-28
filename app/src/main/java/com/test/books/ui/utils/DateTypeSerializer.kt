package com.test.books.ui.utils

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.util.*

class DateTypeSerializer : JsonSerializer<Date> {

    override fun serialize(src: Date?, typeOfSrc: Type, context: JsonSerializationContext): JsonElement? {
        return if (src == null) null else JsonPrimitive(src.time)
    }
}