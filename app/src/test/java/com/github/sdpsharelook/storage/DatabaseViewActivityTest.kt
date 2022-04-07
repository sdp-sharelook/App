package com.github.sdpsharelook.storage

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdpsharelook.R
import com.github.sdpsharelook.di.StorageBindsModule
import com.github.sdpsharelook.di.StorageModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Singleton

@UninstallModules(StorageModule::class,StorageBindsModule::class)
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class DatabaseViewActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun inject() {
        hiltRule.inject()
    }

    @Test
    fun testPrintsDatabaseContents() {
        launchActivity<DatabaseViewActivity>()
        onView(withId(R.id.fab)).perform(click())
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(isDisplayed()))
    }

    @Module
    @InstallIn(SingletonComponent::class)
    object FakeRealtimeFirebaseModule {
        @Provides
        @Singleton
        fun wordListRepo(): IRepository<Any> = object : IRepository<Any> {
            override fun flow(name: String): Flow<Result<Any?>> =
                flowOf(Result.success("Hello World!"))

            override suspend fun insert(name: String, entity: Any) = Unit
            override suspend fun read(name: String): Any? = null
            override suspend fun update(name: String, entity: Any) = Unit
            override suspend fun delete(name: String) = Unit
        }
    }
}