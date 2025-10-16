package com.example.musicplayer.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import com.example.musicplayer.R
import com.example.musicplayer.adapter.RecentSongsAdapter
import com.example.musicplayer.data.Mp3FilesDataClass
import com.example.musicplayer.databinding.FragmentHomeBinding
import com.example.musicplayer.interfaces.PlaySongClickListernerInterface
import com.example.musicplayer.ui.activities.MainActivity
import com.example.musicplayer.ui.activities.PlayMusicActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HomeFragment : BaseFragment(), PlaySongClickListernerInterface {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var recentAdapter: RecentSongsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).binding.bottombar.linearMusic.setOnClickListener {
            findNavController().navigate(R.id.action_home2_to_myMusic)
        }
        (activity as MainActivity).binding.bottombar.linearArtist.setOnClickListener {
            findNavController().navigate(R.id.action_home2_to_artist)
        }
        (activity as MainActivity).binding.bottombar.linearHome.setOnClickListener {
            findNavController().navigate(R.id.action_home2_self)
        }
        (activity as MainActivity).binding.bottombar.linearPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_home2_to_playList)
        }
        (activity as MainActivity).binding.bottombar.linearFavourite.setOnClickListener {
            findNavController().navigate(R.id.action_home2_to_favourite)
        }
        CoroutineScope(Dispatchers.Default).launch {
            val filesFetched = (activity as MainActivity).allSongs
            Log.i("test", "${filesFetched.size}")
            setUpRecyclerView(filesFetched)
        }
    }

    private fun setUpRecyclerView(mp3Files: ArrayList<Mp3FilesDataClass>) {
        activity?.let { context ->
            if (context is MainActivity) {
                recentAdapter = RecentSongsAdapter(
                    context, mp3Files,this
                )
            }
            if (::binding.isInitialized) {
                binding.recyclerRecentSongs.apply {
                    setHasFixedSize(true)
                    val handler = Handler(Looper.getMainLooper())
                    handler.post { adapter = recentAdapter }
                }
            }
        }
    }

    override fun PlaySong(Mp3Songs: Mp3FilesDataClass) {
        Log.i("test","uri->${Mp3Songs.path}")
        Intent(context, PlayMusicActivity::class.java).also {
            it.putExtra("image", Mp3Songs.path.toUri())
            startActivity(it)
        }
    }
}