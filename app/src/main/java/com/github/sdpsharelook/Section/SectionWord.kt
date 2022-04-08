package com.github.sdpsharelook.Section

import java.io.Serializable

class SectionWord(var sourceText: String, var translatedText: String): Serializable {

    fun toList():List<String>{
        return listOf(sourceText, translatedText)
    }
}