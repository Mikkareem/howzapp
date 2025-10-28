package com.techullurgy.howzapp.feature.chat.data.system

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import com.techullurgy.howzapp.feature.chat.domain.system.FileReader
import kotlinx.io.IOException

actual class FileReaderImpl(
    private val context: Context
) : FileReader {
    actual override suspend fun getFileBytesFromUrl(filePath: String): ByteArray {

        @SuppressLint("UseKtx")
        val contentUri = Uri.parse(filePath)

        return context.contentResolver.openInputStream(contentUri)?.use { it.readBytes() } ?: throw IOException("File not found")
    }
}
