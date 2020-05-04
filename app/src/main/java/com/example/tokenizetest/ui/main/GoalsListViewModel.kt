package com.example.tokenizetest.ui.main

import android.app.Application
import android.graphics.drawable.Icon
import android.icu.text.NumberFormat
import android.icu.util.Currency
import androidx.lifecycle.*
import com.example.tokenizetest.R
import java.util.*

class GoalsListViewModel(val app: Application) : AndroidViewModel(app) {

    private val _goalsList = MutableLiveData<MutableList<GoalsListItemViewModel>>()
    val goalsList: LiveData<MutableList<GoalsListItemViewModel>>
        get() = _goalsList

    var newGoalName: String = ""
    var newGoalPrice: Int = 0
    var newGoalIcon: Icon = Icon.createWithResource(app.applicationContext, R.drawable.reward)
    var newGoalActivityName: String = ""
    var newGoalActivityEarnings: Int = 0

    init {
        _goalsList.value = mutableListOf<GoalsListItemViewModel>()

        var newGoal = Goal("Smartwatch", 500, Icon.createWithResource(app.applicationContext, R.drawable.smartwatch), "Act1", 5)
        newGoal.balance = 150
        addGoal(GoalsListItemViewModel(app, newGoal))
        var secondGoal = Goal("Fernseher", 350, newGoalIcon, "Act2", 10)
        secondGoal.balance = 80
        addGoal(GoalsListItemViewModel(app, secondGoal))
    }

    private fun addGoal(goal: GoalsListItemViewModel) = _goalsList.value?.add(goal)
    fun addNewGoal() {
        val goal = GoalsListItemViewModel(
            app,
            Goal(
            newGoalName,
            newGoalPrice,
            newGoalIcon,
            newGoalActivityName,
            newGoalActivityEarnings)
        )
        addGoal(goal)
        newGoalName = ""
        newGoalPrice = 0
        newGoalIcon = Icon.createWithResource(app.applicationContext, R.drawable.reward)
        newGoalActivityName = ""
        newGoalActivityEarnings = 0
    }

/*    companion object {
        fun balanceString(goal: Goal): String {
            return "${goal.balance} € (${GoalsListViewModel.progress(goal)}%)"
        }

        fun titleString(goal: Goal): String {
            return "${goal.name} (${goal.price} €)"
        }

        fun progress(goal: Goal): Int {
            return ((goal.balance.toFloat()) / goal.price * 100).toInt()
        }
    }*/
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

    val balanceString = "Balance: ${format.format(_goal.balance)} (${progress}%)"
    val titleString = "$name ($price)"
    val icon = _goal.icon
}

class Goal(
    text: String,
    var price: Int,
    var icon: Icon,
    activityName: String,
    activityEarnings: Int
) {
    var balance: Int = 0
    var name: String = text
    val id: Int
    var activities = mutableListOf<TokenizedActivity>()

    init {
        id = nextId++
        activities.add(TokenizedActivity(activityName, activityEarnings))
    }
    /*constructor(g: Goal): this(g.name, g.price, g.icon, "",0) {
        activities = g.activities
        balance = g.balance
    }*/

    private companion object {
        var nextId = 1
    }

    override fun toString(): String {
        return "$name: $price €"
    }

    data class TokenizedActivity(var name: String, var earnings: Int)
}

fun <T> MutableLiveData<T>.notifyObserver() {
    this.value = this.value
}

class GoalsListViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GoalsListViewModel::class.java)) {
            return GoalsListViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}