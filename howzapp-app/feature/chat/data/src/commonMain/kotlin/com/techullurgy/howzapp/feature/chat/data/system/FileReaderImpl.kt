package com.techullurgy.howzapp.feature.chat.data.system

import com.techullurgy.howzapp.feature.chat.domain.system.FileReader

expect class FileReaderImpl: FileReader {
    override suspend fun getFileBytesFromUrl(filePath: String): ByteArray
}