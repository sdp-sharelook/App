package com.github.sdpsharelook.storage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.sdpsharelook.R
import com.github.sdpsharelook.databinding.FragmentDatabaseViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DatabaseViewFragment : Fragment() {

    private val repository: IRepository<Any> = RTDBAnyRepository()
    private lateinit var binding: FragmentDatabaseViewBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.databaseContents.apply {
            text = context.getString(R.string.default_database_content)
        }

        CoroutineScope(Dispatchers.IO).launch {
            collectDBFlow()
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
        binding = FragmentDatabaseViewBinding.inflate(layoutInflater)
        return binding.root
    }
}