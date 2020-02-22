package org.fabiodarosa.slackmsg.client

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.fabiodarosa.slackmsg.dsl.SlackMessage
import org.fabiodarosa.slackmsg.dsl.toJson

fun SlackMessage.toRequestBody(toMediaType: MediaType) = toJson().toRequestBody(toMediaType)

class SlackClient {

    fun sendMessage(message: SlackMessage, channel: String? = null) {
        val client = OkHttpClient()
        val webHookUrl = System.getenv("WEBHOOK_URL")

        val request = Request.Builder()
            .url(webHookUrl)
            .post(message.toRequestBody("application/json".toMediaType()))
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                println("ERROR: " + response.body!!.string())
            } else {
                println("OK: " + response.body!!.string())
            }
        }
    }

}