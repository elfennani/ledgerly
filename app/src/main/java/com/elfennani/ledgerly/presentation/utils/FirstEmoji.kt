package com.elfennani.ledgerly.presentation.utils

import java.text.BreakIterator
import java.util.Locale

fun String.firstEmojiOrNull(): String? {
    val it = BreakIterator.getCharacterInstance(Locale.getDefault())
    it.setText(this)
    var start = it.first()
    var end = it.next()
    while (end != BreakIterator.DONE) {
        val cluster = this.substring(start, end)
        if (cluster.codePoints()
                .anyMatch { Character.getType(it) == Character.OTHER_SYMBOL.toInt() }
        ) {
            return cluster
        }
        start = end
        end = it.next()
    }
    return null
}

fun String.excludeFirstEmoji(): String {
    val emoji = firstEmojiOrNull() ?: return this
    return if (startsWith(emoji)) drop(emoji.length).trimStart() else this
}