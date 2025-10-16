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
import com.example.musicplayer.adapter.AllArtistAdapter
import com.example.musicplayer.data.Mp3FilesDataClass
import com.example.musicplayer.databinding.FragmentArtistBinding
import com.example.musicplayer.interfaces.BottomMenuClickInterface
import com.example.musicplayer.ui.activities.MainActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArtistFragment : BaseFragment(), BottomMenuClickInterface {
    private lateinit var binding: FragmentArtistBinding
    private lateinit var allArtistAdapter: AllArtistAdapter
    private var allArtists: ArrayList<Mp3FilesDataClass> = arrayListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentArtistBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).binding.bottombar.linearMusic.setOnClickListener {
            findNavController().navigate(R.id.action_artist_to_myMusic)
        }
        (activity as MainActivity).binding.bottombar.linearArtist.setOnClickListener {
            findNavController().navigate(R.id.action_artist_self)
        }
        (activity as MainActivity).binding.bottombar.linearHome.setOnClickListener {
            findNavController().navigate(R.id.action_artist_to_home2)
        }
        (activity as MainActivity).binding.bottombar.linearPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_artist_to_playList)
        }
        (activity as MainActivity).binding.bottombar.linearFavourite.setOnClickListener {
            findNavController().navigate(R.id.action_artist_to_favourite)
        }

        CoroutineScope(Dispatchers.IO).launch {

            allArtists = (activity as MainActivity).allSongs
            Log.i("test", "My Music Frag->${allArtists.size} ")
            setUpRecyclerView(allArtists)
        }
    }

    private fun setUpRecyclerView(mp3Files: ArrayList<Mp3FilesDataClass>) {
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