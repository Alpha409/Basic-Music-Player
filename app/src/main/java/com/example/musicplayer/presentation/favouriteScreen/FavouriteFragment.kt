package com.example.musicplayer.presentation.favouriteScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.musicplayer.common.MainActivity
import com.example.musicplayer.common.extensionFunctions.ViewsExtensionF.hide
import com.example.musicplayer.common.extensionFunctions.ViewsExtensionF.show
import com.example.musicplayer.common.utils.Utils
import com.example.musicplayer.databinding.FragmentFavouriteBinding
import com.example.musicplayer.domain.models.Mp3FilesDataClass
import com.example.musicplayer.presentation.musicScreen.AllSongsAdapter
import com.example.musicplayer.viewModel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.getValue

class FavouriteFragment : Fragment(), AllSongsAdapter.AllSongsClickListener {
    private lateinit var binding: FragmentFavouriteBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private val allSongsAdapter: AllSongsAdapter by lazy {
        AllSongsAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouriteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch(Dispatchers.IO) {
            mainViewModel.favList.collect { favSongs ->
                if (favSongs.isEmpty()) {
                    binding.llNoItems.show()
                } else {
                    binding.llNoItems.hide()
                    setUpRecyclerView(favSongs)
                }

            }

        }
    }


    private fun setUpRecyclerView(mp3Files: List<Mp3FilesDataClass>) {
        binding.rvFavSongs.adapter = allSongsAdapter
        allSongsAdapter.allSongsClick = this
        allSongsAdapter.setAllSongsData(mp3Files)
    }

    override fun onFavClick(
        favSong: Mp3FilesDataClass, position: Int
    ) {
        lifecycleScope.launch(Dispatchers.IO) {
            mainViewModel.removeFav(favSong)
            withContext(Dispatchers.Main) {
                allSongsAdapter.notifyItemChanged(position)
                Toast.makeText(
                    activity, "Song Removed to favorites successfully", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onItemClick(
        songClick: Mp3FilesDataClass, position: Int
    ) {
        activity?.let {

            Utils.initPlayer(it)
            Utils.playMedia(it, songClick.path.toUri())
        }
        Utils.currentSong =songClick


        MainActivity.instance?.showBottomPlayer(
            songItem = songClick, isPlaying = true, isFav = songClick.isFav
        )

    }


}