package com.github.sdpsharelook.language

import android.content.Context
import com.github.sdpsharelook.R
import com.google.firebase.encoders.annotations.Encodable
import com.google.firebase.encoders.annotations.Encodable.*
import com.google.gson.annotations.Expose
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Language(
    val tag: String = "",
) {

    @delegate:Transient
    val displayName: String by lazy { locale?.displayName ?: AUTO_TAG }


    @Transient
    @Contextual
    val locale:  Locale? = Locale.forLanguageTag(tag)
    /**@param ctx [Context] : the context of the app
     * return [Int] : the id of the flag or 0 if it doesn't exists
     */
    fun flagId(ctx: Context): Int? {
        val flagId = when (tag) {
            AUTO_TAG -> R.drawable.ic_auto
            else -> {
                ctx.resources.getIdentifier(
                    tag,
                    "raw",
                    ctx.packageName
                )
            }
        }
        return when (flagId) {
            0 -> null
            else -> flagId
        }
    }

    companion object {
        const val AUTO_TAG: String = "auto"

        @delegate:Transient
        val default by lazy { Language(Locale.getDefault().toLanguageTag()) }
        val auto = Language(AUTO_TAG)
    }

}