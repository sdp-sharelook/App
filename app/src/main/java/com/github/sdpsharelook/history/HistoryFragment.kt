package com.github.sdpsharelook.history

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.github.sdpsharelook.R
import com.github.sdpsharelook.Word
import com.github.sdpsharelook.storage.IRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Use the [HistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

@AndroidEntryPoint
class HistoryFragment : HistoryFragmentLift()

open class HistoryFragmentLift : Fragment() {

    @Inject
    lateinit var wordRepos: IRepository<List<Word>>

    lateinit var wordList: List<Word>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        lifecycleScope.launch {
            wordRepos.flow().collect { words ->
                wordList = words.getOrDefault(emptyList<Word>()) as List<Word>
                wordList.sortedBy { x -> x.savedDate!! }
                //val timeNow = Date.from(Instant.now())
                val rightWord : Date
                for (word in wordList) {
                    if (word.location != null) {
                    }
                }

            }
        }
    }

    // For when feature is fully ready

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
//        inflater.inflate(R.menu.history_menu, menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.lastWeekOption  -> updateViews(0)
//            R.id.lastMonthOption -> updateViews(1)
//            R.id.lastYearOption -> updateViews(2)
//        }
//        return super.onOptionsItemSelected(item)
//    }
//
//    @SuppressLint("SetTextI18n")
//    private fun updateViews(i: Int) {
//        val text : String
//        when(i) {
//            0 -> { text = "Last Week" }
//            1 -> { text = "Last Month"}
//            2 -> { text = "Last Year"}
//            else -> {text = "Last Week"}
//        }
//        requireView().findViewById<TextView>(R.id.recapDescription).text = text
//
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }
}