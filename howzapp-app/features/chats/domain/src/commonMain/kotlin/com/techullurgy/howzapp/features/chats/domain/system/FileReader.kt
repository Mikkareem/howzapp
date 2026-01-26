package com.techullurgy.howzapp.features.chats.domain.system

interface FileReader {
    suspend fun getFileBytesFromUrl(filePath: String): ByteArray
}