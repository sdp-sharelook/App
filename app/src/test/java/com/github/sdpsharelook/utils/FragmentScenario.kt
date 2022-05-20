package com.github.sdpsharelook.utils

import android.R
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.ActivityAction
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.core.internal.deps.guava.base.Preconditions
import kotlin.reflect.KClass
import kotlin.reflect.cast


/**
 * This class allows easy testing of fragments.
 * It is based on [this repo][https://github.com/dedis/popstellar/tree/master/fe2-android/app/src/test/java/com/github/dedis/popstellar/testutils/fragment] adapted to kotlin,
 * With the help of some previous work on [launchFragmentInHiltContainer],
 * and [androidx.fragment.app.testing.FragmentScenario].
 *
 * It allows Hilt injection or custom activity holding the fragment
 *
 * @param [A] Activity class
 * @param [F] Fragment class
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
class FragmentScenario<A : AppCompatActivity, F : Fragment> private constructor(
    private val activityScenario: ActivityScenario<A>,
    private val fragmentClass: KClass<F>
) {
    /**
     * Recreate the scenario
     *
     * @return the scenario
     */
    fun recreate(): FragmentScenario<A, F> {
        activityScenario.recreate()
        return this
    }

    /**
     * Execute on action on the activity the scenario is running on
     *
     * @param action to execute on the activity
     * @return the scenario
     */
    fun onActivity(action: ActivityAction<A>): FragmentScenario<A, F> {
        activityScenario.onActivity(action)
        return this
    }

    /**
     * Execute an action on the fragment the scenario is running on
     *
     * @param action to execute on the fragment
     * @return the scenario
     */
    fun onFragment(action: F.() -> Unit): FragmentScenario<A, F> {
        activityScenario.onActivity { activity: A ->
            val fragment = requireNotNull(
                activity.supportFragmentManager.findFragmentByTag(TAG)
            ) {
                "The fragment has been removed from the FragmentManager already."
            }
            check(fragmentClass.isInstance(fragment))
            requireNotNull(fragmentClass.cast(fragment)).action()
        }
        return this
    }

    /**
     * Advance the fragment to a new state
     *
     * @param newState to move onto
     * @return the scenario
     */
    fun moveToState(newState: Lifecycle.State): FragmentScenario<A, F> {
        if (newState == Lifecycle.State.DESTROYED) {
            activityScenario.onActivity { activity: A ->
                val fragment = activity.supportFragmentManager
                    .findFragmentByTag(TAG)
                if (fragment != null) {
                    activity.supportFragmentManager.beginTransaction().remove(fragment)
                        .commitNow()
                }
            }
        } else {
            activityScenario.onActivity { activity: A ->
                val fragment = activity.supportFragmentManager
                    .findFragmentByTag(TAG)
                    ?: throw IllegalStateException("fragment is already destroyed")
                activity
                    .supportFragmentManager
                    .beginTransaction()
                    .setMaxLifecycle(fragment, newState)
                    .commitNow()
            }
        }
        return this
    }

    /** Close the scenario. This should be called at the end of any test.  */
    fun close() {
        activityScenario.close()
    }

    private class SimpleFragmentFactory(
        private val supplier: () -> Fragment
    ) : FragmentFactory() {

        override fun instantiate(
            classLoader: ClassLoader,
            className: String
        ): Fragment {
            return supplier()
        }
    }

    companion object {
        /**
         * Launch a new FragmentScenario
         *
         * @param fragmentClass fragment to launch the scenario on
         * @param [F] Fragment type
         * @return the launched scenario
         */
        fun <F : Fragment> launch(
            fragmentClass: KClass<F>
        ): FragmentScenario<HiltTestActivity, F> = launchIn(
            HiltTestActivity::class,
            Bundle.EMPTY,
            R.id.content,
            fragmentClass,
            null,
            Bundle.EMPTY
        )

        /**
         * Launch a new FragmentScenario
         *
         * @param fragmentClass fragment to launch the scenario on
         * @param fragmentSupplier supplier that creates the fragment object
         * @param [F] Fragment type
         * @return the launched scenario
         */
        fun <F : Fragment> launch(
            fragmentClass: KClass<F>, fragmentSupplier: (() -> F)? = null
        ): FragmentScenario<HiltTestActivity, F> = launchIn(
            HiltTestActivity::class,
            Bundle.EMPTY,
            R.id.content,
            fragmentClass,
            factory<Fragment>(fragmentSupplier),
            Bundle.EMPTY
        )

        /**
         * Launch a new FragmentScenario
         *
         * @param fragmentClass fragment to launch the scenario on
         * @param fragmentSupplier supplier that creates the fragment object
         * @param [F] Fragment type
         * @return the launched scenario
         */
        fun <F : Fragment> launch(
            fragmentClass: KClass<F>, fragmentSupplier: (() -> F)? = null, fragmentArgs: Bundle?
        ): FragmentScenario<HiltTestActivity, F> = launchIn(
            HiltTestActivity::class,
            Bundle.EMPTY,
            R.id.content,
            fragmentClass,
            factory<Fragment>(fragmentSupplier),
            fragmentArgs
        )

        /**
         * Launch a new FragmentScenario
         *
         * @param activityClass activity to launch the scenario on
         * @param contentId id of the placeholder where the fragment will be put
         * @param fragmentClass fragment to launch the scenario on
         * @param [A] Activity type
         * @param [F] Fragment type
         * @return the launched scenario
         */
        fun <A : AppCompatActivity, F : Fragment> launchIn(
            activityClass: KClass<A>,
            @IdRes contentId: Int,
            fragmentClass: KClass<F>
        ): FragmentScenario<A, F> = launchIn(
            activityClass,
            Bundle.EMPTY,
            contentId,
            fragmentClass,
            null,
            Bundle.EMPTY
        )

        /**
         * Launch a new FragmentScenario
         *
         * @param activityClass activity to launch the scenario on
         * @param contentId id of the placeholder where the fragment will be put
         * @param fragmentClass fragment to launch the scenario on
         * @param fragmentSupplier supplier that creates the fragment object
         * @param [A] Activity type
         * @param [F] Fragment type
         * @return the launched scenario
         */
        fun <A : AppCompatActivity, F : Fragment> launchIn(
            activityClass: KClass<A>,
            @IdRes contentId: Int,
            fragmentClass: KClass<F>,
            fragmentSupplier: () -> F
        ): FragmentScenario<A, F> = launchIn(
            activityClass,
            Bundle.EMPTY,
            contentId,
            fragmentClass,
            factory<Fragment>(fragmentSupplier),
            Bundle.EMPTY
        )

        /**
         * Launch a new FragmentScenario
         *
         * @param activityClass activity to launch the scenario on
         * @param activityArgs arguments of the activity
         * @param contentId id of the placeholder where the fragment will be put
         * @param fragmentClass fragment to launch the scenario on
         * @param fragmentSupplier supplier that creates the fragment object
         * @param [A] Activity type
         * @param [F] Fragment type
         * @return the launched scenario
         */
        fun <A : AppCompatActivity, F : Fragment> launchIn(
            activityClass: KClass<A>,
            activityArgs: Bundle?,
            @IdRes contentId: Int,
            fragmentClass: KClass<F>,
            fragmentSupplier: () -> F
        ): FragmentScenario<A, F> = launchIn(
            activityClass,
            activityArgs,
            contentId,
            fragmentClass,
            factory<Fragment>(fragmentSupplier),
            Bundle.EMPTY
        )

        private fun <F : Fragment> factory(fragmentSupplier: (() -> F)?): FragmentFactory? =
            fragmentSupplier?.let { SimpleFragmentFactory(it) }

        const val TAG = "FRAGMENT"

        /**
         * Launch a new FragmentScenario with following arguments :
         *
         * @param activityClass activity to launch the fragment on
         * @param activityArgs arguments of the activity
         * @param contentId id of the placeholder where the fragment will be put
         * @param fragmentClass fragment to launch
         * @param factory that produces the fragment object. If null, the android default will be used.
         * @param fragmentArgs arguments of the fragment
         * @param [A] Activity type
         * @param [F] Fragment type
         * @return the launched scenario
         */
        fun <A : AppCompatActivity, F : Fragment> launchIn(
            activityClass: KClass<A>,
            activityArgs: Bundle?,
            @IdRes contentId: Int,
            fragmentClass: KClass<F>,
            factory: FragmentFactory?,
            fragmentArgs: Bundle?
        ): FragmentScenario<A, F> {
            val mainActivityIntent = Intent.makeMainActivity(
                ComponentName(
                    ApplicationProvider.getApplicationContext(),
                    activityClass.java
                )
            )
            mainActivityIntent.putExtras(activityArgs!!)
            val scenario = ActivityScenario.launch<A>(mainActivityIntent)
            val fragmentScenario = FragmentScenario(scenario, fragmentClass)
            scenario.onActivity { activity: A ->
                factory?.also {
                    activity.supportFragmentManager.fragmentFactory = it
                }
                val fragment = activity
                    .supportFragmentManager
                    .fragmentFactory
                    .instantiate(
                        Preconditions.checkNotNull(fragmentClass.java.classLoader),
                        fragmentClass.java.name
                    )
                fragment.arguments = fragmentArgs
                activity
                    .supportFragmentManager
                    .beginTransaction()
                    .add(contentId, fragment, TAG)
                    .setMaxLifecycle(fragment, Lifecycle.State.RESUMED)
                    .commitNow()
            }
            return fragmentScenario
        }
    }
}