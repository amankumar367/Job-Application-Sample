package com.aman.findjob.ui.newForm

import com.aman.findjob.room.entity.Form

data class FormState(
    var loading: Boolean = false,
    var success: Boolean = false,
    var failure: Boolean = false,
    var message: String? = null,
    var data: List<Form>? = null,
    var eventType: EventType? = null
) {
    enum class EventType { ADD, DELETE, FETCH }
}