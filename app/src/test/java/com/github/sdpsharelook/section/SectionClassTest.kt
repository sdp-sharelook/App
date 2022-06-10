package com.github.sdpsharelook.section

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdpsharelook.Word
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import org.junit.Assert
import org.junit.Assert.assertFalse
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class SectionClassTest {

    @Test
    fun toMapTest() {
        val section = Section(title = "Cuisine", flag = 0, id = "id")
        val sectionMap = section.toMap()
        assert(sectionMap["title"] == "Cuisine")
        assert(sectionMap["flag"] == 0)
        assert(sectionMap["id"] == "id")
    }

    @Test
    fun equals() {
        val section = Section(title = "Cuisine", flag = 0, id = "id")
        val otherSection = Section(title = "Cuisine", flag = 0, id = "id1")
        val sectionEdited = Section(title = "Maison", flag = 0, id = "id")
        assert(section == sectionEdited)
        assertFalse(section.equals(Word(source= "Hola", target = "Bonjour")))
        assertFalse(section == otherSection)
    }

    @Test
    fun setTitleFlag() {
        val section = Section(title = "Cuisine", flag = 0, id = "id")
        section.title = "Chambre"
        section.flag = 2
        assert(section.title == "Chambre")
        assert(section.flag == 2)
    }

    @Test
    fun `test section serialization`() {
        val sec = Section(title = "Cuisine", flag = 0, id = "id")
        val s = Json.encodeToString(Section.serializer(), sec)
        val decoded = Json.decodeFromString(Section.serializer(), s)
        Assert.assertEquals(sec, decoded)
    }

    @Test
    fun `test default section serialization`() {
        val sec = Section()
        val s = Json.encodeToString(Section.serializer(), sec)
        val decoded = Json.decodeFromString(Section.serializer(), s)
        Assert.assertEquals(sec, decoded)


    }

    @Test
    fun `test Json serialization`() {
        val sec = Section()
        val s = Json.encodeToJsonElement(Section.serializer(), sec)
        val decoded = Json.decodeFromJsonElement<Section>(s)
        Assert.assertEquals(sec, decoded)
    }


}