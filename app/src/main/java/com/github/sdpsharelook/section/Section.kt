package com.github.sdpsharelook.section

import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.serialization.Serializable
import java.lang.Exception
@IgnoreExtraProperties
@Serializable
data class Section(
    var title: String ="",
    var flag: Int=0,
    val id : String= ""
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "title" to title,
            "flag" to flag,
            "id" to id
        )
    }
    constructor(id:String) : this("",0, id)

    override fun equals(other: Any?): Boolean {
        return try {
            val otherSection = (other as Section)
            id == otherSection.id
        }catch (e: Exception){
            false
        }
    }
}