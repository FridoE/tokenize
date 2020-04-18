package com.example.tokenizetest.ui.main

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import com.example.tokenizetest.R

class GoalsViewModel(val app: Application): AndroidViewModel(app) {

    private val _goalsList = MutableLiveData<MutableList<Goal>>()
    val goalsList : LiveData<MutableList<Goal>>
            get() = _goalsList

    var newGoalName: String = ""
    var newGoalPrice: Int = 0
    var newGoalDrawable: Drawable = ContextCompat.getDrawable(app.applicationContext, R.drawable.reward)!!
    var newGoalActivityName: String = ""
    var newGoalActivityEarnings: Int = 0

    init{
        _goalsList.value = mutableListOf<Goal>()

        var newGoal = Goal("Smartwatch", 500, "Act1", 5)
        newGoal.balance = 150
        addGoal(newGoal)
        var secondGoal = Goal("Fernseher", 350, "Act2", 10)
        secondGoal.balance = 80
        addGoal(secondGoal)
        Log.d("goalsviewmodel", "init")
    }

    private fun addGoal(goal: Goal) = _goalsList.value?.add(goal)
    fun addNewGoal() {
        val goal = Goal(newGoalName, newGoalPrice, newGoalActivityName, newGoalActivityEarnings)
        addGoal(goal)
        newGoalName = ""
        newGoalPrice = 0
        newGoalDrawable = ContextCompat.getDrawable(app.applicationContext, R.drawable.reward)!!
        newGoalActivityName = ""
        newGoalActivityEarnings = 0
    }

    companion object {
        fun balanceString(goal: Goal) :String {
            return "${goal.balance} € (${GoalsViewModel.progress(goal)}%)"
        }
        fun titleString(goal: Goal): String {
            return "${goal.text} (${goal.price} €)"
        }
        fun progress(goal: Goal): Int {
            return ((goal.balance.toFloat())/goal.price*100).toInt()
        }
    }
}

class Goal {
    var price: Int
    var balance: Int = 0
    var text: String
    val id: Int
    var icon: Drawable
    var activities = mutableListOf<Activity>()

    constructor(text: String, price: Int, activityName: String, activityEarnings: Int) {
        this.price = price
        this.text = text
        // TODO icon =
        id = nextId++
        activities.add(Activity(activityName, activityEarnings))
    }

    private companion object {
        var nextId = 1
    }

    override fun toString(): String {
        return "$text: $price €"
    }

    data class Activity(var name: String, var earnings: Int)

}

fun <T> MutableLiveData<T>.notifyObserver() {
    this.value = this.value
}

class GoalsViewModelFactory(
    private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GoalsViewModel::class.java)) {
            return GoalsViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}