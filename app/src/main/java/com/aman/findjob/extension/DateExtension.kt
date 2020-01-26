package com.aman.findjob.extension

import java.util.*

val currentDate: Long
    get() {
        return Calendar.getInstance().time.time
    }