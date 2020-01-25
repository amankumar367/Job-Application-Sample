package com.aman.findjob.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aman.findjob.R
import com.aman.findjob.room.entity.Form
import kotlinx.android.synthetic.main.layout_form_single_item.view.*

class FormAdapter(
    private val result: List<Form>,
    private val onRecyclerViewListener: OnRecyclerViewClickListener)
    : RecyclerView.Adapter<FormAdapter.FormViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FormViewHolder {
        return FormViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_form_single_item, parent, false), onRecyclerViewListener
        )
    }

    override fun getItemCount(): Int {
        return result.size
    }

    override fun onBindViewHolder(holder: FormViewHolder, position: Int) {
        holder.bind(result[position])
    }


    class FormViewHolder(
        private val view: View,
        private val onRecyclerViewListener: OnRecyclerViewClickListener) : RecyclerView.ViewHolder(view) {
        fun bind(result: Form) {
            view.tv_form_title.text = result.title
            view.tv_date.text = result.startDate.toString()
            view.tv_rate.text = result.rate
            view.tv_job_term.text = result.jobTerm
            onClicks()
        }

        private fun onClicks() {
            view.iv_more.setOnClickListener {
                onRecyclerViewListener.onMoreButtonClick()
            }

            view.tv_invite.setOnClickListener {
                onRecyclerViewListener.onInviteButtonClick()
            }

            view.tv_inbox.setOnClickListener {
                onRecyclerViewListener.onInboxButtonClick()
            }
        }

    }

    interface OnRecyclerViewClickListener {
        fun onMoreButtonClick()
        fun onInviteButtonClick()
        fun onInboxButtonClick()
    }
}
