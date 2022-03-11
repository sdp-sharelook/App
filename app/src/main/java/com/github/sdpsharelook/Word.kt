package com.github.sdpsharelook

class Word(
    val name: String,
    val definition: String,
    val isFavourite: Boolean = false,
    val themes: Set<String> = emptySet()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Word

        return name == other.name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    override fun toString(): String {
        return "Word(${if (isFavourite) "fav/" else ""}$name=${definition.take(70)}${if (definition.length > 70) "..." else ""})"
    }

    fun isRelatedTo(other: Word): Int {
        return themes.intersect(other.themes).size
    }
}
