package com.example.tokenizetest.data

import android.graphics.drawable.Icon
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.tokenizetest.R

object Repository {
    private var _goals: MutableLiveData<MutableList<Goal>> = MutableLiveData()
    val goals: LiveData<MutableList<Goal>>
        get() = _goals

    init {
    _goals.value = mutableListOf<Goal>()

    var newGoal = Goal("Smartwatch", 500, "smartwatch", "Activity 1", 5)
    var secondGoal = Goal("Fernseher", 350, "reward", "Act2", 10)
    newGoal.balance = 150
    secondGoal.balance = 80
    insert (newGoal)
    insert (secondGoal)
    //TODO: observe goals for new/updated items in the list?
    }
    fun insert(g: Goal) {
        _goals.value?.add(g)
    }
    fun update(g: Goal) {
        _goals.value?.forEachIndexed { index, goal ->
            if(goal.id == g.id)
                _goals.value?.set(index, g)
        }
    }
    fun delete(g: Goal) {
        _goals.value?.removeIf { it.id == g.id }
    }

    fun findById(gid: Int): Goal? {
        return _goals.value?.find { it.id == gid }
    }
}