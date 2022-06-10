package com.github.sdpsharelook

import com.google.android.gms.maps.model.LatLng
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
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
    fun `test word serialization`() {
        val w = Word.testWord
        val s = Json.encodeToString(Word.serializer(), w)
        val decoded = Json.decodeFromString(Word.serializer(), s)
        assertEquals(w,decoded)
    }

    @Test
    fun `test default word serialization`() {
        val w = Word()
        val s = Json.encodeToString(Word.serializer(), w)
        val decoded = Json.decodeFromString(Word.serializer(), s)
        assertEquals(w,decoded)
    }
}