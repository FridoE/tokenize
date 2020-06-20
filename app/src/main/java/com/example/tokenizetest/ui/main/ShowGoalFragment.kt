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
    private lateinit var showgoalViewModel: ShowGoalViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.goal_fragment, container, false)
        val listView = rootView.findViewById<RecyclerView>(R.id.activityList)
        val activityHistoryView = rootView.findViewById<RecyclerView>(R.id.activityHistoryList)
        val activityListAdapter = ShowGoalActivityListAdapter({ avm: TokenizedActivityViewModel -> onClickActivity(avm)})
        //val activityHistoryListAdapter = ActivityHistoryListAdapter()
        //TODO: initialize history list


        showgoalViewModel = activity?.run {
            ViewModelProvider(this, ShowGoalViewModelFactory(this.application, Goal("test", 100, Icon.createWithResource(this.applicationContext, R.drawable.reward), "asdf", 10))).get(ShowGoalViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        showgoalViewModel.activityList.observe(this.viewLifecycleOwner, Observer { list -> activityListAdapter.submitList(list) })

        listView.layoutManager = LinearLayoutManager(this.context)
        activityHistoryView.layoutManager = LinearLayoutManager(this.context)

        listView.adapter = activityListAdapter

        return rootView
    }

    fun onClickActivity(activityVM: TokenizedActivityViewModel) {
        ActivityDoneDialog(activityVM, showgoalViewModel).show(childFragmentManager, "f")
    }
}