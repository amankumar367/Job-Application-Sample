package com.aman.findjob.utils

import android.app.DatePickerDialog
import android.content.Context
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    const val DATE_FORMAT = "dd MMM yyyy"

    fun datePicker(context: Context, listener: OnDateSetListener): DatePickerDialog {

        val calendar = Calendar.getInstance()

        return DatePickerDialog(
            context,
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->

                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                listener.onDateSet(view, calendar.timeInMillis)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    fun format(timeInMillis: Long, format: String): String {

        val sdf = SimpleDateFormat(format, Locale.ENGLISH)
        val instance = Calendar.getInstance()
        instance.timeInMillis = timeInMillis
        return sdf.format(instance.time)
    }

}