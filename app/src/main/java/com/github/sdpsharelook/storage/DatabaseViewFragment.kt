package com.github.sdpsharelook.storage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.sdpsharelook.R
import com.github.sdpsharelook.databinding.FragmentDatabaseViewBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/** A simple [Fragment] subclass. */
class DatabaseViewFragment : Fragment() {

    private val repository: IRepository<Any> = RTDBAnyRepository()
    private lateinit var binding: FragmentDatabaseViewBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Not yet implemented", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        view.findViewById<TextView>(R.id.database_contents).apply {
            text = context.getString(R.string.default_database_content)
        }

        CoroutineScope(Dispatchers.IO).launch {
            collectDBFlow(view)
        }
    }

    private suspend fun collectDBFlow(view: View) {
        repository.flow().collect {
            when {
                it.isSuccess -> {
                    val message = it.getOrNull().toString()
                    withContext(Dispatchers.Main) {
                        view.findViewById<TextView>(R.id.database_contents).apply {
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
    ): View? {
        binding = FragmentDatabaseViewBinding.inflate(layoutInflater)
        return binding.root
    }
}