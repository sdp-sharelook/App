package com.github.sdpsharelook.section

import android.content.Context
import android.view.LayoutInflater
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdpsharelook.databinding.CardSectionBinding
import com.github.sdpsharelook.storage.IRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.robolectric.RuntimeEnvironment

@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class CardAdapterTest {
    private lateinit var adapter: CardAdapter
    private lateinit var holder: CardViewHolder
    private var mockDatabaseRepo : IRepository<List<String>> = mock()
    private var section: Section = Section("test", 1, mockDatabaseRepo, "test" )

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Before
    @Throws(Exception::class)
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun itemCount() {
        adapter = CardAdapter(listOf(section, section, section) as MutableList<Section>, mock(), mock())
        assert(adapter.getItemCount().equals(3))
    }

    @Test
    fun onBindViewHolder_setsTextAndClickEventForCandyView() {
        val inflater = RuntimeEnvironment.application.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        adapter = CardAdapter(listOf(section, section, section) as MutableList<Section>, mock(), mock())
        val cardBinding = CardSectionBinding.inflate(inflater)
        holder = CardViewHolder(cardBinding, mock())

        adapter.onBindViewHolder(holder, 1)

        assert(cardBinding.sectionTitle.text == "test")
    }


}

