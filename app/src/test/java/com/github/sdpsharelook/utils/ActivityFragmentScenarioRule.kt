package com.github.sdpsharelook.utils

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import org.junit.rules.ExternalResource
import kotlin.reflect.KClass

/**
 * A test rule that creates a Fragment for each test
 * Based on [this repo][https://github.com/dedis/popstellar/tree/master/fe2-android/app/src/test/java/com/github/dedis/popstellar/testutils/fragment] and adapted to kotlin
 *
 * @param [F] the fragment type
 */
@Suppress("unused")
open class ActivityFragmentScenarioRule<A : AppCompatActivity, F : Fragment>(
    private val scenarioSupplier: () -> FragmentScenario<A, F>
) : ExternalResource() {
    private var _scenario: FragmentScenario<A, F>? = null
    val scenario
        get() = _scenario!!

    @Throws(Throwable::class)
    override fun before() {
        _scenario = scenarioSupplier()
    }

    override fun after() {
        _scenario?.close()
        _scenario = null
    }


    companion object {
        fun <A : AppCompatActivity, F : Fragment> launchIn(
            activityClass: KClass<A>,
            @IdRes contentId: Int,
            fragmentClass: KClass<F>,
            supplier: () -> F
        ): ActivityFragmentScenarioRule<A, F> = ActivityFragmentScenarioRule {
            FragmentScenario.launchIn(
                activityClass,
                contentId,
                fragmentClass,
                supplier
            )
        }

        fun <A : AppCompatActivity, F : Fragment> launchIn(
            activityClass: KClass<A>,
            activityArgs: Bundle?,
            @IdRes contentId: Int,
            fragmentClass: KClass<F>,
            supplier: () -> F
        ): ActivityFragmentScenarioRule<A, F> = ActivityFragmentScenarioRule {
            FragmentScenario.launchIn(
                activityClass, activityArgs, contentId, fragmentClass, supplier
            )
        }
    }
}