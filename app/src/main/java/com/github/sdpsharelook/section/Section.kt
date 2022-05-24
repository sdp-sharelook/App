package com.github.sdpsharelook.section

import androidx.annotation.Keep
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.serialization.Serializable
import java.util.*
@IgnoreExtraProperties
@Serializable
data class Section(
    var title: String ="",
    var flag: Int=0,
    var sectionRepo: String="",
    val id : String= ""
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "title" to title,
            "flag" to flag,
            "sectionRepo" to sectionRepo,
            "id" to id
        )
    }
    constructor(id:String) : this("",0,"", id)
}