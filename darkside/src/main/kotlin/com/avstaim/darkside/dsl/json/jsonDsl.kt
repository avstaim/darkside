@file:Suppress("unused")

package com.avstaim.darkside.dsl.json

import org.json.JSONArray
import org.json.JSONObject

inline fun jsonObject(init: JSONObject.() -> Unit): JSONObject {
    val jsonObject = JSONObject()
    jsonObject.init()
    return jsonObject
}

inline fun jsonArray(init: JSONArray.() -> Unit): JSONArray {
    val jsonArray = JSONArray()
    jsonArray.init()
    return jsonArray
}

inline fun JSONObject.jsonObject(name: String, init: JSONObject.() -> Unit = { }) {
    val jsonObject = JSONObject()
    jsonObject.init()
    put(name, jsonObject)
}

inline fun JSONArray.jsonObject(init: JSONObject.() -> Unit = { }) {
    val jsonObject = JSONObject()
    jsonObject.init()
    put(jsonObject)
}

inline fun JSONObject.jsonArray(name: String, init: JSONArray.() -> Unit = { }) {
    val jsonArray = JSONArray()
    jsonArray.init()
    put(name, jsonArray)
}

inline fun JSONArray.jsonArray(init: JSONArray.() -> Unit = { }) {
    val jsonArray = JSONArray()
    jsonArray.init()
    put(jsonArray)
}

fun JSONObject.element(name: String, value: String) {
    put(name, value)
}

inline fun JSONObject.element(name: String, value: () -> String) {
    put(name, value())
}

fun JSONArray.element(value: String) {
    put(value)
}

inline fun JSONArray.element(value: () -> String) {
    put(value())
}

fun JSONObject.element(name: String, value: Number) {
    put(name, value)
}

fun JSONArray.element(value: Number) {
    put(value)
}

fun JSONObject.element(name: String, value: Boolean) {
    put(name, value)
}

fun JSONArray.element(value: Boolean) {
    put(value)
}

fun JSONObject.element(name: String, value: JSONObject) {
    put(name, value)
}

fun JSONArray.element(value: JSONObject) {
    put(value)
}

fun JSONObject.element(name: String, value: JSONArray) {
    put(name, value)
}

fun JSONArray.element(value: JSONArray) {
    put(value)
}

fun JSONObject.optionalElement(name: String, value: String?) {
    if (value != null) put(name, value)
}

fun JSONArray.optionalElement(value: String?) {
    if (value != null) put(value)
}
