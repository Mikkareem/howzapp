package com.techullurgy.howzapp.uploads.api

import com.techullurgy.howzapp.uploads.services.GCSUploaderService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/file/uploads")
class FileUploadController(
    private val gcsUploader: GCSUploaderService
) {
    @PostMapping
    fun uploadRequest(): String {
        return gcsUploader.getSignedUrl()
    }
}