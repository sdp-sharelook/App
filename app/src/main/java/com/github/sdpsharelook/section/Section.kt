package com.github.sdpsharelook.section

import com.github.sdpsharelook.storage.RTDBWordListRepository

var sectionList = mutableListOf<Section>()

val SECTION_ID = "sectionExtra"

class Section(
    var title: String,
    var flag: Int,
    val databaseRepo: RTDBWordListRepository,
    val sectionRepo: String,
    val id: Int = sectionList.size
)
