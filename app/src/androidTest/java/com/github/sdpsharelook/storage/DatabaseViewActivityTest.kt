package com.github.sdpsharelook.storage

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdpsharelook.MainActivity
import org.junit.Assert.*
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DatabaseViewActivityTest {

    @get:Rule
    var testRule = ActivityScenarioRule(DatabaseViewActivity::class.java)

    @Test
    fun testPrintsDatabaseContents() {
    }
}