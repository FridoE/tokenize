package com.example.tokenizetest.ui.showgoal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tokenizetest.R
import com.example.tokenizetest.data.Goal
import java.util.*

class ActivityHistoryListAdapter(val onclickListener: (ActivityHistoryListItemVM) -> Unit,
                                 val onLongClickListener: (ActivityHistoryListItemVM) -> Boolean,
                                 val longPressActive: LiveData<Boolean> ) : ListAdapter<ActivityHistoryListItemVM, ActivityHistoryListAdapter.ViewHolder>(
    ActivityHistoryListItemVMDiffCallback()
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.activityhistorylist_entry, parent, false)
       // longPressActive.observe(Observer { o, arg ->  })
        return ViewHolder(
            itemView
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val activityHistoryListItemVM = getItem(position)
        holder.historyDateText?.text = activityHistoryListItemVM.historyDateText
        holder.bind(activityHistoryListItemVM, onclickListener)
        holder.itemView.setOnLongClickListener{onLongClickListener(activityHistoryListItemVM)}
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val historyDateText: TextView? = itemView.findViewById(R.id.activityhistorydate_text)
        val removeButton: ImageButton? = itemView.findViewById(R.id.removeHLI_button)

        fun bind(activityHLIVM: ActivityHistoryListItemVM, ocl: (ActivityHistoryListItemVM) -> Unit) {
            removeButton?.setOnClickListener{ocl(activityHLIVM)}
        }
    }
}

class ActivityHistoryListItemVM(val act: Goal.TokenizedActivity, val date: Date) {
    val historyDateText = "${date.toString()}"
//    val activity = _act
//    val date = _date
}

class ActivityHistoryListItemVMDiffCallback : DiffUtil.ItemCallback<ActivityHistoryListItemVM>() {
    override fun areItemsTheSame(oldItem: ActivityHistoryListItemVM, newItem: ActivityHistoryListItemVM): Boolean {
        return false // oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ActivityHistoryListItemVM, newItem: ActivityHistoryListItemVM): Boolean {
        return false // oldItem == newItem
    }
}