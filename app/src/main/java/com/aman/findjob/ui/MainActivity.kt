package com.aman.findjob.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.aman.findjob.R
import com.aman.findjob.extention.createFactory
import com.aman.findjob.extention.gone
import com.aman.findjob.extention.visible
import com.aman.findjob.repo.FormRepoI
import com.aman.findjob.room.entity.Form
import com.aman.findjob.ui.adapter.FormAdapter
import com.aman.findjob.ui.newForm.FormState
import com.aman.findjob.ui.newForm.FormViewModel
import com.aman.findjob.ui.newForm.NewFormFragment
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

    private fun setObserver() {
        viewModel.stateObservable.observe(this, Observer {
            updateView(it)
        })
    }

    private fun updateView(state: FormState) {
        when {
            state.loading -> showLoading()
            state.success -> setFormsRecyclerView(state.data)
            state.failure -> showError()
        }
    }

    private fun showLoading() {
        progressBar.visible()
    }

    private fun setFormsRecyclerView(data: List<Form>?) {
        progressBar.gone()
        rv_form_list.layoutManager = LinearLayoutManager(this)
        rv_form_list.adapter = data?.let {
            FormAdapter(it, object : FormAdapter.OnRecyclerViewClickListener{
                override fun onMoreButtonClick() {

                }

                override fun onInviteButtonClick() {

                }

                override fun onInboxButtonClick() {

                }
            })
        }
    }

    private fun showError() {

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
        rv_form_list.visibility = View.GONE
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
            rv_form_list.visibility = View.VISIBLE
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val CLASS_SIMPLE_NAME = "Form Listing"
    }

}
