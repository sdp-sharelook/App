package com.github.sdpsharelook.section

var sectionList = mutableListOf<Section>()

val SECTION_ID = "sectionExtra"

class Section (
    var title: String,
    var flag: Int,
    val id: Int? = sectionList.size
)
