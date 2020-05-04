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

class ShowGoalActivityListAdapter : ListAdapter<TokenizedActivityViewModel, ShowGoalActivityListAdapter.ViewHolder>(TokenizedActivityViewModelDiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.activitylist_entry, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val goal = getItem(position)
        /*holder.textGoal?.text = goal.titleString
        holder.textProgress?.text = goal.balanceString
        holder.progressBalance?.progress = goal.progress
        holder.iconImageView?.setImageIcon(goal.icon)*/
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
       /* var textGoal: TextView? = null
        var textProgress: TextView? = null
        var progressBalance: ProgressBar? = null
        var iconImageView: CircleImageView? = null
        init {
            textGoal = itemView.findViewById(R.id.textGoal)
            textProgress = itemView.findViewById(R.id.textProgress)
            progressBalance = itemView.findViewById(R.id.progressBalance)
            iconImageView = itemView.findViewById(R.id.imageSymbol)
        }*/
    }

}
class TokenizedActivityViewModelDiffCallback : DiffUtil.ItemCallback<TokenizedActivityViewModel>() {
    override fun areItemsTheSame(oldItem: TokenizedActivityViewModel, newItem: TokenizedActivityViewModel): Boolean {
        return false // oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TokenizedActivityViewModel, newItem: TokenizedActivityViewModel): Boolean {
        return false // oldItem == newItem
    }
}

