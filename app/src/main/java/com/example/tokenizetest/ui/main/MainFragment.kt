// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright © 2020 F. Engel
package com.example.tokenizetest.ui.main

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tokenizetest.R
import com.example.tokenizetest.databinding.MainActivityBinding


class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var goalListViewModel: GoalsListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        goalListViewModel = activity?.run {
            ViewModelProvider(this).get(GoalsListViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        //val binding: MainActivityBinding = DataBindingUtil.setContentView(activity!!, R.layout.main_activity)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = inflater.inflate(R.layout.main_fragment, container, false)
        val listView = rootView.findViewById<RecyclerView>(R.id.listGoals)
        val adapter = GoalsListAdapter({ gvm: GoalsListItemViewModel ->
            OnClickSelectGoalListItem(
                gvm
            )
        }, { gvm: GoalsListItemViewModel -> OnClickDeleteGoalsListItem(gvm) })

        goalListViewModel.goalsList.observe(this.viewLifecycleOwner, Observer { list ->
            adapter.submitList(list)
            adapter.notifyDataSetChanged()
            if(list.size == 0) {
                val action = MainFragmentDirections.actionMainFragmentToAddgoalFragement()
                findNavController().navigate(action)
            }
        })

        listView.layoutManager = LinearLayoutManager(this.context)
        listView.adapter = adapter

        val addgoalBtn = rootView.findViewById<Button>(R.id.addgoal_button)
        addgoalBtn.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToAddgoalFragement()
            findNavController().navigate(action)
        }

 /*       context?.let {
            ContextCompat.getDrawable(it, R.drawable.line_divider)
        }?.also {  listView.addItemDecoration(GoalsListDividerItemDecoration(it))}*/

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
   //     if(goalListViewModel.goalsList.value?.size == 0 || goalListViewModel.goalsList.value == null)
    }

    fun OnClickSelectGoalListItem(gvm: GoalsListItemViewModel) {
        val action = MainFragmentDirections.actionMainFragmentToShowGoalFragment(gvm.id, gvm.name)
        findNavController().navigate(action)
    }

    fun OnClickDeleteGoalsListItem(gvm: GoalsListItemViewModel): Boolean {
       //Toast.makeText(this.context, "Long Click", Toast.LENGTH_LONG).show()
        goalListViewModel.deleteGoal(gvm)
        return true
    }
}

class GoalsListDividerItemDecoration(var mDivider: Drawable) : RecyclerView.ItemDecoration() {
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingLeft;
        val right = parent.width - parent.paddingRight;

        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i);
            val params = child.layoutParams as RecyclerView.LayoutParams

            val top = child.bottom + params.bottomMargin;
            val bottom = top + mDivider.intrinsicHeight;

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
}
