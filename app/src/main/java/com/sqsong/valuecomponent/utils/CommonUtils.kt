package com.sqsong.valuecomponent.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader

object CommonUtils {

    fun <T> parseAddress(context: Context, jsonName: String, gson: Gson? = null): T {
        var tempGson: Gson? = gson
        if (tempGson == null) {
            tempGson = Gson()
        }
        val type = object : TypeToken<T>() {}.type
        val reader = BufferedReader(InputStreamReader(context.assets.open(jsonName)))
        return tempGson.fromJson(reader, type)
    }

    fun readFileToString(filePath: String): String {
        try {
            val reader = BufferedReader(InputStreamReader(FileInputStream(File(filePath))))
            val buffer = StringBuilder()
            var flag = true
            while (flag) {
                val line = reader.readLine()
                if (line != null) {
                    buffer.append(line).append("\n")
                } else {
                    flag = false
                }
            }
            reader.close()
            return buffer.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }
}