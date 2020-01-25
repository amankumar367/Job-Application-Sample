package com.aman.findjob.ui.newForm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aman.findjob.repo.FormRepoI
import com.aman.findjob.room.entity.Form
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class FormViewModel(private val repo: FormRepoI): ViewModel() {

    var stateObservable: MutableLiveData<FormState> = MutableLiveData()

    private val compositeDisposable = CompositeDisposable()

    var state = FormState()
        set(value) {
            field = value
            publishState(value)
        }

    fun addForm(form: Form) {
        Log.d(TAG, " >>> Receive call for add form: $form")
        state = state.copy(loading = true, success = false, failure = false, message = "Loading . . .", data = null)
        compositeDisposable.add(
            repo.addForm(form)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    state = state.copy(loading = false, success = true, failure = false, message = it)
                }, {
                    state = state.copy(loading = false, success = false, failure = true, message = it.localizedMessage)
                })
        )
    }

    fun deleteForm(form: Form) {
        Log.d(TAG, " >>> Receive call for delete form: $form")
        state = state.copy(loading = true, success = false, failure = false, message = "Loading . . .", data = null)
        compositeDisposable.add(
            repo.deleteForm(form)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    state = state.copy(loading = false, success = true, failure = false, message = it)
                }, {
                    state = state.copy(loading = false, success = false, failure = true, message = it.localizedMessage)
                })
        )
    }

    fun getAllForms() {
        Log.d(TAG, " >>> Receive call for fetching all forms")
        state = state.copy(loading = true, success = false, failure = false, message = "Loading . . .", data = null)
        compositeDisposable.add(
            repo.getAllForm()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    result.isNotEmpty().let {
                        state = state.copy(loading = false, success = true, failure = false, data = result)
                    }.run {
                        state = state.copy(loading = false, success = false, failure = true, message = "List is empty")
                    }
                }, {
                    state = state.copy(loading = false, success = false, failure = true, message = it.localizedMessage)
                })
        )
    }

    private fun publishState(state: FormState) {
        Log.d(TAG," >>> Publish State : $state")
        stateObservable.value = state
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, " >>> Clearing compositeDisposable")
        compositeDisposable.clear()
    }

    companion object {
        const val TAG = "FormViewModel"
    }
}