package com.example.tokenizetest.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tokenizetest.R
import java.util.*

class ActivityHistoryListAdapter : ListAdapter<ActivityHistoryListItemVM, ActivityHistoryListAdapter.ViewHolder>(ActivityHistoryListItemVMDiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.activityhistorylist_entry, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val activityHistoryListItemVM = getItem(position)
        holder.historyText?.text = activityHistoryListItemVM.historyText
//        holder.bind(activityHistoryListItemVM, onclickListener)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var historyText: TextView? = null
        init {
            historyText = itemView.findViewById(R.id.activityhistorylist_text)
        }
        fun bind(activityVM: TokenizedActivityViewModel, ocl: (TokenizedActivityViewModel) -> Unit) {
//            itemView.setOnClickListener{ocl(activityVM)}
        }
    }
}



class ActivityHistoryListItemVM(val _act: Goal.TokenizedActivity, val _date: Date) {
    val historyText = "${_date.toString()} -> ${_act.name}"
}

class ActivityHistoryListItemVMDiffCallback : DiffUtil.ItemCallback<ActivityHistoryListItemVM>() {
    override fun areItemsTheSame(oldItem: ActivityHistoryListItemVM, newItem: ActivityHistoryListItemVM): Boolean {
        return false // oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ActivityHistoryListItemVM, newItem: ActivityHistoryListItemVM): Boolean {
        return false // oldItem == newItem
    }
}