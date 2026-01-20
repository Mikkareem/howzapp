package com.techullurgy.howzapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        println("onNewIntent")
        handleIntent(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        handleIntent(intent)

        setContent {
            App()
        }
    }

    private fun handleIntent(intent: Intent?) {
        if (intent?.action == Intent.ACTION_SEND) {
            val intentText = intent.extras?.getString(Intent.EXTRA_TEXT)
            val intentSubject = intent.extras?.getString(Intent.EXTRA_SUBJECT)
            val intentTitle = intent.extras?.getString(Intent.EXTRA_TITLE)

            println(intentText)
            println(intentSubject)
            println(intentTitle)

            if (intentText != null && intentText.contains("maps.app.goo.gl")) {
                // Google Maps Location (Shared)
                // https://maps.app.goo.gl/UDVx4oymnF6qSWvv6
                // Expand Url (FollowRedirect: false, 301/302, Original Url = Header.Location) using Ktor in IO dispatcher
                // val regex1 = Regex("@(-?\\d+\\.\\d+),(-?\\d+\\.\\d+)")
                // val regex2 = Regex("!3d(-?\\d+(?:\\.\\d+)?)!4d(-?\\d+(?:\\.\\d+)?)") -> Prefer this first
                // val latitude = regex.groupValues[1].toDouble()
                // val longitude = regex.groupValues[2].toDouble()
                handleGoogleMapShare(intentText)
            }
        }
    }

    private fun handleGoogleMapShare(url: String) {
        val client = HttpClient {
            followRedirects = false
        }

        lifecycleScope.launch {
            client.get(url).run {
                headers["Location"]?.let { location ->
//                    val regex = Regex("@(-?\\d+\\.\\d+),(-?\\d+\\.\\d+)")
                    val regex = Regex("!3d(-?\\d+(?:\\.\\d+)?)!4d(-?\\d+(?:\\.\\d+)?)")
                    val matchResult = regex.find(location)
                    val latitude = matchResult?.groupValues[1]?.toDouble()
                    val longitude = matchResult?.groupValues[2]?.toDouble()

                    println("Latitude: $latitude, Longitude: $longitude")
                }
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}