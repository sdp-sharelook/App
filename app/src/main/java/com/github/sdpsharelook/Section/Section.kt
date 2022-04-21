package com.github.sdpsharelook.Section

import com.github.sdpsharelook.storage.RTDBWordListRepository
import com.github.sdpsharelook.storage.RTDBWordSectionRepository

var sectionList = mutableListOf<Section>()

val SECTION_ID = "sectionExtra"

class Section(
    var title: String,
    var flag: Int,
    val databaseRepo: RTDBWordListRepository,//RTDBWordSectionRepository,
    val sectionRepo: String,
    val id: Int? = sectionList.size
){
    override fun equals(other: Any?): Boolean {
        var section = other as Section
        return sectionRepo == section.sectionRepo
    }
}

