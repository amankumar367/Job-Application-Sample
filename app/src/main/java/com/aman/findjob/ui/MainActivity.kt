package com.aman.findjob.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.aman.findjob.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NewFormFragement.OnFragmentInteractionListener {


    private var isFormListEnable = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setToolbar()
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar)
        toolbar.title = CLASS_SIMPLE_NAME
        toolbar.navigationIcon = null

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)

        val addItem = menu!!.findItem(R.id.menu_add)
        val sendItem = menu.findItem(R.id.menu_send)
        if (isFormListEnable) {
            addItem.isVisible = true
            sendItem.isVisible = false
        }


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
        isFormListEnable = false
        rv_form_list.visibility = View.GONE
        val instance = NewFormFragement.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, instance, NewFormFragement.CLASS_SIMPLE_NAME)
            .addToBackStack(NewFormFragement.CLASS_SIMPLE_NAME)
            .commit()
    }

    private fun getFormList() {

    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if (count==1){
            setToolbar()
            getFormList()
            isFormListEnable = true
            rv_form_list.visibility = View.VISIBLE
            supportFragmentManager.popBackStack()
        }else{
            super.onBackPressed()
        }
    }

    companion object {
        const val CLASS_SIMPLE_NAME = "Form Listing"
    }

}
