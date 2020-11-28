// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright © 2020 F. Engel
package com.example.tokenizetest.data

import android.app.Activity
import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import java.util.*

@Entity
data class GoalEntity(
    @PrimaryKey var goalID: Long,
    var name: String,
    var price: Int,
    var balance: Int,
    var iconName: String
) {
    //constructor(goalID: Long, name: String, price: Int, balance: Int, iconName: String, activities: List<ActivityEntity>) : this(goalID, name, price, balance, iconName, listOf<ActivityEntity>())
    @Ignore
    var activities: List <ActivityEntity> = listOf<ActivityEntity>()
}

@Entity(foreignKeys = [ForeignKey(entity = GoalEntity::class,
        parentColumns = ["goalID"],
    childColumns = ["goalID"],
    onDelete = CASCADE)])
data class ActivityEntity(
    @PrimaryKey var activityID: Long,
    var goalID: Long,
    var name: String,
    var earnings: Int
) {
    @Ignore
    var historyItems: List <ActivityHistoryEntity> = listOf<ActivityHistoryEntity>() //TODO: https://stackoverflow.com/questions/44667160/android-room-insert-relation-entities-using-room
}


@Entity(
    foreignKeys = [ForeignKey(
        entity = ActivityEntity::class,
        parentColumns = ["activityID"],
        childColumns = ["activityID"],
        onDelete = CASCADE
    )]
)
data class ActivityHistoryEntity(
    @PrimaryKey
    var date: Date,
    var activityID: Long
)

class ActivitiesAndActivityHistoryEntities {
    @Embedded
    var activity: ActivityEntity? = null

    @Relation(parentColumn = "activityID", entityColumn = "activityID", entity = ActivityHistoryEntity::class)
    var activityhistoryentities: List<ActivityHistoryEntity>? = null
}

class GoalAndActivities {
    @Embedded
    var goal: GoalEntity? = null

    @Relation(parentColumn = "goalID", entityColumn = "goalID", entity = ActivityEntity::class)
    var activities: List<ActivitiesAndActivityHistoryEntities>? = null
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date): Long {
        return date.time.toLong()
    }
}

interface Mapper<Entity, DomainModel> {
    fun toEntity(domainmodel: DomainModel): Entity
    fun toDomainModel(entity: Entity): DomainModel
}

/*object GoalMapper: Mapper<GoalAndActivities, Goal> {
    override fun toEntity(domainmodel: Goal): GoalAndActivities {
        var entity = GoalAndActivities()
        var activityEntityList = mutableListOf<ActivitiesAndActivityHistoryEntities>()

        var actAndActHistory = ActivitiesAndActivityHistoryEntities()
        var activityHistoryEntities = mutableListOf<ActivityHistoryEntity>()

        entity.goal = GoalEntity(domainmodel.id, domainmodel.name, domainmodel.price, domainmodel.balance, domainmodel.iconName)
        domainmodel.activities.forEach {
            actAndActHistory.activity = ActivityEntity(it.id, domainmodel.id, it.name, it.earnings)
            activityHistoryEntities = mutableListOf<ActivityHistoryEntity>()
            for (date in it.log) {
                activityHistoryEntities.add(ActivityHistoryEntity(date, it.id))
            }
            actAndActHistory.activityhistoryentities = activityHistoryEntities.toList()
            activityEntityList.add(actAndActHistory)
        }
        entity.activities = activityEntityList.toList()
        return entity
    }

    override fun toDomainModel(entity: GoalAndActivities): Goal {
        var goal = Goal("Error", 100, "error")
        goal = entity.goal?.let {
            Goal(it.goalID, it.name, it.price, it.balance, it.iconName)
        } ?: goal
        entity.activities?.forEach { activityAndHistoryItem ->
            activityAndHistoryItem.activity?.let {
                val activity = Goal.TokenizedActivity(it.activityID,it.name, it.earnings)
                activityAndHistoryItem.activityhistoryentities?.forEach { historyItem ->
                    activity.log.add(historyItem.date)
                }
                goal.activities.add(activity)
            }
        }
        return goal
    }
}*/

object GoalMapper: Mapper<GoalEntity, Goal> {
    override fun toEntity(domainmodel: Goal): GoalEntity {
        var entity = GoalEntity(domainmodel.id, domainmodel.name, domainmodel.price, domainmodel.balance, domainmodel.iconName)
        entity.activities = domainmodel.activities.map {tokenAct ->
            ActivityEntity(tokenAct.id, domainmodel.id, tokenAct.name, tokenAct.earnings).apply {
                historyItems = tokenAct.log.map { ActivityHistoryEntity(it, tokenAct.id)  }
            }
        }
        return entity
    }

    override fun toDomainModel(entity: GoalEntity): Goal {
        var goal = Goal(entity.goalID, entity.name, entity.price, entity.balance, entity.iconName)
        goal.activities = entity.activities.map {act ->
            Goal.TokenizedActivity(act.activityID, act.name, act.earnings).apply {
                log = act.historyItems.map { it.date }.toMutableList()
            }
        }.toMutableList()
        return goal
    }
}
//
//object ActivityMapper: Mapper<ActivityEntity, Goal.TokenizedActivity> {
//    override fun toEntity(domainmodel: Goal.TokenizedActivity): ActivityEntity {
//        var entity = ActivityEntity(domainmodel.id, domainmodel.
//
//    }
//
//    override fun toDomainModel(entity: ActivityEntity): Goal.TokenizedActivity {
//        TODO("Not yet implemented")
//    }
//}

//TODO: Add Mappers for activities and ActivitiesAndActivityHistoryItems