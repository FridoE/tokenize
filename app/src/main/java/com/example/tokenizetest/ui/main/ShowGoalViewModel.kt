package com.example.tokenizetest.ui.main

import android.app.Application
import android.icu.util.Calendar
import android.util.Log
import androidx.lifecycle.*

class ShowGoalViewModel(val app: Application, val _goal: Goal) : AndroidViewModel(app) {
    private val _activityVMList = MutableLiveData<MutableList<TokenizedActivityViewModel>>()
    private val _activityHistoryList = MutableLiveData<MutableList<ActivityHistoryListItemVM>>()
    val activityList: LiveData<MutableList<TokenizedActivityViewModel>>
        get() = _activityVMList
    val activityHistoryList: LiveData<MutableList<ActivityHistoryListItemVM>>
        get() = _activityHistoryList

    init {
        _activityVMList.value =  _goal.activities.map { TokenizedActivityViewModel(app, it)}.toMutableList()
        _activityHistoryList.value = _goal.activities.map {ac -> ac.log.map { ActivityHistoryListItemVM(ac, it) }}.flatten().toMutableList()
    }

    fun doneActivity(activityVM: TokenizedActivityViewModel) {
        _goal.logActivity(activityVM.id, Calendar.getInstance().time)
        _activityHistoryList.value = _goal.activities.map {ac -> ac.log.map { ActivityHistoryListItemVM(ac, it) }}.flatten().toMutableList()
    }
}

class ShowGoalViewModelFactory(
    private val application: Application, val goal: Goal
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShowGoalViewModel::class.java)) {
            return ShowGoalViewModel(application, goal) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class TokenizedActivityViewModel(
    val app: Application,
    var _tokenizedActivity: Goal.TokenizedActivity
) : AndroidViewModel(app) {
    val titleString = "${_tokenizedActivity.name}: ${_tokenizedActivity.earnings} €"
    val totalEarningsString = "You have earned a total of ${_tokenizedActivity.log.count()*_tokenizedActivity.earnings} € by doing this activity"
    val remainingString = ""
    val id = _tokenizedActivity.id
}
