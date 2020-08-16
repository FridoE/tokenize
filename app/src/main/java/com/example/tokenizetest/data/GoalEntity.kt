package com.example.tokenizetest.data

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import java.util.*

@Entity
data class GoalEntity(
    @PrimaryKey var goalID: Long,
    var name: String,
    var price: Int,
    var balance: Int
)

@Entity(foreignKeys = [ForeignKey(entity = GoalEntity::class,
        parentColumns = ["goalId"],
    childColumns = ["goalId"],
    onDelete = CASCADE)])
data class ActivityEntity(
    @PrimaryKey var activityID: Long,
    var goalID: Long,
    var name: String,
    var earnings: Int
)


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

    @Relation(parentColumn = "goalAndActivityID", entityColumn = "goalAndActivityID", entity = ActivityHistoryEntity::class)
    var activityhistoryentities: List<ActivityHistoryEntity>? = null
}

class GoalAndActivities {
    @Embedded
    var goal: GoalEntity? = null

    @Relation(parentColumn = "goalID", entityColumn = "goalID", entity = ActivitiesAndActivityHistoryEntities::class)
    var activities: List<ActivitiesAndActivityHistoryEntities>? = null
}

interface GoalAndActivitiesDao {
    @Insert
    fun insertGoalAndActivities(vararg goalandact: GoalAndActivities)

    @Update
    fun updateGoalAndActivities(vararg goalandact: GoalAndActivities)

    @Delete
    fun deleteGoalAndActivities(vararg goalandact: GoalAndActivities)

    @Query("SELECT * from GoalEntity")
    fun loadAllGoalsAndActivities(): Array<GoalAndActivities>

    @Query("SELECT * from GoalEntity WHERE goalID= :goalID")
    fun loadGoalAndActivity(goalID: Long): GoalAndActivities
}

interface Mapper<Entity, DomainModel> {
    fun toEntity(domainmodel: DomainModel): Entity
    fun toDomainModel(entity: Entity): DomainModel
}

object GoalMapper: Mapper<GoalAndActivities, Goal> {
    override fun toEntity(domainmodel: Goal): GoalAndActivities {
        var entity = GoalAndActivities()
        var activityEntityList = mutableListOf<ActivitiesAndActivityHistoryEntities>()

        var actAndActHistory = ActivitiesAndActivityHistoryEntities()
        var activityHistoryEntities = mutableListOf<ActivityHistoryEntity>()

        entity.goal = GoalEntity(domainmodel.id, domainmodel.name, domainmodel.price, domainmodel.balance)
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
        TODO("Not yet implemented")
    }
}