// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright © 2020 F. Engel
package com.example.tokenizetest.data

import android.content.Context
import androidx.room.*
import java.lang.Exception
import java.time.LocalDateTime

@Dao
abstract class GoalAndActivitiesDao {
    @Transaction
    @Insert
    abstract fun _insertGoal(goalentity: GoalEntity)

    @Transaction
    @Insert
    abstract fun _insertActivities(actentity: List<ActivityEntity>)

    @Transaction
    @Insert
    abstract fun _insertActivitieHistoryItems(actentities: List<ActivityHistoryEntity>)

    fun insertAll(goals: List<GoalEntity>) {
        goals.forEach{ goal ->
            insertAll(goal)
        }
    }
    fun insertAll(goal: GoalEntity) {
        _insertGoal(goal)
        _insertActivities(goal.activities)
        goal.activities.forEach { act ->
           _insertActivitieHistoryItems(act.historyItems)
        }
    }


    @Transaction
    @Update
    abstract fun _updateGoal(goal: GoalEntity)

    @Transaction
    @Update
    abstract fun _updateActivities(activityEntities: List<ActivityEntity>)

    @Transaction
    @Update
    abstract fun _updateActivityHistoryItems(historyItems: List<ActivityHistoryEntity>)

     fun updateAll(goal: GoalEntity) {
        _updateGoal(goal)
        _updateActivities(goal.activities)
        goal.activities.forEach { _updateActivityHistoryItems(it.historyItems) }
    }

    @Transaction
    @Delete
    abstract fun _deleteGoal(goal: GoalEntity)

    @Transaction
    @Delete
    abstract fun _deleteActivities(activityEntities: List<ActivityEntity>)

    @Transaction
    @Delete
    abstract fun _deleteActivityHistoryItems(historyItems: List<ActivityHistoryEntity>)

    fun deleteAll(goal: GoalEntity) {
        goal.activities.forEach { _deleteActivityHistoryItems(it.historyItems) }
        _deleteActivities(goal.activities)
        _deleteGoal(goal)
    }
    
    @Transaction
    @Query("SELECT * from GoalEntity")
    abstract fun _loadGoalAndActivities(): List<GoalAndActivities>

    @Transaction
    @Query("SELECT * from GoalEntity WHERE goalID= :goalID")
    abstract fun _loadActivitiesForGoal(goalID: Long): GoalAndActivities

    fun loadAll(): List<GoalEntity> {
        var goals = mutableListOf<GoalEntity>()
        var goalAndAct = _loadGoalAndActivities()
        goalAndAct.forEach {goalAnd ->
            var goal = goalAnd.goal ?: GoalEntity(0, "error", 0, 0, "")
            goalAnd.activities?.let { activities ->
                goal.activities = activities.map {
                    (it.activity ?: ActivityEntity(
                        0,
                        0,
                        "error",
                        0)).apply {
                        historyItems = it.activityhistoryentities ?: listOf<ActivityHistoryEntity>()
                    }
                }
                /*activities.forEach { act ->
                    goal.activities.find { it.activityID == act.activity?.activityID }?.historyItems =
                        act.activityhistoryentities ?: listOf<ActivityHistoryEntity>()
                } */
            }
            goals.add(goal)
        }
        return goals
    }
}

@Database(entities = [GoalEntity::class, ActivityEntity::class, ActivityHistoryEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class GoalDatabase : RoomDatabase() {

    abstract val goalDatabaseDao: GoalAndActivitiesDao

    companion object {
        @Volatile
        private var INSTANCE: GoalDatabase? = null

        fun getInstance(context: Context): GoalDatabase {
            // Multiple threads can ask for the database at the same time, ensure we only initialize
            // it once by using synchronized. Only one thread may enter a synchronized block at a
            // time.
            synchronized(this) {
                // Copy the current value of INSTANCE to a local variable so Kotlin can smart cast.
                // Smart cast is only available to local variables.
                var instance = INSTANCE
                // If instance is `null` make a new database instance.
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        GoalDatabase::class.java,
                        "goal_database"
                    )
                        // Wipes and rebuilds instead of migrating if no Migration object.
                        // Migration is not part of this lesson. You can learn more about
                        // migration with Room in this blog post:
                        // https://medium.com/androiddevelopers/understanding-migrations-with-room-f01e04b07929
                        .fallbackToDestructiveMigration()
                        .build()
                    // Assign INSTANCE to the newly created database.
                    INSTANCE = instance
                }
                // Return instance; smart cast to be non-null.
                return instance
            }
        }
    }
}

class DatabaseDuplicateIDException(message: String): Exception(message)