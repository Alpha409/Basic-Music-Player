package com.example.musicplayer.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.musicplayer.R
import com.example.musicplayer.adapter.AllSongsAdapter
import com.example.musicplayer.common.extensionFunctions.NavigationExtensionF.findNavControllerSafely
import com.example.musicplayer.common.extensionFunctions.ViewsExtensionF.setOnOneClickListener
import com.example.musicplayer.domain.models.Mp3FilesDataClass
import com.example.musicplayer.databinding.FragmentMyMusicBinding
import com.example.musicplayer.presentation.activities.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyMusicFragment : Fragment() {
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
        initClickListener()
        lifecycleScope.launch(IO) {
            (activity as MainActivity).mp3Files.collect {allSongs ->

                Log.i("checkSongs", "My Music Frag->${allSongs.size} ")
                withContext(Main){

                    setUpRecyclerView(allSongs)
                }
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