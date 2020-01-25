package com.aman.findjob.repo

import com.aman.findjob.room.database.AppDatabase
import com.aman.findjob.room.entity.Form
import io.reactivex.Single

class FormRepo(private val db: AppDatabase): FormRepoI {

    override fun addForm(form: Form): Single<String> {
        return Single.create<String> { emitter ->
            try {
                db.formDao().addForm(form).let {
                    emitter.onSuccess("Form added successfully")
                }
            } catch (exception: Exception) {
                emitter.onError(exception)
            }
        }
    }

    override fun deleteForm(form: Form): Single<String> {
        return Single.create { emitter ->
            try {
                db.formDao().deleteForm(form).let {
                    emitter.onSuccess("Form deleted Successfully")
                }
            } catch (exception: Exception) {
                emitter.onError(exception)
            }
        }
    }

    override fun getAllForm(): Single<List<Form>> {
        return Single.create { emitter ->
            try {
                db.formDao().getAllTaskList().let {
                    emitter.onSuccess(it)
                }
            } catch (exception: Exception) {
                emitter.onError(exception)
            }
        }
    }
}