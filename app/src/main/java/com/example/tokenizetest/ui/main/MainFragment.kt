package com.example.tokenizetest.ui.main

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tokenizetest.R

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
        val viewModelFactory =
            GoalsListViewModelFactory(application = requireNotNull(this.activity).application)
        val adapter = GoalsListAdapter()

        goalListViewModel =
            ViewModelProvider(requireActivity(), viewModelFactory).get(GoalsListViewModel::class.java)

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
