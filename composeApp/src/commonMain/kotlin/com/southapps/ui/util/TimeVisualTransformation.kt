package com.southapps.ui.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class TimeVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text
        val transformedText = buildString {
            for (i in originalText.indices) {
                append(originalText[i])
                if (i == 1 && originalText.length > 2) {
                    append(':')
                }
            }
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (originalText.length <= 2) {
                    return offset
                }
                return if (offset <= 1) {
                    offset
                } else {
                    offset + 1
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (originalText.length <= 2) {
                    return offset
                }

                return if (offset <= 2) {
                    offset
                } else {
                    offset - 1
                }
            }
        }

        return TransformedText(AnnotatedString(transformedText), offsetMapping)
    }
}