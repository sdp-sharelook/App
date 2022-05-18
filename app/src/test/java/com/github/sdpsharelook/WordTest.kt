package com.github.sdpsharelook

import org.junit.Test

class WordTest {

    @Test
    fun `test Word toMap`() {
        val (uid, source, sourceLanguage, target, targetLanguage, location, savedDate, picture, isFavourite) = Word.testWord
        val map = Word.testWord.toMap()
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
    fun `test dbWord toMap`() {
        val (_uid, _source, _sourceLanguage, _target, _targetLanguage, _location, _savedDate, _picture, _isFavourite) = Word.testWord
        val word = DBWord(
            _source!!,
            _sourceLanguage!!.tag,
            _target!!,
            _targetLanguage!!.tag,
            _location.toString(),
            _savedDate.toString(),
            _picture!!,
            _uid,
            _isFavourite!!
        )
        val (source, sourceLanguage, target, targetLanguage, location, savedDate, picture, uid, isFavourite) = word
        val map = word.toMap()
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
}