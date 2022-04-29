package com.github.sdpsharelook.revision

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.github.sdpsharelook.R
import com.github.sdpsharelook.storage.RTDBAnyRepository
import kotlinx.coroutines.flow.collect
import java.util.concurrent.Flow

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ReviewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReviewFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var rButtons: Array<Button>
    var sessionWords: List<revisionWord>? = null
    var currentWord: revisionWord? = null
    val TEST_WORDS = listOf<revisionWord>(
        revisionWord("id0"),
        revisionWord("id1", 12312L, 6.0, 3), revisionWord("id2")
    )
    var revFlow: kotlinx.coroutines.flow.Flow<Result<Any?>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        sessionWords= TEST_WORDS
        val revRep = RTDBAnyRepository()
        revFlow =revRep.flow("revision")




    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_review, container, false)

        rButtons = arrayOf(
            view.findViewById(R.id.rate_1),
            view.findViewById(R.id.rate_2),
            view.findViewById(R.id.rate_3),
            view.findViewById(R.id.rate_4),
            view.findViewById(R.id.rate_5),
        )
        rButtons.forEach { button ->
            button.setOnClickListener { rate(button.text.toString()) }
        }

        currentWord=sessionWords!![0]
        updateWord(view, currentWord!!)
        return view
    }

    fun updateWord(view: View, newWord: revisionWord) {
        val textBox = view.findViewById<TextView>(R.id.question_text)
        textBox.text = newWord.wordId
    }

    fun rate(text: String) {
        val rating = text.toInt()
        currentWord?.let {

            Log.d("","RATE RATE RATE")
            Log.d("",sessionWords!!.size.toString())
            SRAlgo.calcNextReviewTime(it, rating)

            currentWord!!.lastReview = System.currentTimeMillis()
            it.saveToStorage(requireContext())
            var index = sessionWords!!.indexOf(currentWord!!)
            if(index<sessionWords!!.size-1){
                index+=1
            }else{
                index = 0
            }
            currentWord = sessionWords!![index]
            updateWord(requireView(), currentWord!!)
        }

    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ReviewFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReviewFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}