package com.github.sdpsharelook.section

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
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
import org.robolectric.RuntimeEnvironment
import com.github.sdpsharelook.R
import com.github.sdpsharelook.Word
import org.mockito.kotlin.*

@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class CardAdapterTest {
    private lateinit var adapter: CardAdapter
    private lateinit var holder: CardViewHolder
//    private var mockDatabaseRepo : IRepository<List<String>> = mock()
//    private var section: Section = Section("test", 1, "test" )
//
//    private var clickListenerMock: SectionClickListener = mock()
//    private var dialogMock: Dialog = mock()
//    private var wordRTDBMock : IRepository<List<Word>> = mock()

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun itemCount() {
        // TODO inject in sectionList listOf(section, section, section)
//        adapter = CardAdapter(clickListenerMock, dialogMock, wordRTDBMock)
//        assert(adapter.itemCount == 3)
        // TODO Edouard please fix call of newly changed constructor of CardAdapter
    }

    @Test
    fun onBindViewHolder() {
//        val inflater = RuntimeEnvironment.application.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        adapter = CardAdapter(clickListenerMock, dialogMock, wordRTDBMock)
//        val cardBinding = CardSectionBinding.inflate(inflater)
//        holder = CardViewHolder(cardBinding, mock())
//        adapter.notifyDataSetChanged()
//        adapter.onBindViewHolder(holder, 1)
//        assert(cardBinding.sectionTitle.text == "test")
//        holder.onEditClick
//        holder.onDeletClick
//        holder.itemView.findViewById<ImageButton>(R.id.editButton).performClick()
//        assert(edit)
//        holder.itemView.findViewById<ImageButton>(R.id.deleteButton).performClick()
//        verify(wordRTDBMock).deleteSection(section)

        // TODO verify mock delete call
        // TODO
    }


}

