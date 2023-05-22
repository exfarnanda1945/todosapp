package com.example.todosapp.common

import android.app.AlertDialog
import android.content.Context
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.example.todosapp.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utils {
    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("dd.MMMM.yyyy", Locale.getDefault())
        return format.format(date)
    }

    fun createAlertDialog(
        context: Context,
        @StringRes title: Int,
        @StringRes message: Int,
        onPositive: () -> Unit,
        onNegative: () -> Unit,
        @ColorRes primaryColor: Int
    ) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.apply {
            setTitle(context.getString(title))
            setMessage(context.getString(message))
            setNegativeButton(context.getString(R.string.no)) { _, _ -> onNegative() }
            setPositiveButton(context.getString(R.string.yes)) { _, _ ->
                onPositive()
            }

            val dialog = alertDialogBuilder.create()
            dialog.show()

            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).apply {
                setTextColor(
                    ContextCompat.getColor(
                        context,
                        primaryColor
                    )
                )
            }
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).apply {
                setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        primaryColor
                    )
                )
                setTextColor(ContextCompat.getColor(context, R.color.white))
            }
        }

    }

    fun createSpanString(str: String, color: Int): SpannableString {
        val spanString = SpannableString(str)
        spanString.setSpan(ForegroundColorSpan(color), 0, spanString.length, 0)

        return spanString

    }

}