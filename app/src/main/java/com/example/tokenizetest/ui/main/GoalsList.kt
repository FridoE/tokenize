package com.example.tokenizetest.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tokenizetest.R
import kotlinx.android.synthetic.main.goal_entry.*
import kotlinx.android.synthetic.main.goal_entry.view.*
import org.w3c.dom.Text


class GoalsListAdapter(): ListAdapter<Goal, GoalsListAdapter.ViewHolder>(SleepNightDiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GoalsListAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.goal_entry, parent, false)
        return GoalsListAdapter.ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GoalsListAdapter.ViewHolder, position: Int) {
        val goal = getItem(position)
        holder.textGoal?.text = GoalsViewModel.titleString(goal)
        holder.textProgress?.text = GoalsViewModel.balanceString(goal)
        holder.progressBalance?.progress = GoalsViewModel.progress(goal)
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
class SleepNightDiffCallback : DiffUtil.ItemCallback<Goal>() {
    override fun areItemsTheSame(oldItem: Goal, newItem: Goal): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Goal, newItem: Goal): Boolean {
        return oldItem == newItem
    }
}

