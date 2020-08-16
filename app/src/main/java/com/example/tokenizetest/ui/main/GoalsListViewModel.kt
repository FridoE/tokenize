package com.example.tokenizetest.ui.main

import android.app.Application
import android.graphics.drawable.Icon
import android.icu.text.NumberFormat
import android.icu.util.Calendar
import android.icu.util.Currency
import android.util.Log
import androidx.lifecycle.*
import com.example.tokenizetest.R
import com.example.tokenizetest.data.Goal
import com.example.tokenizetest.data.Repository
import java.util.*
import java.util.stream.Collectors.toMap

class GoalsListViewModel(val app: Application) : AndroidViewModel(app) {

    private val _goalsList = MutableLiveData<MutableList<GoalsListItemViewModel>>()
    val goalsList: LiveData<MutableList<GoalsListItemViewModel>>
        get() = _goalsList

    var newGoalName: String = ""
    var newGoalPrice: Int = 0
    var newGoalIconName: String = "reward"
    var newGoalActivityName: String = ""
    var newGoalActivityEarnings: Int = 0

    init {
        //_goalsList.value = mutableListOf<GoalsListItemViewModel>()
        _goalsList.value = Repository.goals.value?.map {
            GoalsListItemViewModel(app, it)}?.toMutableList()
/*        var newGoal = Goal("Smartwatch", 500, "smartwatch", "Act1", 5)
        newGoal.balance = 150
        addGoal(GoalsListItemViewModel(app, newGoal))
        var secondGoal = Goal("Fernseher", 350, "reward", "Act2", 10)
        secondGoal.balance = 80
        addGoal(GoalsListItemViewModel(app, secondGoal))*/
    }

    private fun addGoal(goal: GoalsListItemViewModel) {
        Repository.insert(goal._goal)
        _goalsList.value?.add(goal)
    }
    fun addNewGoal() {
        val goal = GoalsListItemViewModel(
            app,
            Goal(
            newGoalName,
            newGoalPrice,
            newGoalIconName,
            newGoalActivityName,
            newGoalActivityEarnings)
        )
        addGoal(goal)
        newGoalName = ""
        newGoalPrice = 0
        newGoalIconName = "reward"
        newGoalActivityName = ""
        newGoalActivityEarnings = 0
    }
}

class GoalsListItemViewModel(
    val app: Application,
    val _goal: Goal
) : AndroidViewModel(app) {
    val format: NumberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())

    init {
        format.maximumFractionDigits = 0
        format.currency = Currency.getInstance("EUR")
    }

    val goal = _goal
    val name = _goal.name
    val price = format.format(_goal.price)

    val id = _goal.id
    val progress = (_goal.balance.toFloat() / _goal.price * 100).toInt()

    val balance = format.format(_goal.balance)
    val balanceString = "Balance: ${balance} (${progress}%)"
    val titleString = "$name ($price)"
    val iconName = _goal.iconName
}


//Not necessary since the only argument for the viewmodel is (application)
/*class GoalsListViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GoalsListViewModel::class.java)) {
            return GoalsListViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}*/