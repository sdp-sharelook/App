package com.github.sdpsharelook.section

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdpsharelook.Word
import com.github.sdpsharelook.storage.IRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import java.util.*

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



}