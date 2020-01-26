package com.aman.findjob.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.aman.findjob.R
import com.aman.findjob.extension.createFactory
import com.aman.findjob.extension.gone
import com.aman.findjob.extension.visible
import com.aman.findjob.repo.FormRepoI
import com.aman.findjob.room.entity.Form
import com.aman.findjob.ui.adapter.FormAdapter
import com.aman.findjob.ui.newForm.FormState
import com.aman.findjob.ui.newForm.FormViewModel
import com.aman.findjob.ui.newForm.NewFormFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : DaggerAppCompatActivity(), NewFormFragment.OnFragmentInteractionListener {

    @Inject
    lateinit var repo: FormRepoI

    private lateinit var viewModel: FormViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
        setToolbar()
        loadAllForms()
        setObserver()
    }

    private fun init() {
        Log.d(TAG, " >>> Initializing viewModel")

        val factory = FormViewModel(repo).createFactory()
        viewModel = ViewModelProvider(this, factory).get(FormViewModel::class.java)
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar)
        toolbar.title = CLASS_SIMPLE_NAME
        toolbar.navigationIcon = null

    }

    private fun loadAllForms() {
        viewModel.getAllForms()
    }

    private fun deleteForm(form: Form) {
        viewModel.deleteForm(form)
        loadAllForms()
    }

    private fun setObserver() {
        viewModel.stateObservable.observe(this, Observer {
            updateView(it)
        })
    }

    private fun updateView(state: FormState) {
        if (state.eventType == FormState.EventType.FETCH) {
            when {
                state.loading -> showLoading()
                state.success -> setFormsRecyclerView(state.data)
                state.failure -> showError(state.message)
            }
        }
    }

    private fun showLoading() {
        progressBar.visible()
        tv_message_response.gone()
    }

    private fun setFormsRecyclerView(data: List<Form>?) {
        progressBar.gone()
        tv_message_response.gone()
        rv_form_list.layoutManager = LinearLayoutManager(this)
        rv_form_list.adapter = data?.let {
            FormAdapter(it, object : FormAdapter.OnRecyclerViewClickListener{
                override fun onMoreButtonClick(form: Form) {
                    showBottomSheetDialog(form)
                }

                override fun onInviteButtonClick(form: Form) {
                    // Use onInviteButtonClick callback when invite button clicks
                }

                override fun onInboxButtonClick(form: Form) {
                    // Use onInboxButtonClick callback when inbox button clicks
                }
            })
        }
    }

    private fun showBottomSheetDialog(form: Form) {
        val view = layoutInflater.inflate(R.layout.layout_bottom_sheet, null)

        val dialog = BottomSheetDialog(this)
        dialog.setContentView(view)
        dialog.show()

        view.setOnClickListener {
            deleteForm(form)
            dialog.dismiss()
        }
    }

    private fun showError(message: String?) {
        progressBar.gone()
        tv_message_response.visible()
        tv_message_response.text = message
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)

        val addItem = menu!!.findItem(R.id.menu_add)
        val sendItem = menu.findItem(R.id.menu_send)
        addItem.isVisible = true
        sendItem.isVisible = false

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_add -> {
                openNewFormScreen()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openNewFormScreen() {
        Log.d(TAG, " >>> Opening New From Screen")
        rv_form_list.gone()
        tv_message_response.gone()
        val instance = NewFormFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, instance, NewFormFragment.CLASS_SIMPLE_NAME)
            .addToBackStack(NewFormFragment.CLASS_SIMPLE_NAME)
            .commit()
    }

    override fun onBackPressed() {
        Log.d(TAG, " >>> Back to the main Screen")
        val count = supportFragmentManager.backStackEntryCount
        if (count==1) {
            supportFragmentManager.popBackStack()
            setToolbar()
            loadAllForms()
            rv_form_list.visible()
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val CLASS_SIMPLE_NAME = "Form Listing"
    }

}
