package com.aman.findjob.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.aman.findjob.R


class NewFormFragement: Fragment() {

    private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_form, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        setToolbar()
    }

    private fun init() {

    }

    private fun setToolbar(){
        val toolbar = (activity as MainActivity).findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = CLASS_SIMPLE_NAME
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener {
            listener?.onBackPressed()
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val addItem = menu.findItem(R.id.menu_add)
        val sendItem = menu.findItem(R.id.menu_send)

        addItem.isVisible = false
        sendItem.isVisible = true
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    interface OnFragmentInteractionListener {
        fun onBackPressed()
    }

    companion object {
        const val CLASS_SIMPLE_NAME = "New Form"
        private const val TAG = "NewFormFragement"

        fun newInstance(): NewFormFragement = NewFormFragement()
    }
}