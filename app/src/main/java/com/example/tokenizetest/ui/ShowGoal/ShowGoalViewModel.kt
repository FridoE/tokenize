package com.example.tokenizetest.ui.showgoal

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.tokenizetest.data.Goal
import com.example.tokenizetest.data.Repository
import com.example.tokenizetest.ui.main.GoalsListItemViewModel
import com.example.tokenizetest.data.notifyObserver

class ShowGoalViewModel(val app: Application, val goalID: Int) : AndroidViewModel(app) {
    private val _activityVMList = MutableLiveData<MutableList<TokenizedActivityViewModel>>()
    private val _activityHistoryList = MutableLiveData<MutableList<ActivityHistoryListItemVM>>()

    val activityList: LiveData<MutableList<TokenizedActivityViewModel>>
        get() = _activityVMList
    val activityHistoryList: LiveData<MutableList<ActivityHistoryListItemVM>>
        get() = _activityHistoryList

    var _goalReached = MutableLiveData<Boolean>(false)
    val goalReached: MutableLiveData<Boolean>
        get() = _goalReached

    private var _goal: Goal = Repository.findById(goalID)?: Goal()
    val goalIconName = _goal.iconName

    var _longPressOnHistoryItem  = MutableLiveData<Boolean>(false)
    val longPressOnHistoryItem: MutableLiveData<Boolean>
        get() = _longPressOnHistoryItem

    init {
        _activityVMList.value =  _goal.activities.map {
            TokenizedActivityViewModel(
                app,
                it
            )
        }.toMutableList()
        _activityHistoryList.value = _goal.activities.map {ac -> ac.log.map {
            ActivityHistoryListItemVM(
                ac,
                it
            )
        }}.flatten().toMutableList()
    }

    fun doneActivity(activityVM: TokenizedActivityViewModel) {
        val activity = _goal.activities.find { it.id==activityVM.id }
        activity?.let {
            _goal.logActivity(activityVM.id)
            if(_goal.goalReached) _goalReached.value = true

            _activityHistoryList.value?.add(
                ActivityHistoryListItemVM(
                    it,
                    it.log.last()
                )
            )
            _activityHistoryList.notifyObserver()
            Repository.update(_goal)
        }
    }

    fun removeActivityHistoryItem(ahvm: ActivityHistoryListItemVM) {
        //TODO: ask for longpress active
        _goal.removeActivityFromLog(ahvm.act.id, ahvm.date)
        _activityHistoryList.value?.remove(ahvm)
        Log.d("deleteHI", "showgoalviewmodel")
        _activityHistoryList.notifyObserver()
        _goalReached.value = _goal.goalReached
        Repository.update(_goal)
    }

    val titleGoal:String
            get() = _goal.name //_goalsListItemVM.name
    val priceText: String
            get() = "Price :${_goal.price} €"
    val progress: Int
            get() = (_goal.balance.toFloat() / _goal.price * 100).toInt()
    val balanceText : String
            get() = "Balance: ${_goal.balance} € (${progress} %)"
}

class ShowGoalViewModelFactory(
    private val application: Application, val goalID: Int
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShowGoalViewModel::class.java)) {
            return ShowGoalViewModel(application, goalID) as T
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
