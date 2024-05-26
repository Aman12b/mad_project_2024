package com.example.mad_project

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object: TypeToken<T>() {}.type)

inline fun <reified T> Gson.toJson(obj: T): String = this.toJson(obj, object: TypeToken<T>() {}.type)