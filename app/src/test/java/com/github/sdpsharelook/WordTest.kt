package com.github.sdpsharelook

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Test

class WordTest {

    @Test
    fun `test Word toMap`() {
        val (uid, source, sourceLanguage, target, targetLanguage, location, savedDate, picture, isFavourite) = Word.testWord
        val map = Word.testWord.toMap()
        assert(map.containsValue(uid))
        assert(map.containsValue(source))
        println(map)
        assert(map.containsValue(sourceLanguage))
        assert(map.containsValue(target))
        assert(map.containsValue(targetLanguage))
        assert(map.containsValue(location))
        assert(map.containsValue(savedDate))
        assert(map.containsValue(picture))
        assert(map.containsValue(isFavourite))
    }


    @Test
    fun `test Word setters and getters`() {
        val testWordCpy = Word.testWord.copy()
        testWordCpy.apply {
            location = Position(location!!.latitude, location!!.longitude)
            uid
            source
            sourceLanguage
            target
            targetLanguage
            location
            savedDate
            picture
            isFavourite
        }
        val (uid, source, sourceLanguage, target, targetLanguage, location, savedDate, picture, isFavourite) = testWordCpy
        val map = testWordCpy.toMap()
        assert(map.containsValue(uid))
        assert(map.containsValue(source))
        assert(map.containsValue(sourceLanguage))
        assert(map.containsValue(target))
        assert(map.containsValue(targetLanguage))
        assert(map.containsValue(location))
        assert(map.containsValue(savedDate))
        assert(map.containsValue(picture))
        assert(map.containsValue(isFavourite))
    }

    @Test
    fun serializeTest() {
        var testWordCpy = Word.testWord.copy()

        var stringWord = Json.encodeToString(testWordCpy)
        var word = Json.decodeFromString<Word>(stringWord)
        assert(testWordCpy == word)

        val w = Word()
        stringWord = Json.encodeToString(w)
        word = Json.decodeFromString<Word>(stringWord)
        assert(w == word)
    }


}