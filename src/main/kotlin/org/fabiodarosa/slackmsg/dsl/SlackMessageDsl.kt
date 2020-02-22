package org.fabiodarosa.slackmsg.dsl

import com.google.gson.Gson
val json = Gson()

abstract class SlackBlock(
    val type: String
)

class SectionBlock(
    val text: TextField
): SlackBlock("section")

abstract class TextField (
    val type: String,
    val text: String
)

class PlainText(text: String): TextField("plain_text", text)
class MarkDownText(text: String): TextField("mrkdwn", text)
class JsonText(json: String): TextField("mrkdwn", "```$json```")

class SlackMessage(
    val blocks: List<SlackBlock>
)
fun SlackMessage.toJson(): String = json.toJson(this)

class PlainTextBuilder() {
    private var textBuffer: StringBuffer = StringBuffer()
    operator fun String.unaryPlus() {
        if(textBuffer.isNotEmpty()) {
            textBuffer.append("\n")
        }
        textBuffer.append(this)
    }
    fun build() = SectionBlock(PlainText(textBuffer.toString()))
}
class MarkDownTextBuilder() {
    private var textBuffer: StringBuffer = StringBuffer()
    operator fun String.unaryPlus() {
        if(textBuffer.isNotEmpty()) {
            textBuffer.append("\n")
        }
        textBuffer.append(this)
    }
    fun build() = SectionBlock(MarkDownText(textBuffer.toString()))
}

class SlackMessageBuilder {
    private val blocks: MutableList<SlackBlock> = mutableListOf()

    fun text(body: PlainTextBuilder.() -> Unit) = blocks.add(PlainTextBuilder().apply(body).build())
    fun markDown(body: MarkDownTextBuilder.() -> Unit) =  blocks.add(MarkDownTextBuilder().apply(body).build())
    fun json(body: () -> String) = blocks.add(SectionBlock(JsonText(body())))

    fun build() = SlackMessage(blocks)
}

fun slackMessage(body: SlackMessageBuilder.() -> Unit): SlackMessage =
    SlackMessageBuilder().apply(body).build()

