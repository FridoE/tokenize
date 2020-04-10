package com.example.tokenizetest.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tokenizetest.R
import com.example.tokenizetest.databinding.MainActivityBinding
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }
    val args: MainFragmentArgs by navArgs()

    val listGoals = ArrayList<GoalsViewModel>()
    //val testGoal = GoalsViewModel("Smartwatch", 300)

    private lateinit var goalViewModel: GoalsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = inflater.inflate(R.layout.main_fragment, container, false)
        val listView = rootView.findViewById<RecyclerView>(R.id.listGoals)
        val viewModelFactory = GoalsViewModelFactory(application = requireNotNull(this.activity).application)
        val adapter = GoalsListAdapter()

        goalViewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(GoalsViewModel::class.java)

        goalViewModel = activity?.run {
            ViewModelProvider(this).get(GoalsViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        goalViewModel.goalsList.observe(this, Observer{list -> adapter.submitList(list)})
 /*       if (args.GoalName != null) {
            val newGoal = Goal(args.GoalName!!, args.GoalPrice)
            goalViewModel.addGoal(newGoal)
        }*/

        listView.layoutManager = LinearLayoutManager(this.context)

        listView.adapter = adapter

        val addgoal_btn = rootView.findViewById<Button>(R.id.addgoal_button)
        addgoal_btn.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToAddgoalFragement()
            findNavController().navigate(action)
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}
