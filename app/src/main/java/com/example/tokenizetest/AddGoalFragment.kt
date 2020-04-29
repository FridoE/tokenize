package com.example.tokenizetest

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.cottacush.android.currencyedittext.CurrencyEditText
import com.example.tokenizetest.databinding.AddgoalFragmentBinding
import com.example.tokenizetest.ui.main.GoalsListViewModel
import com.example.tokenizetest.ui.main.GoalsListViewModelFactory
import com.example.tokenizetest.ui.main.hideSoftKeyboardOnFocusLostEnabled
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

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

    private lateinit var goalListViewModel: GoalsListViewModel
    private lateinit var binding: AddgoalFragmentBinding

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
        val root = inflater.inflate(R.layout.addgoal_fragment, container, false)

        val viewModelFactory =
            GoalsListViewModelFactory(application = requireNotNull(this.activity).application)
        goalListViewModel =
            ViewModelProvider(requireActivity(), viewModelFactory).get(GoalsListViewModel::class.java)

        val addgoalBtn = root.findViewById<Button>(R.id.addgoal_button_addgoal)
        val nameInput = root.findViewById<TextInputEditText>(R.id.goal_textInput)
        val priceInput = root.findViewById<CurrencyEditText>(R.id.price_textInput)
        val priceInputLayout = root.findViewById<TextInputLayout>(R.id.price_textInputLayout)
        priceInput.hideSoftKeyboardOnFocusLostEnabled(true)
        nameInput.hideSoftKeyboardOnFocusLostEnabled(true)

        addgoalBtn.setOnClickListener {
            goalListViewModel.newGoalName = nameInput.text.toString()
            goalListViewModel.newGoalPrice = priceInput.getNumericValue().toInt()
            val action = AddGoalFragmentDirections.actionAddgoalFragementToAddActivityFragment()
            it.findNavController().navigate(action)
        }

        nameInput.addTextChangedListener(NameInputWatcher(addgoalBtn, priceInput))
        nameInput.text?.let {
            addgoalBtn.isEnabled = it.isNotEmpty()
            addgoalBtn.isClickable = it.isNotEmpty()
        }
        priceInput.addTextChangedListener(PriceInputWatcher(addgoalBtn, priceInputLayout, nameInput))
        return root
    }

    private class NameInputWatcher(val btn: Button, val prc_inp: TextInputEditText) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            s?.let {
                val btn_enabled = it.isNotEmpty() && !prc_inp.text.isNullOrEmpty()
                btn.isEnabled = btn_enabled
                btn.isClickable = btn_enabled
            }
        }
    }

    private class PriceInputWatcher(val btn: Button, val inp: TextInputLayout, val name_inp: TextInputEditText) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            s?.let {
                var btn_enabled = it.length>2 && !name_inp.text.isNullOrEmpty()
 /*               var new_hint = ""
                try {
                    it.toString().toInt()
                } catch (e: NumberFormatException) {
                    btn_enabled = false
                    new_hint = inp.context.getString(R.string.addgoal_hint_notANumber)
                }
                inp.hint = new_hint*/
                btn.isEnabled = btn_enabled
                btn.isClickable = btn_enabled
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment addgoal_fragment.
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
