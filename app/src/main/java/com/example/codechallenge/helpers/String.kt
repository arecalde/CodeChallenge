package com.example.codechallenge.helpers

import android.util.Base64

fun String.encodeData(): String {
    val encoded = Base64.encode(toByteArray(), Base64.DEFAULT)
    return String(encoded)
}

fun String.decodeData(): String {
    val decoded = Base64.decode(toByteArray(), Base64.DEFAULT)
    return String(decoded)
}