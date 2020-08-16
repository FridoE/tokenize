package com.example.tokenizetest.data

import android.graphics.drawable.Icon
import android.icu.util.Calendar
import android.util.Log
import androidx.lifecycle.MutableLiveData
import java.util.*

class Goal(
    text: String = "",
    var price: Int = 0,
    var iconName: String = "",
    activityName: String = "",
    activityEarnings: Int = 0
) {
    var balance: Int = 0
    var name: String = text
    val id: Long
    var goalReached = false
    var activities = mutableListOf<TokenizedActivity>()

    init {
        id = nextId++
        activities.add(TokenizedActivity(activityName, activityEarnings))
    }
    /*constructor(g: Goal): this(g.name, g.price, g.icon, "",0) {
        activities = g.activities
        balance = g.balance
    }*/

    private companion object {
        var nextId = 1
    }

    override fun toString(): String {
        return "$name: $price €"
    }

    fun logActivity(activityId : Int, date: Date = Calendar.getInstance().time) {
        activities.find{it.id == activityId}?.let {
            it.log?.add(date)
            Log.d("tokenizedactivity", "created activity id: "+id.toString())
            balance += it.earnings
            if(balance >= price)
                goalReached = true
        }
    }

    fun removeActivityFromLog(activityID: Int, date: Date) {
        activities.find{it.id == activityID}?.let {
            it.log.remove(date)
            balance -= it.earnings
            if(balance < price)
                goalReached = false
        }
    }

    val activityHistoryMap = activities.map { mapOf(it to it.log)} //.flatten().toMutableList()

    data class TokenizedActivity(var name: String, var earnings: Int) {
        var log: MutableList<Date> = mutableListOf<Date>()
        val id = nextId++
        private companion object {var nextId = 1L}
    }
}

fun <T> MutableLiveData<T>.notifyObserver() {
    this.value = this.value
}
