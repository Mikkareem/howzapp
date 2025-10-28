package com.techullurgy.howzapp.feature.chat.data.system

import com.techullurgy.howzapp.feature.chat.domain.system.FileReader

actual class FileReaderImpl : FileReader {
    actual override suspend fun getFileBytesFromUrl(filePath: String): ByteArray {
        TODO("Not yet implemented")
    }
}