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
import com.example.musicplayer.databinding.FragmentFavouriteBinding
import com.example.musicplayer.presentation.activities.MainActivity


class FavouriteFragment : Fragment() {
    private lateinit var binding: FragmentFavouriteBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouriteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickListener()
    }

    fun initClickListener() {
        (activity as MainActivity).binding.linearMusic.setOnOneClickListener {
            findNavControllerSafely()?.navigate(R.id.myMusicFragment)
        }
        (activity as MainActivity).binding.linearArtist.setOnOneClickListener {
            findNavControllerSafely()?.navigate(R.id.artistFragment)
        }
        (activity as MainActivity).binding.linearHome.setOnOneClickListener {
            findNavControllerSafely()?.navigate(R.id.homeFragment)
        }
        (activity as MainActivity).binding.linearPlaylist.setOnOneClickListener {
            findNavControllerSafely()?.navigate(R.id.playListFragment)
        }
        (activity as MainActivity).binding.linearFavourite.setOnOneClickListener {
            findNavControllerSafely()?.navigate(R.id.favouriteFragment)
        }
    }
}