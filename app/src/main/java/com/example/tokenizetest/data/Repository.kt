// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright © 2020 F. Engel
package com.example.tokenizetest.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*


//TODO: make repository interface and create RoomRepository
class Repository private constructor(val context: Context) {
    private val _goals: MutableLiveData<MutableList<Goal>> = MutableLiveData()
    private val goalDatabase: GoalAndActivitiesDao = GoalDatabase.getInstance(context).goalDatabaseDao
    private var databaseRead = false
    val goals: LiveData<MutableList<Goal>>
        get() = _goals

    companion object : SingletonHolder<Repository, Context>(::Repository)

    init {
        _goals.value = mutableListOf<Goal>()
        //TODO: move to viewmodel
        //TODO: observe goals for new/updated items in the list?
    }

    suspend fun init() { // This is never used!!
        withContext(Dispatchers.IO) {
            GoalDatabase.getInstance(context).clearAllTables()

            var newGoal = Goal("Smartwatch", 500, "smartwatch", "Activity 1", 5)
            var secondGoal = Goal("Fernseher", 350, "reward", "Act2", 10)
            newGoal.balance = 150
            secondGoal.balance = 80
            insert(newGoal)
            insert(secondGoal)
        }
    }
    suspend fun getAllGoals() : List<Goal>{
        if(!databaseRead)
            _readAllGoalsFromDB()
        return _goals.value!!.toList()
    }
    private suspend fun _readAllGoalsFromDB() {
        var goalEntities = listOf<GoalEntity>()
        withContext(Dispatchers.IO) {
            goalEntities = goalDatabase.loadAll()
        }
        _goals.value = goalEntities.map {
            GoalMapper.toDomainModel(it) }.toMutableList()
        databaseRead = true
    }

    suspend fun insert(g: Goal) {
        if(findById(g.id) == null) {
            _goals.value?.add(g)
            val findGoal = _goals.value?.find { it.id == g.id }
            withContext(Dispatchers.IO) {
                goalDatabase.insertAll(GoalMapper.toEntity(g))
            }
            Log.d("Repository", "insert :" + findGoal?.name)
        }
    }
    suspend fun update(g: Goal) {
        _goals.value?.forEachIndexed { index, goal ->
            if(goal.id == g.id)
                _goals.value?.set(index, g)
        }
        withContext(Dispatchers.IO) {
            goalDatabase.updateAll(GoalMapper.toEntity(g))
        }
    }
    suspend fun logActivity(g: Goal, activityID: Long) {
        _goals.value?.forEachIndexed { index, goal ->
            if(goal.id == g.id)
                _goals.value?.set(index, g)
        }
        val goalEntity = GoalMapper.toEntity(g)
        withContext(Dispatchers.IO) {
            goalEntity.activities.find { it.activityID == activityID }?.historyItems.let {
                it?.takeLast(1)?.let { historyItem -> goalDatabase._insertActivitieHistoryItems(historyItem) }
            }
            goalDatabase._updateGoal(goalEntity)
        }
    }
    suspend fun removeActivityHistoryItem(g: Goal, activityID: Long, historyDate: Date) {
        _goals.value?.forEachIndexed { index, goal ->
            if(goal.id == g.id)
                _goals.value?.set(index, g)
        }
        val goalEntity = GoalMapper.toEntity(g)
        withContext(Dispatchers.IO) {
            goalDatabase._deleteActivityHistoryItems(listOf(ActivityHistoryEntity(historyDate, activityID)))
            goalDatabase._updateGoal(goalEntity)
        }
    }

    suspend fun delete(g: Goal) {
        _goals.value?.removeIf { it.id == g.id }
        withContext(Dispatchers.IO) {
            goalDatabase.deleteAll(GoalMapper.toEntity(g))
        }
    }

    fun findById(gid: Long): Goal? {
        return _goals.value?.find { it.id == gid }
    }
}

open class SingletonHolder<out T: Any, in A>(creator: (A) -> T) {
    private var creator: ((A) -> T)? = creator
    @Volatile private var instance: T? = null

    fun getInstance(arg: A): T {
        val i = instance
        if (i != null) {
            return i
        }

        return synchronized(this) {
            val i2 = instance
            if (i2 != null) {
                i2
            } else {
                val created = creator!!(arg)
                instance = created
                creator = null
                created
            }
        }
    }
}