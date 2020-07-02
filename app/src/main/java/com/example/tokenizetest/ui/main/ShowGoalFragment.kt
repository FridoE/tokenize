package com.example.tokenizetest.ui.main

import android.graphics.drawable.Icon
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tokenizetest.R
import com.example.tokenizetest.databinding.GoalFragmentBinding

class ShowGoalFragment : Fragment() {
    private lateinit var showgoalViewModel: ShowGoalViewModel
    private lateinit var binding: GoalFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = GoalFragmentBinding.inflate(inflater)
        val rootView = binding.root //inflater.inflate(R.layout.goal_fragment, container, false)
        val listView = rootView.findViewById<RecyclerView>(R.id.activityList)
        val activityHistoryView = rootView.findViewById<RecyclerView>(R.id.activityHistoryList)
        val activityListAdapter = ShowGoalActivityListAdapter({ avm: TokenizedActivityViewModel -> onClickActivity(avm)})
        val activityHistoryListAdapter = ActivityHistoryListAdapter()


        showgoalViewModel = activity?.run {
            ViewModelProvider(this, ShowGoalViewModelFactory(this.application, Goal("test", 100, Icon.createWithResource(this.applicationContext, R.drawable.reward), "asdf", 10))).get(ShowGoalViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        // TODO: create view model with goal id -> load data from model/repository
        // TODO: use data binding in the layout file to connect viemmodel with view

        showgoalViewModel.activityList.observe(this.viewLifecycleOwner, Observer { list -> activityListAdapter.submitList(list) })
        showgoalViewModel.activityHistoryList.observe(this.viewLifecycleOwner, Observer { list -> activityHistoryListAdapter.submitList(list) })


        listView.layoutManager = LinearLayoutManager(this.context)
        listView.adapter = activityListAdapter
        activityHistoryView.layoutManager = LinearLayoutManager(this.context)
        activityHistoryView.adapter = activityHistoryListAdapter

        return rootView
    }

    fun onClickActivity(activityVM: TokenizedActivityViewModel) {
        ActivityDoneDialog(activityVM, showgoalViewModel).show(childFragmentManager, "f")
    }
}