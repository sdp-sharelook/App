package com.github.sdpsharelook.revision

import com.github.sdpsharelook.section.Section

data class SectionSelect(
    val section: Section,
    var wordsToReview: Int = 0,
    val isChecked: Boolean = false
)