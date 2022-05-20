package com.github.sdpsharelook

data class DBWord(
    val source: String = "",
    val sourceLanguage: String = "",
    val target: String = "",
    val targetLanguage: String = "",
    val location: String? = "",
    val savedDate: String? = "",
    val picture: String? = "",
    val uid: String = "",
    val isFavourite: Boolean = false,
) {
    // fun synonyms(): Set<Word> = TODO("not implemented yet")
    // ...
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "source" to source,
            "sourceLanguage" to sourceLanguage,
            "targetLanguage " to targetLanguage,
            "location" to location,
            "savedDate" to savedDate,
            "pictureUrl" to picture,
            "isFavorite" to isFavourite
        )
    }

    constructor() : this("", "", "", "", "", "", "", "", false)
}