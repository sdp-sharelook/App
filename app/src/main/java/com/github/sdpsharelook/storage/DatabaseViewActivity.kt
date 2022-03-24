package com.github.sdpsharelook.storage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.sdpsharelook.R
import com.github.sdpsharelook.databinding.ActivityDatabaseViewBinding
import com.google.android.material.snackbar.Snackbar

class DatabaseViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDatabaseViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDatabaseViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        binding.toolbarLayout.title = title
        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }
}