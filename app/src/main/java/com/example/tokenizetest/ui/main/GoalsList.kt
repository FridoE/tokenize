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


class GoalsListAdapter(): ListAdapter<GoalViewModel, GoalsListAdapter.ViewHolder>(SleepNightDiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GoalsListAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.goal_entry, parent, false)
        return GoalsListAdapter.ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GoalsListAdapter.ViewHolder, position: Int) {
        val goal = getItem(position)
        holder.textGoal?.text = goal.name
        holder.textProgress?.text = goal.balanceString
        holder.progressBalance?.progress = goal.progress
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var textGoal: TextView? = null
        var textProgress: TextView? = null
        var progressBalance: ProgressBar? = null
        init {
            textGoal = itemView.findViewById(R.id.textGoal)
            textProgress = itemView.findViewById(R.id.textProgress)
            progressBalance = itemView.findViewById(R.id.progressBalance)
        }
    }

}
class SleepNightDiffCallback : DiffUtil.ItemCallback<GoalViewModel>() {
    override fun areItemsTheSame(oldItem: GoalViewModel, newItem: GoalViewModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: GoalViewModel, newItem: GoalViewModel): Boolean {
        return oldItem == newItem
    }
}

