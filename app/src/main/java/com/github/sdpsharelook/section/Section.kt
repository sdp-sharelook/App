package com.github.sdpsharelook.section

import com.github.sdpsharelook.storage.IRepository
import com.github.sdpsharelook.storage.RTDBWordListRepository
import java.util.*

var sectionList = mutableListOf<Section>()


class Section(
    var title: String,
    var flag: Int,
    val databaseRepo: RTDBWordListRepository,
    val sectionRepo: String,
    val sectionSize: Int = sectionList.size,
    val id : String = UUID.randomUUID().toString()
){

    override fun equals(other: Any?): Boolean {
        var section = other as Section
        return sectionRepo == section.sectionRepo
    }
}