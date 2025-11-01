package com.example.musicplayer.presentation.homeScreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.musicplayer.common.MainActivity
import com.example.musicplayer.common.extensionFunctions.LoadingDialog
import com.example.musicplayer.common.extensionFunctions.LoadingDialog.showLoadingDialog
import com.example.musicplayer.common.utils.Utils
import com.example.musicplayer.databinding.FragmentHomeBinding
import com.example.musicplayer.domain.models.Mp3FilesDataClass
import com.example.musicplayer.viewModel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment(), RecentSongsAdapter.PlaySongClickListenerInterface {
    private lateinit var binding: FragmentHomeBinding
    private val recentAdapter: RecentSongsAdapter by lazy {
        RecentSongsAdapter()
    }
    private val mainViewModel: MainViewModel by activityViewModels()
    private var mp3Files: MutableList<Mp3FilesDataClass> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        MainActivity.Companion.instance?.hideBottomPlayer()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MainActivity.Companion.instance?.showTopBarAndBottomBar()

        activity?.showLoadingDialog()
        lifecycleScope.launch(Dispatchers.IO) {
            mainViewModel.mp3Files.collect { filesFetched ->
                LoadingDialog.hideLoadingDialog()
                Log.i("test", "${filesFetched.size}")
                mp3Files.addAll(filesFetched)
                setUpRecyclerView(filesFetched)

            }
        }

        recentAdapter.clickListener = this
        setHomePlayerUI()
    }


    private fun setUpRecyclerView(filesFetched: List<Mp3FilesDataClass>) {


        binding.recyclerRecentSongs.adapter = recentAdapter


        recentAdapter.setData(filesFetched)


    }

    fun setHomePlayerUI() {
//        if (Utils.getPlayer()?.isPlaying == true) {
        binding.txtSongName.text = Utils.getPlayer()?.mediaMetadata?.title
        binding.txtArtistName.text = Utils.getPlayer()?.mediaMetadata?.artist
//        }
    }

    override fun playSong(songModel: Mp3FilesDataClass) {
        Log.i("test", "uri->${songModel.path}")
        activity?.let { fragmentActivity ->
            Utils.initPlayer(fragmentActivity)
            Utils.playMedia(fragmentActivity, songModel.path.toUri())
            setHomePlayerUI()
        }


    }
}