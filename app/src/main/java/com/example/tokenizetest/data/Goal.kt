// SPDX-License-Identifier: GPL-3.0-or-later
// Copyright © 2020 F. Engel
package com.example.tokenizetest.data

import android.graphics.drawable.Icon
import android.icu.util.Calendar
import android.util.Log
import androidx.lifecycle.MutableLiveData
import java.util.*

class Goal {
    var balance: Int = 0
    var name: String
    var price: Int = 0
    var iconName: String = ""
    val id: Long
    var goalReached = false
    var activities = mutableListOf<TokenizedActivity>()

    constructor(name: String, price: Int, iconName: String, activityName: String = "", activityEarnings: Int = 0) {
        this.id = nextId++
        this.name = name
        this.price = price
        this.iconName = iconName
        this.goalReached = balance >= price
        if(activityName != "")
            activities.add(TokenizedActivity(activityName, activityEarnings))
    }
    constructor(id: Long, name: String, price: Int, balance: Int, iconName: String, activityName: String = "", activityEarnings: Int = 0) {
        this.id = id
        nextId = id+1
        this.name = name
        this.balance = balance
        this.price = price
        this.iconName = iconName
        this.goalReached = balance >= price
        if(activityName != "")
            activities.add(TokenizedActivity(activityName, activityEarnings))
    }
    /*constructor(g: Goal): this(g.name, g.price, g.icon, "",0) {
        activities = g.activities
        balance = g.balance
    }*/

    private companion object {
        var nextId = 1L //TODO:
    }

    override fun toString(): String {
        return "$name: $price €"
    }

    fun logActivity(activityId : Long, date: Date = Calendar.getInstance().time) {
        activities.find{it.id == activityId}?.let {
            it.log?.add(date)
            Log.d("tokenizedactivity", "created activity id: "+id.toString())
            balance += it.earnings
            if(balance >= price)
                goalReached = true
        }
    }

    fun removeActivityFromLog(activityID: Long, date: Date) {
        activities.find{it.id == activityID}?.let {
            it.log.remove(date)
            balance -= it.earnings
            if(balance < price)
                goalReached = false
        }
    }

    val activityHistoryMap = activities.map { mapOf(it to it.log)} //.flatten().toMutableList()

    class TokenizedActivity {
        var name: String
        var earnings: Int
        var log: MutableList<Date> = mutableListOf<Date>()
        val id: Long
        constructor(name: String, earnings: Int) {
            id = nextId++
            this.name = name
            this.earnings = earnings
        }
        constructor(id: Long, name: String, earnings: Int) {
            this.id = id
            nextId = id+1
            this.name = name
            this.earnings = earnings
        }
        private companion object {var nextId = 1L}
    }
}

fun <T> MutableLiveData<T>.notifyObserver() {
    this.value = this.value
}
