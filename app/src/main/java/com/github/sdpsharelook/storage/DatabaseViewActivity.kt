package com.github.sdpsharelook.storage

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.sdpsharelook.R
import com.github.sdpsharelook.databinding.ActivityDatabaseViewBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class DatabaseViewActivity : AppCompatActivity() {

    @Inject lateinit var repository: IRepository<Any>
    private lateinit var binding: ActivityDatabaseViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDatabaseViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        binding.toolbarLayout.title = title
        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Not yet implemented", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        findViewById<TextView>(R.id.database_contents).apply {
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
                        findViewById<TextView>(R.id.database_contents).apply {
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
}