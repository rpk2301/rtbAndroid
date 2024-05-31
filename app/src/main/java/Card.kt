package com.example.ridethebus

import android.os.Parcel
import android.os.Parcelable

data class Card(val suit: String, val rank: Int) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(suit)
        parcel.writeInt(rank)
    }

    override fun describeContents(): Int {
        return 0
    }

    fun getImageResourceName(): String {
        val rankString = when (rank) {
            2 -> "two"
            3 -> "three"
            4 -> "four"
            5 -> "five"
            6 -> "six"
            7 -> "seven"
            8 -> "eight"
            9 -> "nine"
            10 -> "ten"
            11 -> "jack"
            12 -> "queen"
            13 -> "king"
            14 -> "ace"
            else -> rank.toString()
        }
        return "${rankString}_of_${suit}"
    }

    fun getCardValue(): Int {
        return rank
    }

    companion object CREATOR : Parcelable.Creator<Card> {
        override fun createFromParcel(parcel: Parcel): Card {
            return Card(parcel)
        }

        override fun newArray(size: Int): Array<Card?> {
            return arrayOfNulls(size)
        }
    }
}
