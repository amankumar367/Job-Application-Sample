package com.aman.findjob.ui.newForm

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.aman.findjob.R
import com.aman.findjob.extention.createFactory
import com.aman.findjob.repo.FormRepoI
import com.aman.findjob.room.entity.Form
import com.aman.findjob.ui.MainActivity
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class NewFormFragment: DaggerFragment() {

    @Inject
    lateinit var repo: FormRepoI

    private lateinit var viewModel: FormViewModel

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
        Log.d(TAG, " >>> Initializing viewModel")

        val factory = FormViewModel(repo).createFactory()
        viewModel = ViewModelProvider(this, factory).get(FormViewModel::class.java)
    }

    private fun addNewForm() {
        Log.d(TAG, " >>> Receive call for new Form ")
        viewModel.addForm(Form(title ="Aman", description = "This is dumm", budget = 800, currency = "vsjk",
            rate = "vsvjd", paymentMode = "vsdv", startDate = 241969146L, jobTerm = "dvbskjd"))

    }

    private fun setToolbar(){
        val toolbar = (activity as MainActivity).findViewById<Toolbar>(R.id.toolbar)
        toolbar.title =
            CLASS_SIMPLE_NAME
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener {
            listener?.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_send -> {
                addNewForm()
                true
            }
            else -> super.onOptionsItemSelected(item)
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
        private const val TAG = "NewFormFragement"
        const val CLASS_SIMPLE_NAME = "New Form"

        fun newInstance(): NewFormFragment = NewFormFragment()
    }
}