package com.siheal.usbserialport.utils

import android.content.Context
import android.content.SharedPreferences
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method


/**
 * Created by hd on 2017/8/31 .
 * 
 */            
object PreferenceUtil{

    val FILE_NAME = "share_data"

    fun put(context: Context, key: String, any: Any) {
        val sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        val editor = sp.edit()
        if (any is String) {
            editor.putString(key, any)
        } else if (any is Int) {
            editor.putInt(key, any)
        } else if (any is Boolean) {
            editor.putBoolean(key, any)
        } else if (any is Float) {
            editor.putFloat(key, any)
        } else if (any is Long) {
            editor.putLong(key, any)
        } else {
            editor.putString(key, any.toString())
        }
        SharedPreferencesCompat.apply(editor)
    }

    fun get(context: Context, key: String, defaultObject: Any): Any {
        val sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        if (contains(context, key)) {
            if (defaultObject is String) {
                return sp.getString(key, defaultObject)
            } else if (defaultObject is Int) {
                return sp.getInt(key, defaultObject)
            } else if (defaultObject is Boolean) {
                return sp.getBoolean(key, defaultObject)
            } else if (defaultObject is Float) {
                return sp.getFloat(key, defaultObject)
            } else if (defaultObject is Long) {
                return sp.getLong(key, defaultObject)
            }
        }
        return defaultObject
    }

    fun remove(context: Context, key: String) {
        val sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        val editor = sp.edit()
        if (contains(context, key)) {
            editor.remove(key)
            SharedPreferencesCompat.apply(editor)
        }
    }

    fun clear(context: Context) {
        val sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.clear()
        SharedPreferencesCompat.apply(editor)
    }

    fun contains(context: Context, key: String): Boolean {
        val sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        return sp.contains(key)
    }

    fun getAll(context: Context): Map<String, *> {
        val sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        return sp.all
    }

    private object SharedPreferencesCompat {
        private val sApplyMethod = findApplyMethod()

        private fun findApplyMethod(): Method? {
            try {
                val clz = SharedPreferences.Editor::class.java
                return clz.getMethod("apply")
            } catch (e: NoSuchMethodException) {
            }
            return null
        }

        fun apply(editor: SharedPreferences.Editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor)
                    return
                }
            } catch (e: IllegalArgumentException) {
            } catch (e: IllegalAccessException) {
            } catch (e: InvocationTargetException) {
            }

            editor.commit()
        }
    }

}