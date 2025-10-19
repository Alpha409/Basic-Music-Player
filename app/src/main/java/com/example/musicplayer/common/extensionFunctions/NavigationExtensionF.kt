package com.example.musicplayer.common.extensionFunctions

import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.musicplayer.R

object NavigationExtensionF {
    fun NavController.navigateSafely(@IdRes resId: Int, args: Bundle? = null) {
        val action = currentDestination?.getAction(resId) ?: graph.getAction(resId)
        if (action != null && currentDestination?.id != action.destinationId) {
            navigate(resId, args)
        }
    }
    fun NavController.navigateSafely(directions: NavDirections) {
        val action = currentDestination?.getAction(directions.actionId)
        if (action != null) navigate(directions)
    }


    fun NavController.safePopBackStack() {
        if (popBackStack().not()) {
            // optional: handle when no back stack is available
        }
    }


    fun NavController.navigateAndClearStack(@IdRes resId: Int, args: Bundle? = null) {
        navigate(resId, args, NavOptions.Builder()
            .setPopUpTo(graph.startDestinationId, true)
            .build())
    }

    fun NavController.navigateWithAnim(
        @IdRes resId: Int,
        args: Bundle? = null,
        enterAnim: Int = R.anim.slide_in_right,
        exitAnim: Int = R.anim.slide_out_left,
        popEnterAnim: Int = R.anim.slide_in_left,
        popExitAnim: Int = R.anim.slide_out_right
    ) {
        val options = NavOptions.Builder()
            .setEnterAnim(enterAnim)
            .setExitAnim(exitAnim)
            .setPopEnterAnim(popEnterAnim)
            .setPopExitAnim(popExitAnim)
            .build()
        navigate(resId, args, options)
    }
    fun NavController.popBackTo(@IdRes destinationId: Int, inclusive: Boolean = false) {
        popBackStack(destinationId, inclusive)
    }
    fun NavController.isAtDestination(@IdRes destinationId: Int): Boolean =
        currentDestination?.id == destinationId
    fun NavController.navigateIfNotAt(@IdRes resId: Int, args: Bundle? = null) {
        if (currentDestination?.id != resId) {
            navigate(resId, args)
        }
    }
    /**
     * Safely get NavController from a Fragment.
     * Returns null if fragment is not attached or NavController not found.
     */
    fun Fragment.findNavControllerSafely(): NavController? = runCatching {
        findNavController()
    }.getOrNull()

    /**
     * Safely get NavController from a View.
     * Returns null if NavController cannot be found.
     */
    fun View.findNavControllerSafely(): NavController? = runCatching {
        Navigation.findNavController(this)
    }.getOrNull()

    /**
     * Safely get NavController using Activity root view.
     * Returns null if no NavController is attached.
     */
    fun androidx.appcompat.app.AppCompatActivity.findNavControllerSafely(
        hostFragmentId: Int
    ): NavController? = runCatching {
        findNavController(hostFragmentId)
    }.getOrNull()

}