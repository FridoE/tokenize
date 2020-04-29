package com.example.tokenizetest.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.cottacush.android.currencyedittext.CurrencyEditText
import com.example.tokenizetest.R
import com.google.android.material.textfield.TextInputEditText

class AddActivityFragment : Fragment() {

    private lateinit var goalListViewModel: GoalsListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.add_activity, container, false)
        val activityInputEdit = rootView.findViewById<TextInputEditText>(R.id.activityInputEdit)
        val  rewardInputEdit = rootView.findViewById<CurrencyEditText>(R.id.RewardInputEdit)
        rewardInputEdit.hideSoftKeyboardOnFocusLostEnabled(true)
        activityInputEdit.hideSoftKeyboardOnFocusLostEnabled(true)

 //       val addAnotherActivityButton =  rootView.findViewById<Button>(R.id.addAnotherActivityButton)
        val completeButton = rootView.findViewById<Button>(R.id.completeButton)

        val viewModelFactory =
            GoalsListViewModelFactory(application = requireNotNull(this.activity).application)
        goalListViewModel =
            ViewModelProvider(requireActivity(), viewModelFactory).get(GoalsListViewModel::class.java)

        val inputsArray = ArrayList<TextInputEditText>()
        inputsArray.add(activityInputEdit)
        inputsArray.add(rewardInputEdit)
        val buttonsArray = ArrayList<Button>()
//        buttonsArray.add(addAnotherActivityButton)
        buttonsArray.add(completeButton)
        buttonsArray.forEach {
            it.isEnabled = false
            it.isClickable = false
        }
        activityInputEdit.addTextChangedListener(EmptyInputsDisableButtonsWatcher(activityInputEdit, inputsArray.toTypedArray(), buttonsArray.toTypedArray()))
        rewardInputEdit.addTextChangedListener(EmptyInputsDisableButtonsWatcher(rewardInputEdit, inputsArray.toTypedArray(), buttonsArray.toTypedArray()))

        completeButton.setOnClickListener {
            goalListViewModel.newGoalActivityName = activityInputEdit.text.toString()
            goalListViewModel.newGoalActivityEarnings = rewardInputEdit.getNumericValue().toInt()
            goalListViewModel.addNewGoal()
            val action = AddActivityFragmentDirections.actionAddActivityFragmentToMainFragment()
            it.findNavController().navigate(action)
        }


        return rootView
    }
}

private class EmptyInputsDisableButtonsWatcher(val curInp: TextInputEditText, val otherInp: Array<TextInputEditText>, val btn: Array<Button>) :
    TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    override fun afterTextChanged(s: Editable?) {
        var btn_enabled = true
        s?.let {
            if (curInp is CurrencyEditText) {
                btn_enabled = it.length > 2
            } else {
                btn_enabled = it.isNotEmpty()
            }
        }
        otherInp.forEach {
            btn_enabled = !it.text.isNullOrEmpty() && btn_enabled
        }
        btn.forEach {
            it.isEnabled = btn_enabled
            it.isClickable = btn_enabled
        }
    }
}