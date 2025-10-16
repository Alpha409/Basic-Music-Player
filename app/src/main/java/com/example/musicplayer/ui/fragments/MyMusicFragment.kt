package com.example.musicplayer.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.musicplayer.R
import com.example.musicplayer.adapter.AllSongsAdapter
import com.example.musicplayer.data.Mp3FilesDataClass
import com.example.musicplayer.databinding.FragmentMyMusicBinding
import com.example.musicplayer.ui.activities.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyMusicFragment : BaseFragment() {
    private lateinit var binding: FragmentMyMusicBinding

    private lateinit var allSongsAdapter: AllSongsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyMusicBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).binding.bottombar.linearMusic.setOnClickListener {
            findNavController().navigate(R.id.action_myMusic_self)
        }
        (activity as MainActivity).binding.bottombar.linearArtist.setOnClickListener {
            findNavController().navigate(R.id.action_myMusic_to_artist)
        }
        (activity as MainActivity).binding.bottombar.linearHome.setOnClickListener {
            findNavController().navigate(R.id.action_myMusic_to_home2)
        }
        (activity as MainActivity).binding.bottombar.linearPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_myMusic_to_playList)
        }
        (activity as MainActivity).binding.bottombar.linearFavourite.setOnClickListener {
            findNavController().navigate(R.id.action_myMusic_to_favourite)
        }
        CoroutineScope(Dispatchers.IO).launch {
            val allSongs = (activity as MainActivity).allSongs
            Log.i("test", "My Music Frag->${allSongs.size} ")
            setUpRecyclerView(allSongs)
        }
    }

    private fun setUpRecyclerView(mp3Files: ArrayList<Mp3FilesDataClass>) {
        activity?.let { context ->
            if (context is MainActivity) {
                allSongsAdapter = AllSongsAdapter(
                    context, mp3Files
                )
            }
            if (::binding.isInitialized) {
                binding.recyclerMyMusic.apply {
                    setHasFixedSize(true)
                    val handler = Handler(Looper.getMainLooper())
                    handler.post { adapter = allSongsAdapter }

                }
            }
        }
    }
}