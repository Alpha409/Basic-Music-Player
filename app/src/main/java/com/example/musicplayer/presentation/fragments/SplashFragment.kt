package com.example.musicplayer.presentation.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.lifecycleScope
import com.example.musicplayer.R
import com.example.musicplayer.common.extensionFunctions.NavigationExtensionF.findNavControllerSafely
import com.example.musicplayer.common.extensionFunctions.NavigationExtensionF.navigateAndClearStack
import com.example.musicplayer.databinding.FragmentSplashBinding
import com.example.musicplayer.presentation.activities.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashFragment : Fragment() {

    lateinit var binding: FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSplashBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MainActivity.instance?.hideTopBarAndBottomBar()
        launchHomeScreen()
    }

    /**
     * Sets the activity window to fullscreen mode and navigates to the Home screen
     * after a specified delay.
     *
     * This implementation uses a coroutine within the Fragment's lifecycle scope
     * instead of a Handler to ensure that the delayed navigation is lifecycle-aware
     * and automatically canceled if the Fragment's view is destroyed.
     */
    private fun launchHomeScreen() {
        // Enable fullscreen mode
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // Delay navigation safely using coroutine
        viewLifecycleOwner.lifecycleScope.launch {
            delay(3000L)

            Log.d("splashClick", "launchHomeScreen: ")
            findNavControllerSafely()?.navigateAndClearStack(R.id.homeFragment)
        }
    }
}