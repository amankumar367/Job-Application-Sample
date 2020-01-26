package com.aman.findjob.ui.newForm

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
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
import com.aman.findjob.extention.createFactory
import com.aman.findjob.extention.currentDate
import com.aman.findjob.extention.gone
import com.aman.findjob.extention.visible
import com.aman.findjob.repo.FormRepoI
import com.aman.findjob.room.entity.Form
import com.aman.findjob.ui.MainActivity
import com.aman.findjob.utils.DateUtils
import com.aman.findjob.utils.OnDateSetListener
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_new_form.*
import kotlinx.android.synthetic.main.layout_job_term.view.*
import kotlinx.android.synthetic.main.layout_payment_mode.*
import kotlinx.android.synthetic.main.layout_payment_mode.view.*
import kotlinx.android.synthetic.main.layout_rate.*
import kotlinx.android.synthetic.main.layout_rate.view.*
import javax.inject.Inject
import kotlin.properties.Delegates


class NewFormFragment: DaggerFragment() {

    @Inject
    lateinit var repo: FormRepoI

    private lateinit var viewModel: FormViewModel

    private var listener: OnFragmentInteractionListener? = null

    private var startDate: Long? = null

    private lateinit var mRateDialog: AlertDialog
    private lateinit var mPaymentModeDialog: AlertDialog
    private lateinit var mJobTermDialog: AlertDialog

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
        initCustomDialog()
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

    private fun initCustomDialog() {
        val rateDialog = LayoutInflater.from(context!!).inflate(R.layout.layout_rate, null)
        val paymentModeDialog = LayoutInflater.from(context!!).inflate(R.layout.layout_payment_mode, null)
        val jobTermDialog = LayoutInflater.from(context!!).inflate(R.layout.layout_job_term, null)

        mRateDialog = AlertDialog.Builder(context!!)
            .setView(rateDialog)
            .setTitle(getString(R.string.rate))
            .setPositiveButton(getText(R.string.select)
            ) { _, _ ->
                val rate: String? = when (rateDialog.rg_rate.checkedRadioButtonId) {
                    R.id.rb_no_preference_rate -> rateDialog.rb_no_preference_rate.text.toString()
                    R.id.rb_fixed_budget -> rateDialog.rb_fixed_budget.text.toString()
                    R.id.rb_hourly_rate -> rateDialog.rb_hourly_rate.text.toString()
                    else -> null
                }
                et_rate.text = Editable.Factory.getInstance().newEditable(rate!!)
                til_rate.error = null
            }
            .setNegativeButton(getString(R.string.cancel)
            ) { _, _ ->
                if (mRateDialog.isShowing)
                    mRateDialog.dismiss()
            }.create()

        mPaymentModeDialog = AlertDialog.Builder(context!!)
            .setView(paymentModeDialog)
            .setTitle(getString(R.string.payment_mode))
            .setPositiveButton(getText(R.string.select)
            ) { _, _ ->
                val paymentMode: String? = when (paymentModeDialog.rg_payment_mode.checkedRadioButtonId) {
                    R.id.rb_no_preference_payment_mode -> paymentModeDialog.rb_no_preference_payment_mode.text.toString()
                    R.id.rb_epayment -> paymentModeDialog.rb_epayment.text.toString()
                    R.id.rb_cash -> paymentModeDialog.rb_cash.text.toString()
                    else -> null
                }
                et_payment_mode.text = Editable.Factory.getInstance().newEditable(paymentMode!!)
                til_payment_mode.error = null
            }
            .setNegativeButton(getString(R.string.cancel)
            ) { _, _ ->
                if (mPaymentModeDialog.isShowing)
                    mPaymentModeDialog.dismiss()
            }
            .create()

        mJobTermDialog = AlertDialog.Builder(context!!)
            .setView(jobTermDialog)
            .setTitle(getString(R.string.job_term))
            .setPositiveButton(getText(R.string.select)
            ) { _, _ ->
                val jobTerm: String? = when (jobTermDialog.rg_job_term.checkedRadioButtonId) {
                    R.id.rb_no_preference_job_term -> jobTermDialog.rb_no_preference_job_term.text.toString()
                    R.id.rb_recurring_job -> jobTermDialog.rb_recurring_job.text.toString()
                    R.id.rb_same_day_job -> jobTermDialog.rb_same_day_job.text.toString()
                    R.id.rb_multi_days_job -> jobTermDialog.rb_multi_days_job.text.toString()
                    else -> null
                }
                et_job_terms.text = Editable.Factory.getInstance().newEditable(jobTerm!!)
                til_job_terms.error = null
            }
            .setNegativeButton(getString(R.string.cancel)
            ) { _, _ ->
                if (mJobTermDialog.isShowing)
                    mJobTermDialog.dismiss()
            }
            .create()
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
            mRateDialog.show()
        }

        et_payment_mode.setOnClickListener {
            mPaymentModeDialog.show()
        }

        et_job_terms.setOnClickListener {
            mJobTermDialog.show()
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
        when {
            formState.loading -> showLoading()
            formState.success -> goToMainScreen()
            formState.failure -> showError()
        }
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
        var budgetValue: Int = 0
        if (budget.isEmpty()) {
            til_budget.error = getString(R.string.required)
            return null
        } else {
            budgetValue = budget.toInt()
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