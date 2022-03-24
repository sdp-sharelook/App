package com.github.sdpsharelook.storage

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.sdpsharelook.R
import com.github.sdpsharelook.databinding.ActivityDatabaseViewBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.*

class DatabaseViewActivity : AppCompatActivity() {

    private val firebaseRTTRepository = FirebaseRTTRepository(FirebaseDatabase.getInstance("https://billinguee-default-rtdb.europe-west1.firebasedatabase.app/"), "test")
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

        findViewById<TextView>(R.id.database_contents).apply { text = context.getString(R.string.default_database_content) }
    }

    fun fetchValues(@Suppress("UNUSED_PARAMETER")view: View) {
        GlobalScope.launch(Dispatchers.IO) {
            firebaseRTTRepository.fetchValues().collect {
                when {
                    it.isSuccess -> {
                        val message = it.getOrNull().toString()
                        findViewById<TextView>(R.id.database_contents).apply { text = message }
                    }
                    it.isFailure -> {
                        it.exceptionOrNull()?.printStackTrace()
                    }
                }
            }
        }
    }
}