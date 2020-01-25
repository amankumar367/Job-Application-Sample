package com.aman.findjob.repo

import androidx.lifecycle.LiveData
import com.aman.findjob.room.entity.Form
import io.reactivex.Single

interface FormRepoI {
    fun addForm(form: Form): Single<String>

    fun deleteForm(form: Form): Single<String>

    fun getAllForm(): Single<List<Form>>
}