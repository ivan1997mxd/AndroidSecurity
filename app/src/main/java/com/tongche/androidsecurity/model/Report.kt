package com.tongche.androidsecurity.model

import android.os.Parcel
import android.os.Parcelable
import org.bson.types.ObjectId

//data class Report(
//    var name: String,
//    var score: String,
//    var riskRank: String,
//    var state: State,
//    var details: ArrayList<Result>
//)

data class Result(
    var category: String, var engine_name: String, var result: String, var method: String
) : Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(category)
        parcel.writeString(engine_name)
        parcel.writeString(result)
        parcel.writeString(method)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Result> {
        override fun createFromParcel(parcel: Parcel): Result {
            return Result(parcel)
        }

        override fun newArray(size: Int): Array<Result?> {
            return arrayOfNulls(size)
        }
    }

}

data class State(
    var harmless: String,
    var type_unsupported: String,
    var suspicious: String,
    var confirmed_timeout: String,
    var timeout: String,
    var failure: String,
    var malicious: String,
    var undetected: String
)


open class  Report(
    val id: ObjectId = ObjectId(),
    var name: String,
    var score: String,
    var riskRank: String,
    var state: State,
    var details: ArrayList<Result>
) {
    override fun toString(): String {
        return "Report [id=$id, name=$name, score=$score, riskRank=$riskRank, state=$state, partition=$details]"
    }
}