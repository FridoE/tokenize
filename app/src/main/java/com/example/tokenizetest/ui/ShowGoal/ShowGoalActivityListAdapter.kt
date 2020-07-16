package com.example.tokenizetest.ui.showgoal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tokenizetest.R

class ShowGoalActivityListAdapter(val onclickListener: (TokenizedActivityViewModel) -> Unit) : ListAdapter<TokenizedActivityViewModel, ShowGoalActivityListAdapter.ViewHolder>(
    TokenizedActivityViewModelDiffCallback()
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.activitylist_entry, parent, false)
        return ViewHolder(
            itemView
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val activityVM = getItem(position)
        holder.activityEntryTitle?.text = activityVM.titleString
        holder.activityTotalEarnings?.text = activityVM.totalEarningsString
        holder.bind(activityVM, onclickListener)
        //holder.activityRemaining?.text = activityVM.remainingString
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var activityEntryTitle: TextView? = null
        var activityTotalEarnings: TextView? = null
        var activityRemaining: TextView? = null

        init {
            activityEntryTitle = itemView.findViewById(R.id.textActivityEntryTitle)
            activityTotalEarnings = itemView.findViewById(R.id.textActivityTotalEarnings)
            //activityRemaining = itemView.findViewById(R.id.textActivityRemaining)
        }
        fun bind(activityVM: TokenizedActivityViewModel, ocl: (TokenizedActivityViewModel) -> Unit) {
            itemView.setOnClickListener{ocl(activityVM)}
        }
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

