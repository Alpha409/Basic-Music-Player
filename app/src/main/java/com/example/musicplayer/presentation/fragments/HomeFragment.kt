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
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.musicplayer.R
import com.example.musicplayer.adapter.RecentSongsAdapter
import com.example.musicplayer.common.extensionFunctions.NavigationExtensionF.findNavControllerSafely
import com.example.musicplayer.common.extensionFunctions.ViewsExtensionF.setOnOneClickListener
import com.example.musicplayer.data.Mp3FilesDataClass
import com.example.musicplayer.databinding.FragmentHomeBinding
import com.example.musicplayer.interfaces.PlaySongClickListernerInterface
import com.example.musicplayer.presentation.activities.MainActivity
import com.example.musicplayer.ui.activities.PlayMusicActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch


class HomeFragment : Fragment(), PlaySongClickListernerInterface {
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

        MainActivity.instance?.showTopBarAndBottomBar()
        initClickListener()
        lifecycleScope.launch(IO) {
             (activity as MainActivity).mp3Files.collect {filesFetched ->

                Log.i("test", "${filesFetched.size}")
                setUpRecyclerView(filesFetched)
            }
        }
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

    private fun setUpRecyclerView(mp3Files: List<Mp3FilesDataClass>) {
        activity?.let { context ->
            if (context is MainActivity) {
                recentAdapter = RecentSongsAdapter(
                    context, mp3Files, this
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
        Log.i("test", "uri->${Mp3Songs.path}")
        Intent(context, PlayMusicActivity::class.java).also {
            it.putExtra("image", Mp3Songs.path.toUri())
            startActivity(it)
        }
    }
}