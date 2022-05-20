package com.github.sdpsharelook.storage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.github.sdpsharelook.R
import com.github.sdpsharelook.databinding.FragmentDatabaseViewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class DatabaseViewFragment : DatabaseViewFragmentLift()

open class DatabaseViewFragmentLift : Fragment() {

    /**
     * This property is only valid between onCreateView and onDestroyView.
     */
    private val binding get() = _binding!!
    private var _binding: FragmentDatabaseViewBinding? = null

    @Inject
    lateinit var repository: IRepository<Any>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.databaseContents.apply {
            text = context.getString(R.string.default_database_content)
        }

        lifecycleScope.launch {
            collectDBFlow()
        }
    }

    private suspend fun collectDBFlow() = withContext(Dispatchers.IO) {
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
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}