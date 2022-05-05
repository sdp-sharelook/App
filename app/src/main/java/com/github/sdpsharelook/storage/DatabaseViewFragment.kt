package com.github.sdpsharelook.storage

import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.sdpsharelook.R
import com.github.sdpsharelook.Word
import com.github.sdpsharelook.databinding.FragmentDatabaseViewBinding
import com.github.sdpsharelook.dbWord
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.onEach

class DatabaseViewFragment : Fragment() {

    private val repository: RTDBWordListRepository = RTDBWordListRepository()

    /**
     * This property is only valid between onCreateView and onDestroyView.
     */
    private val binding get() = _binding!!
    private var _binding: FragmentDatabaseViewBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.databaseContents.apply {
            binding.databaseContents.
            text = context.getString(R.string.default_database_content)
        }


    }

    private suspend fun collectDBFlow() {

        repository.flow().collect {
            when {
                it.isSuccess -> {
                    val message = it.getOrNull().toString()
                    withContext(Dispatchers.Main) {
                        binding.databaseContents.apply {
                            text = message
                        }
                    }
                }
                it.isFailure -> {
                    it.exceptionOrNull()?.printStackTrace()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDatabaseViewBinding.inflate(layoutInflater)
        val frag = this
        Dispatchers.IO.dispatch(Dispatchers.IO){
            runBlocking {
                frag.collectDBFlow()
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}