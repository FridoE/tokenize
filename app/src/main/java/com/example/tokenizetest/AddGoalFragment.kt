package com.example.tokenizetest

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.tokenizetest.ui.main.Goal
import com.example.tokenizetest.ui.main.GoalsListAdapter
import com.example.tokenizetest.ui.main.GoalsViewModel
import com.example.tokenizetest.ui.main.GoalsViewModelFactory
import com.google.android.material.textfield.TextInputEditText

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddGoalFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddGoalFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var goalViewModel: GoalsViewModel
    private lateinit var addgoal_btn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.addgoal_fragement, container, false)

        val viewModelFactory = GoalsViewModelFactory(application = requireNotNull(this.activity).application)
        goalViewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(GoalsViewModel::class.java)

        addgoal_btn = root.findViewById<Button>(R.id.addgoal_button_addgoal)
        val name_input = root.findViewById<TextInputEditText>(R.id.goal_textInput)
        val price_input = root.findViewById<TextInputEditText>(R.id.price_textInput)

        addgoal_btn.setOnClickListener {
            val priceInt = 100 //price?.text.toString().toInt()
            goalViewModel.addGoal(Goal(name_input.toString(), priceInt))
            Toast.makeText(this.context, name_input.text.toString(), Toast.LENGTH_LONG).show()
            val action = AddGoalFragmentDirections.actionAddgoalFragementToMainFragment("test" /*name?.text?.toString()*/, priceInt)
            it.findNavController().navigate(action)
        }

        name_input.addTextChangedListener( NameInputWatcher(addgoal_btn))
        name_input.text?.let {
            addgoal_btn.isEnabled = it.isNotEmpty()
            addgoal_btn.isClickable = it.isNotEmpty()
        }
        return root
    }

    private class NameInputWatcher(val btn: Button) : TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
           }

    override fun afterTextChanged(s: Editable?) {
        s?.let {
            val btn_enabled = it.isNotEmpty()
            btn.isEnabled = btn_enabled
            btn.isClickable = btn_enabled
        }
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }
}

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment addgoal_fragement.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddGoalFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
