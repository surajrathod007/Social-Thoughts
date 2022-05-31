package com.surajrathod.socialthoughts.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Programs(
     val title : String = " ",
     val content : String = " ",
     val sem : Int = 0,
     val unit : Int = 0,
     val subject : String = " "







) : Parcelable
