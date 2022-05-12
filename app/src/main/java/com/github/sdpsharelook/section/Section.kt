package com.github.sdpsharelook.section

import com.github.sdpsharelook.storage.IRepository

var sectionList = mutableListOf<Section>()


class Section(
    var title: String,
    var flag: Int,
    val databaseRepo: IRepository<List<String>>,
    val sectionRepo: String,
    val id: Int = sectionList.size
)