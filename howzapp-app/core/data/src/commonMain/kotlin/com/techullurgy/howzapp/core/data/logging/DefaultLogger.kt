package com.techullurgy.howzapp.core.data.logging

import com.techullurgy.howzapp.core.domain.logging.HowzappLogger

object DefaultLogger: HowzappLogger {
    override fun debug(message: String) {
//        Log.d("HowzappLogger", message)
    }

    override fun error(message: String, throwable: Throwable?) {
//        Log.e("HowzappLogger", message, throwable)
    }

    override fun info(message: String) {
//        Log.i("HowzappLogger", message)
    }

    override fun warn(message: String) {
//        Log.w("HowzappLogger", message)
    }
}