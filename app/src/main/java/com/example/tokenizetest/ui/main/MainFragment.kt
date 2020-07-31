package com.example.tokenizetest.ui.main

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tokenizetest.R
import com.example.tokenizetest.data.Goal

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    val listGoals = ArrayList<GoalsListViewModel>()
    //val testGoal = GoalsViewModel("Smartwatch", 300)

    private lateinit var goalListViewModel: GoalsListViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = inflater.inflate(R.layout.main_fragment, container, false)
        val listView = rootView.findViewById<RecyclerView>(R.id.listGoals)
        val adapter = GoalsListAdapter({g: Goal -> goalsListItemClickListener(g)})

        goalListViewModel = activity?.run {
            ViewModelProvider(this).get(GoalsListViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        goalListViewModel.goalsList.observe(this.viewLifecycleOwner, Observer { list -> adapter.submitList(list) })

        listView.layoutManager = LinearLayoutManager(this.context)

        listView.adapter = adapter

        val addgoal_btn = rootView.findViewById<Button>(R.id.addgoal_button)
        addgoal_btn.setOnClickListener {
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
    }

    fun goalsListItemClickListener(g: Goal) {
        Toast.makeText(this.context, "Click ${g.id}", Toast.LENGTH_LONG).show()
        val action = MainFragmentDirections.actionMainFragmentToShowGoalFragment(g.id)
        findNavController().navigate(action)
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
