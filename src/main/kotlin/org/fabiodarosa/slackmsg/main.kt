package org.fabiodarosa.slackmsg

import org.fabiodarosa.slackmsg.client.SlackClient
import org.fabiodarosa.slackmsg.dsl.slackMessage

fun main() {

    val slackClient = SlackClient()
    val myMessage = slackMessage {

        text {
            + "_italic_"
            + "*bold*"
            + "~strike~"
        }
        markDown{
            + "_italic_"
            + "*bold*"
            + "~strike~"
        }

        json {
             """{"id": "123"}"""
        }
    }

    slackClient.sendMessage(message = myMessage)
}
