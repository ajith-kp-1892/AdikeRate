package com.ajithkp.application.util;

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spanned
import java.util.*

/**
 * Created by kpajith on 28-08-2018 on 15:32.
 */

class Util(private val context: Context) {

    private var defaultLanguage = "ENGLISH"
    private var property: Properties? = null

    init {
        val reader = PropertyReader.getInstance()
        property = reader.getProperties("Kannada_langauage.properties", context)
    }

    fun getLangaueKeyValue(key: String): Spanned {
        if (defaultLanguage.equals("KANNADA")) {
            key.replace(oldChar = ' ', newChar = '_', ignoreCase = true)
            if (property!!.containsKey(key)) {
                return fromHtml(property!!.getProperty(key))
            } else {
                return fromHtml(key.replace(oldChar = '_', newChar = ' ', ignoreCase = true))
            }
        } else {
            return fromHtml(key)
        }
    }


    fun fromHtml(html: String): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(String(html.toByteArray()))
        } else {
            Html.fromHtml(html)
        }
    }


}
