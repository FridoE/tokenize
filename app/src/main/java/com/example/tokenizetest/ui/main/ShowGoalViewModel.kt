package com.example.tokenizetest.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.map

class ShowGoalViewModel(val app: Application, val _goal: Goal) : AndroidViewModel(app) {

    private val _activityList = MutableLiveData<MutableList<TokenizedActivityViewModel>>()
    val activityList: LiveData<MutableList<TokenizedActivityViewModel>>
        get() = _activityList

    init {
        _activityList.value =  _goal.activities.map { TokenizedActivityViewModel(app, it) }.toMutableList()
    }
}

class TokenizedActivityViewModel(
    val app: Application,
    var _activity: Goal.TokenizedActivity
) : AndroidViewModel(app) {
    val titleString = _activity.name

}