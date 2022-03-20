package com.github.sdpsharelook.Section

var sectionList = mutableListOf<Section>()

val SECTION_ID = "sectionExtra"

class Section (
    var title: String,
    var flag: Int,
    val id: Int? = sectionList.size
)
