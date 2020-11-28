// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright © 2020 F. Engel
package com.example.tokenizetest.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tokenizetest.R
import de.hdodenhof.circleimageview.CircleImageView


class GoalsListAdapter(
    private val onSelectGoal: (GoalsListItemViewModel) -> Unit,
    private val onDeleteGoal: (GoalsListItemViewModel) -> Boolean
): ListAdapter<GoalsListItemViewModel, GoalsListAdapter.ViewHolder>(GoalViewModelDiffCallback()) {
    private var selectedItem: GoalsListItemViewModel? = null
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GoalsListAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.goalslist_entry,
            parent,
            false
        )
        return GoalsListAdapter.ViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: GoalsListAdapter.ViewHolder, position: Int) {
        val goalVM = getItem(position)
        holder.textGoal?.text = goalVM.titleString
        holder.textProgress?.text = goalVM.balanceString
        holder.progressBalance?.progress = goalVM.progress
        holder.iconImageView?.setImageIcon(
            getIconFromResName(
                goalVM.iconName,
                holder.iconImageView?.context!!
            )
        )
        fun longClickListener() : Boolean {
            selectedItem = goalVM
            goalVM.selectedForDeletion = !goalVM.selectedForDeletion
            notifyItemChanged(position)
            return true
        }
        holder.itemView.setOnLongClickListener{
            longClickListener()
        }
        holder.itemView.setOnClickListener {
            if(selectedItem?.selectedForDeletion == false || selectedItem==null) {
                onSelectGoal(goalVM)
            } else
                goalVM.selectedForDeletion = false
            selectedItem = goalVM
        }
        holder.iconImageView?.setOnClickListener {
            if(goalVM.selectedForDeletion)
                onDeleteGoal(goalVM)
            else
                holder.itemView.callOnClick()
        }
        holder.iconImageView?.setOnLongClickListener{ longClickListener() }

        if(selectedItem == goalVM && goalVM.selectedForDeletion) {
            holder.textGoal?.alpha = 0.3F
            holder.textProgress?.alpha = 0.3F
            holder.progressBalance?.alpha = 0.3F
            holder.iconImageView?.setImageIcon(
                getIconFromResName(
                    "trashcan",
                    holder.iconImageView?.context!!
                )
            )
        } else {
            holder.textGoal?.alpha = 1.0F
            holder.textProgress?.alpha = 1F
            holder.progressBalance?.alpha = 1F
            holder.iconImageView?.setImageIcon(
                getIconFromResName(
                    goalVM.iconName,
                    holder.iconImageView?.context!!
                )
            )
        }
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
    }

}
class GoalViewModelDiffCallback : DiffUtil.ItemCallback<GoalsListItemViewModel>() {
    override fun areItemsTheSame(oldItem: GoalsListItemViewModel, newItem: GoalsListItemViewModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: GoalsListItemViewModel,
        newItem: GoalsListItemViewModel
    ): Boolean {
        return false // oldItem == newItem
    }
}

