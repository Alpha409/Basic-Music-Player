package com.example.musicplayer.presentation.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.musicplayer.R
import com.example.musicplayer.adapter.AllArtistAdapter
import com.example.musicplayer.common.extensionFunctions.NavigationExtensionF.findNavControllerSafely
import com.example.musicplayer.common.extensionFunctions.ViewsExtensionF.setOnOneClickListener
import com.example.musicplayer.domain.models.Mp3FilesDataClass
import com.example.musicplayer.databinding.FragmentArtistBinding
import com.example.musicplayer.interfaces.BottomMenuClickInterface
import com.example.musicplayer.presentation.activities.MainActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class ArtistFragment : Fragment(), BottomMenuClickInterface {
    private lateinit var binding: FragmentArtistBinding
    private lateinit var allArtistAdapter: AllArtistAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentArtistBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        initClickListener()

        lifecycleScope.launch(IO) {

             (activity as MainActivity).mp3Files.collect { allArtists ->

                 Log.i("test", "My Music Frag->${allArtists.size} ")
                 setUpRecyclerView(allArtists)
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
            findNavControllerSafely()?.navigate(R.id.favourite)
        }
    }

    private fun setUpRecyclerView(mp3Files: List<Mp3FilesDataClass>) {
        activity?.let { context ->
            if (context is MainActivity) {
                allArtistAdapter = AllArtistAdapter(
                    context, mp3Files, this
                )
            }
            if (::binding.isInitialized) {
                binding.recyclerMyMusic.apply {
                    setHasFixedSize(true)
                    val handler = Handler(Looper.getMainLooper())
                    handler.post { adapter = allArtistAdapter }
                }
            }
        }
    }

    override fun showBottomMenu(Mp3Songs: Mp3FilesDataClass) {
        showBottomMenuDialog()
    }

    private fun showBottomMenuDialog() {
        val bottomMenuDialog = BottomSheetDialog(requireContext())
        bottomMenuDialog.setContentView(R.layout.custom_bottom_menu)
        bottomMenuDialog.show()
    }
}