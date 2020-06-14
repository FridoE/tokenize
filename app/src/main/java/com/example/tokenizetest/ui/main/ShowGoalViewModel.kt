package com.example.tokenizetest.ui.main

import android.app.Application
import androidx.lifecycle.*

class ShowGoalViewModel(val app: Application, val _goal: Goal) : AndroidViewModel(app) {

    private val _activityList = MutableLiveData<MutableList<TokenizedActivityViewModel>>()
    val activityList: LiveData<MutableList<TokenizedActivityViewModel>>
        get() = _activityList

    init {
        _activityList.value =  _goal.activities.map { TokenizedActivityViewModel(app, it) }.toMutableList()
    }
    fun activityDone(activityVM : TokenizedActivityViewModel) {

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
    var doneActivity: Boolean = false
}
