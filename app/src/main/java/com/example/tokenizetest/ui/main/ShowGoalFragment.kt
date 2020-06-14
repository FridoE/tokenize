package com.example.tokenizetest.ui.main

import android.graphics.drawable.Icon
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tokenizetest.R

class ShowGoalFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.goal_fragment, container, false)
        val listView = rootView.findViewById<RecyclerView>(R.id.activityList)
        val adapter = ShowGoalActivityListAdapter({avm: TokenizedActivityViewModel -> onClickActivity(avm)})

        val showgoalViewModel = activity?.run {
            ViewModelProvider(this, ShowGoalViewModelFactory(this.application, Goal("test", 100, Icon.createWithResource(this.applicationContext, R.drawable.reward), "asdf", 10))).get(ShowGoalViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        showgoalViewModel.activityList.observe(this.viewLifecycleOwner, Observer { list -> adapter.submitList(list) })

        listView.layoutManager = LinearLayoutManager(this.context)

        listView.adapter = adapter

        return rootView
    }

    fun onClickActivity(activityVM: TokenizedActivityViewModel) {
        ActivityDoneDialog(activityVM).show(childFragmentManager, "f")
        if(activityVM.doneActivity) {
            showgoalViewModel.doneActivity(activityVM)
            activityVM.doneActivity = false
        }
    }
}