package com.example.musicplayer.presentation.musicScreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.musicplayer.common.MainActivity
import com.example.musicplayer.presentation.musicScreen.AllSongsAdapter
import com.example.musicplayer.common.extensionFunctions.LoadingDialog
import com.example.musicplayer.common.extensionFunctions.LoadingDialog.showLoadingDialog
import com.example.musicplayer.common.utils.Utils
import com.example.musicplayer.databinding.FragmentMyMusicBinding
import com.example.musicplayer.domain.models.Mp3FilesDataClass
import com.example.musicplayer.viewModel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyMusicFragment : Fragment(), AllSongsAdapter.AllSongsClickListener {
    private lateinit var binding: FragmentMyMusicBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private val allSongsAdapter: AllSongsAdapter by lazy {
        AllSongsAdapter()
    }


    var favSongList: MutableList<Mp3FilesDataClass> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyMusicBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.showLoadingDialog()
        lifecycleScope.launch(Dispatchers.IO) {
            mainViewModel.mp3Files.collect { allSongs ->
                withContext(Dispatchers.Main) {
                    LoadingDialog.hideLoadingDialog()
                    setUpRecyclerView(allSongs)
                }
            }
        }
        lifecycleScope.launch(Dispatchers.IO) {
            mainViewModel.favList.collect { favSongs ->
                favSongList.addAll(favSongs)

            }

        }
    }


    private fun setUpRecyclerView(mp3Files: List<Mp3FilesDataClass>) {
        binding.recyclerMyMusic.adapter = allSongsAdapter
        allSongsAdapter.allSongsClick = this

        // Convert favorite song IDs to a Set for O(1) lookup
        val favIds = favSongList.map { it.id }.toSet()

        // Mark favorites efficiently
        mp3Files.forEach { song ->
            song.isFav = favIds.contains(song.id)
        }

        allSongsAdapter.setAllSongsData(mp3Files)
    }


    override fun onFavClick(favSong: Mp3FilesDataClass, position: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            if (favSong.isFav) {
                mainViewModel.insertFav(favSong)
                withContext(Dispatchers.Main) {
                    allSongsAdapter.notifyItemChanged(position)
                    Toast.makeText(
                        activity, "Song added to favorites successfully", Toast.LENGTH_SHORT
                    ).show()
                }
            } else {

                mainViewModel.removeFav(favSong)
                withContext(Dispatchers.Main) {
                    allSongsAdapter.notifyItemChanged(position)
                    Toast.makeText(
                        activity, "Song removed from favorites successfully", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onItemClick(
        songClick: Mp3FilesDataClass, position: Int
    ) {
        activity?.let {

            Utils.playMedia(it, songClick.path.toUri())
        }



        MainActivity.instance?.showBottomPlayer(
            songItem = songClick, isPlaying = true, isFav = songClick.isFav
        )
    }
}