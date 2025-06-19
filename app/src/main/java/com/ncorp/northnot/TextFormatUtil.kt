package com.ncorp.northnot


import android.graphics.Typeface
import android.text.Spannable
import android.text.Spanned
import android.text.style.*
import android.widget.EditText

object TextFormatter {

    fun toggleBold(editText: EditText) {
        toggleSpan(editText, StyleSpan(Typeface.BOLD))
    }

    fun toggleItalic(editText: EditText) {
        toggleSpan(editText, StyleSpan(Typeface.ITALIC))
    }

    fun toggleUnderline(editText: EditText) {
        toggleSpan(editText, UnderlineSpan())
    }

    fun toggleBullet(editText: EditText) {
        val text = editText.text
        val start = editText.selectionStart
        val end = editText.selectionEnd

        if (start < 0 || end <= start) return

        val bullet = "• "
        val selectedText = text.subSequence(start, end).toString()
        val lines = selectedText.split("\n")

        val newText = lines.joinToString("\n") { line ->
            if (line.startsWith(bullet)) line.removePrefix(bullet)
            else bullet + line
        }

        text.replace(start, end, newText)
    }




    // Ortak toggle fonksiyonu: aynı span varsa siler, yoksa uygular
    private fun toggleSpan(editText: EditText, span: Any) {
        val start = editText.selectionStart
        val end = editText.selectionEnd

        if (start < 0 || end <= start) return

        val spans = editText.text.getSpans(start, end, span::class.java)
        if (spans.isNotEmpty()) {
            spans.forEach { editText.text.removeSpan(it) }
        } else {
            editText.text.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    fun applyTextColor(editText: EditText, color: Int) {
        val start = editText.selectionStart
        val end = editText.selectionEnd

        if (start == end) {
            editText.setTextColor(color)
        } else {
            val spannable = editText.text as Spannable
            spannable.setSpan(
                ForegroundColorSpan(color),
                start,
                end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }


}



