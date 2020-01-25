package com.aman.findjob.extention

import java.util.*

val currentDate: Long
    get() {
        return Calendar.getInstance().time.time
    }