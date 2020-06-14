package com.example.tokenizetest.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tokenizetest.R
import de.hdodenhof.circleimageview.CircleImageView


class GoalsListAdapter(val onclickListener: (Goal) -> Unit): ListAdapter<GoalsListItemViewModel, GoalsListAdapter.ViewHolder>(GoalViewModelDiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GoalsListAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.goalslist_entry, parent, false)
        return GoalsListAdapter.ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GoalsListAdapter.ViewHolder, position: Int) {
        val goalVM = getItem(position)
        holder.textGoal?.text = goalVM.titleString
        holder.textProgress?.text = goalVM.balanceString
        holder.progressBalance?.progress = goalVM.progress
        holder.iconImageView?.setImageIcon(goalVM.icon)
        holder.bind(goalVM.goal, onclickListener)
    }

    class ViewHolder(val _itemView: View): RecyclerView.ViewHolder(_itemView) {
        var textGoal: TextView? = null
        var textProgress: TextView? = null
        var progressBalance: ProgressBar? = null
        var iconImageView: CircleImageView? = null
        init {
            textGoal = _itemView.findViewById(R.id.textGoal)
            textProgress = _itemView.findViewById(R.id.textProgress)
            progressBalance = _itemView.findViewById(R.id.progressBalance)
            iconImageView = _itemView.findViewById(R.id.imageSymbol)

        }
        fun bind(g: Goal, onclickListener: (Goal) -> Unit) {
            _itemView.setOnClickListener {onclickListener(g) }
        }
    }

}
class GoalViewModelDiffCallback : DiffUtil.ItemCallback<GoalsListItemViewModel>() {
    override fun areItemsTheSame(oldItem: GoalsListItemViewModel, newItem: GoalsListItemViewModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: GoalsListItemViewModel, newItem: GoalsListItemViewModel): Boolean {
        return false // oldItem == newItem
    }
}

