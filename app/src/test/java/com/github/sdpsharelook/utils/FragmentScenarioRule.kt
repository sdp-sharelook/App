package com.github.sdpsharelook.utils

import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlin.reflect.KClass

/**
 * An [ActivityFragmentScenarioRule] where the activity doesn't matter
 *
 * @param [F] Fragment type
 */
@Suppress("unused")
class FragmentScenarioRule<F : Fragment>(
    fragmentScenarioSupplier: () -> FragmentScenario<HiltTestActivity, F>
) : ActivityFragmentScenarioRule<HiltTestActivity, F>(fragmentScenarioSupplier) {
    companion object {
        fun <F : Fragment> launch(fragmentClass: KClass<F>): FragmentScenarioRule<F> =
            FragmentScenarioRule { FragmentScenario.launch(fragmentClass) }

        fun <F : Fragment> launch(
            fragmentClass: KClass<F>,
            supplier: () -> F
        ): FragmentScenarioRule<F> =
            FragmentScenarioRule { FragmentScenario.launch(fragmentClass, supplier) }

        fun <F : Fragment> launch(
            fragmentClass: KClass<F>,
            supplier: (() -> F)?,
            fragmentArgs: Bundle
        ): FragmentScenarioRule<F> =
            FragmentScenarioRule { FragmentScenario.launch(fragmentClass, supplier,fragmentArgs) }
    }
}