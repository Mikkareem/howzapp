package com.techullurgy.howzapp.uploads.services

import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.cloud.storage.Blob
import com.google.cloud.storage.HttpMethod
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File
import java.util.concurrent.TimeUnit

@Service
class GCSUploaderService(
    @param:Value($$"${application.gcp.storage.credentials.path}")
    private val credentialsPath: String
) {
    fun getSignedUrl(): String {
        val blobInfo = Blob.newBuilder("howzapp-test", "profile_pictures/profile.png")
            .setContentType("application/octet-stream")
            .build()

        val storage = StorageOptions.newBuilder()
            .setCredentials(
                ServiceAccountCredentials.fromStream(
                    File(credentialsPath).inputStream()
                )
            )
            .build()
            .service

        val url = storage.signUrl(
            blobInfo,
            3,
            TimeUnit.MINUTES,
            Storage.SignUrlOption.httpMethod(HttpMethod.PUT),
            Storage.SignUrlOption.withV4Signature(),
            Storage.SignUrlOption.withContentType()
        )

        return url.toString()
    }
}