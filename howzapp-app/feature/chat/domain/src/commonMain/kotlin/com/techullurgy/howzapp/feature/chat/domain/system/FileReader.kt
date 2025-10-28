package com.techullurgy.howzapp.feature.chat.domain.system

interface FileReader {
    suspend fun getFileBytesFromUrl(filePath: String): ByteArray
}