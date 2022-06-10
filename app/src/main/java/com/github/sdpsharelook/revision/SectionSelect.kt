package com.github.sdpsharelook.revision

import com.github.sdpsharelook.section.Section

data class SectionSelect(
    val section: Section,
    var wordsToReview: Int = 0,
    var isChecked: Boolean = false
)