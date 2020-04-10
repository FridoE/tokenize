package com.example.tokenizetest.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*

class GoalsViewModel(app: Application): AndroidViewModel(app) {

    private val _goalsList = MutableLiveData<MutableList<Goal>>()
    val goalsList : LiveData<MutableList<Goal>>
            get() = _goalsList

    init{
        _goalsList.value = mutableListOf<Goal>()

        var newGoal = Goal("Smartwatch", 500)
        newGoal.balance = 150
        addGoal(newGoal)
        var secondGoal = Goal("Fernseher", 350)
        secondGoal.balance = 80
        addGoal(secondGoal)
        Log.d("goalsviewmodel", "init")
    }

    fun addGoal(goal: Goal) = _goalsList.value?.add(goal)

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
    val price: Int
    var balance: Int = 0
    val text: String
    val id: Int

    constructor(text: String, price: Int) {
        this.price = price
        this.text = text
        id = nextId++
    }

    private companion object {
        var nextId = 1
    }

    override fun toString(): String {
        return "$text: $price €"
    }
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