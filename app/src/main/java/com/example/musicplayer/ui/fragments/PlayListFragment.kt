package com.example.musicplayer.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.musicplayer.R
import com.example.musicplayer.databinding.FragmentPlayListBinding
import com.example.musicplayer.ui.activities.MainActivity

class PlayListFragment : BaseFragment() {
    private lateinit var binding: FragmentPlayListBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).binding.bottombar.linearMusic.setOnClickListener {
            findNavController().navigate(R.id.action_playList_to_myMusic)
        }
        (activity as MainActivity).binding.bottombar.linearArtist.setOnClickListener {
            findNavController().navigate(R.id.action_playList_to_artist)
        }
        (activity as MainActivity).binding.bottombar.linearHome.setOnClickListener {
            findNavController().navigate(R.id.action_playList_to_home2)
        }
        (activity as MainActivity).binding.bottombar.linearPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_playList_self)
        }
        (activity as MainActivity).binding.bottombar.linearFavourite.setOnClickListener {
            findNavController().navigate(R.id.action_playList_to_favourite)
        }
    }
}