package com.example.musicplayer.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.musicplayer.R
import com.example.musicplayer.common.extensionFunctions.NavigationExtensionF.findNavControllerSafely
import com.example.musicplayer.common.extensionFunctions.ViewsExtensionF.setOnOneClickListener
import com.example.musicplayer.databinding.FragmentPlayListBinding
import com.example.musicplayer.presentation.activities.MainActivity

class PlayListFragment : Fragment() {
    private lateinit var binding: FragmentPlayListBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


}