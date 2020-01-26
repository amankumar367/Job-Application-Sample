package com.aman.findjob.ui.newForm

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.DatePicker
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.aman.findjob.R
import com.aman.findjob.extension.createFactory
import com.aman.findjob.extension.currentDate
import com.aman.findjob.extension.gone
import com.aman.findjob.extension.visible
import com.aman.findjob.repo.FormRepoI
import com.aman.findjob.room.entity.Form
import com.aman.findjob.ui.MainActivity
import com.aman.findjob.utils.DateUtils
import com.aman.findjob.utils.OnDateSetListener
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_new_form.*
import kotlinx.android.synthetic.main.layout_radio.view.*
import javax.inject.Inject


class NewFormFragment: DaggerFragment() {

    @Inject
    lateinit var repo: FormRepoI

    private lateinit var viewModel: FormViewModel

    private var listener: OnFragmentInteractionListener? = null

    private var startDate: Long? = null

    private lateinit var mAlertDialog: AlertDialog

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
        addTextWatcher()
        onClicks()
        setObserver()

    }

    private fun init() {
        Log.d(TAG, " >>> Initializing viewModel")

        val factory = FormViewModel(repo).createFactory()
        viewModel = ViewModelProvider(this, factory).get(FormViewModel::class.java)
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

    private fun addTextWatcher() {
        et_form_title.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                if (p0!!.isNotEmpty()) {
                    til_form_title.error = null
                } else {
                    til_form_title.error = getString(R.string.required)
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })

        et_form_discription.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                if (p0!!.isNotEmpty()) {
                    til_form_discription.error = null
                } else {
                    til_form_discription.error = getString(R.string.required)
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })

        et_budget.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                if (p0!!.isNotEmpty()) {
                    til_budget.error = null
                } else {
                    til_budget.error = getString(R.string.required)
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })

    }

    private fun onClicks() {
        et_rate.setOnClickListener {
            showCustomDialog(DialogType.RATE)
        }

        et_payment_mode.setOnClickListener {
            showCustomDialog(DialogType.PAYMENT_MODE)
        }

        et_job_terms.setOnClickListener {
            showCustomDialog(DialogType.JOB_TERM)
        }

        startDate = currentDate
        et_start_date.text = Editable.Factory.getInstance()
            .newEditable(DateUtils.format(startDate!!, DateUtils.DATE_FORMAT))
        et_start_date.setOnClickListener {
            DateUtils.datePicker(context!!, object : OnDateSetListener{
                override fun onDateSet(view: DatePicker?, timeInMillis: Long) {
                    startDate = timeInMillis
                    et_start_date.text = Editable.Factory.getInstance()
                        .newEditable(DateUtils.format(timeInMillis, DateUtils.DATE_FORMAT))
                }
            }).show()
        }

    }

    private fun setObserver() {
        viewModel.stateObservable.observe( this, Observer {
            updateView(it)
        })
    }

    private fun updateView(formState: FormState) {
        if (formState.eventType == FormState.EventType.ADD) {
            when {
                formState.loading -> showLoading()
                formState.success -> goToMainScreen()
                formState.failure -> showError()
            }
        }
    }

    private fun showCustomDialog (dialogType: DialogType) {
        val dialog = LayoutInflater.from(context!!).inflate(R.layout.layout_radio, null)
        var dialogTitle = "Rate"
        when (dialogType){
            DialogType.RATE -> {
                dialogTitle = getString(R.string.rate)
                dialog.rg_rate.visible()
                dialog.rg_payment_mode.gone()
                dialog.rg_job_term.gone()
            }
            DialogType.PAYMENT_MODE -> {
                dialogTitle = getString(R.string.payment_mode)
                dialog.rg_rate.gone()
                dialog.rg_payment_mode.visible()
                dialog.rg_job_term.gone()
            }
            DialogType.JOB_TERM -> {
                dialogTitle = getString(R.string.job_term)
                dialog.rg_rate.gone()
                dialog.rg_payment_mode.gone()
                dialog.rg_job_term.visible()
            }
        }

        mAlertDialog = AlertDialog.Builder(context!!)
            .setView(dialog)
            .setTitle(dialogTitle)
            .setPositiveButton(getText(R.string.select)
            ) { _, _ ->

                when (dialogType){
                    DialogType.RATE -> {
                        val rate: String? = when (dialog.rg_rate.checkedRadioButtonId) {
                            R.id.rb_no_preference_rate -> dialog.rb_no_preference_rate.text.toString()
                            R.id.rb_fixed_budget -> dialog.rb_fixed_budget.text.toString()
                            R.id.rb_hourly_rate -> dialog.rb_hourly_rate.text.toString()
                            else -> null
                        }
                        et_rate.text = Editable.Factory.getInstance().newEditable(rate!!)
                        til_rate.error = null
                    }
                    DialogType.PAYMENT_MODE -> {
                        val paymentMode: String? = when (dialog.rg_payment_mode.checkedRadioButtonId) {
                            R.id.rb_no_preference_payment_mode -> dialog.rb_no_preference_payment_mode.text.toString()
                            R.id.rb_epayment -> dialog.rb_epayment.text.toString()
                            R.id.rb_cash -> dialog.rb_cash.text.toString()
                            else -> null
                        }
                        et_payment_mode.text = Editable.Factory.getInstance().newEditable(paymentMode!!)
                        til_payment_mode.error = null
                    }
                    DialogType.JOB_TERM -> {
                        val jobTerm: String? = when (dialog.rg_job_term.checkedRadioButtonId) {
                            R.id.rb_no_preference_job_term -> dialog.rb_no_preference_job_term.text.toString()
                            R.id.rb_recurring_job -> dialog.rb_recurring_job.text.toString()
                            R.id.rb_same_day_job -> dialog.rb_same_day_job.text.toString()
                            R.id.rb_multi_days_job -> dialog.rb_multi_days_job.text.toString()
                            else -> null
                        }
                        et_job_terms.text = Editable.Factory.getInstance().newEditable(jobTerm!!)
                        til_job_terms.error = null
                    }
                }
            }
            .setNegativeButton(getString(R.string.cancel)
            ) { _, _ ->
                if (mAlertDialog.isShowing)
                    mAlertDialog.dismiss()
            }.create()

        mAlertDialog.show()
    }

    private fun goToMainScreen() {
        listener?.onBackPressed()
        pb_new_form.gone()
    }

    private fun showLoading() {
        pb_new_form.visible()
    }

    private fun showError() {

    }

    private fun addNewForm() {
        Log.d(TAG, " >>> Receive call for new Form ")

        getAndValidateForm()?.let {
            viewModel.addForm(it)
        }

    }

    private fun getAndValidateForm(): Form? {
        val title = til_form_title.editText!!.text.toString()
        if (title.isEmpty()) {
            til_form_title.error = getString(R.string.required)
            return null
        }

        val description = til_form_discription.editText!!.text.toString()
        if (description.isEmpty()) {
            til_form_discription.error = getString(R.string.required)
            return null
        }

        val budget = til_budget.editText!!.text.toString()
        var budgetValue: Long
        if (budget.isEmpty()) {
            til_budget.error = getString(R.string.required)
            return null
        } else {
            budgetValue = budget.toLong()
        }

        val currency = til_currency.hint.toString()

        val rate = til_rate.editText!!.text.toString()
        if (rate.isEmpty()) {
            til_rate.error = getString(R.string.required)
            return null
        }

        val paymentMode = til_payment_mode.editText!!.text.toString()
        if (paymentMode.isEmpty()) {
            til_payment_mode.error = getString(R.string.required)
            return null
        }


        val jobTerm = til_job_terms.editText!!.text.toString()
        if (jobTerm.isEmpty()) {
            til_job_terms.error = getString(R.string.required)
            return null
        }

        return Form(title, description, budgetValue, currency, rate, paymentMode, startDate!!, jobTerm)

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

    enum class DialogType { RATE, PAYMENT_MODE, JOB_TERM }

    companion object {
        private const val TAG = "NewFormFragement"
        const val CLASS_SIMPLE_NAME = "New Form"

        fun newInstance(): NewFormFragment = NewFormFragment()
    }
}