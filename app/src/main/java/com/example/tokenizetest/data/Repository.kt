package com.example.tokenizetest.data

import android.graphics.drawable.Icon
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.tokenizetest.R

class Repository {
    lateinit var _goals: MutableLiveData<MutableList<Goal>>
    val goals: LiveData<MutableList<Goal>>
        get() = _goals

    init {
        var newGoal = Goal("Smartwatch", 500, "smartwatch.jpg", "Activity 1", 5)
        var secondGoal = Goal("Fernseher", 350, "reward.png", "Act2", 10)
        _goals.value = mutableListOf<Goal>()
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
}