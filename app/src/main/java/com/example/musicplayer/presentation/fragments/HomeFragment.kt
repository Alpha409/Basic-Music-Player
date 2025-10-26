package com.example.musicplayer.presentation.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.musicplayer.R
import com.example.musicplayer.adapter.RecentSongsAdapter
import com.example.musicplayer.common.extensionFunctions.LoadingDialog
import com.example.musicplayer.common.extensionFunctions.LoadingDialog.showLoadingDialog
import com.example.musicplayer.common.extensionFunctions.NavigationExtensionF.findNavControllerSafely
import com.example.musicplayer.common.extensionFunctions.ViewsExtensionF.setOnOneClickListener
import com.example.musicplayer.common.utils.Utils
import com.example.musicplayer.databinding.FragmentHomeBinding
import com.example.musicplayer.domain.models.Mp3FilesDataClass
import com.example.musicplayer.presentation.activities.MainActivity
import com.example.musicplayer.viewModel.MainViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlin.getValue


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
        MainActivity.instance?.hideBottomPlayer()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MainActivity.instance?.showTopBarAndBottomBar()

        activity?.showLoadingDialog()
        lifecycleScope.launch(IO) {
            mainViewModel.mp3Files.collect { filesFetched ->
                LoadingDialog.hideLoadingDialog()
                Log.i("test", "${filesFetched.size}")
                mp3Files.addAll(filesFetched)

            }
        }
        setUpRecyclerView()
        recentAdapter.clickListener = this
        setHomePlayerUI()
    }


    private fun setUpRecyclerView() {
        activity?.let { context ->
            if (::binding.isInitialized) {
                binding.recyclerRecentSongs.apply {
                    setHasFixedSize(true)
                    val handler = Handler(Looper.getMainLooper())
                    handler.post { adapter = recentAdapter }

                    recentAdapter.setData(mp3Files)

                }
            }
        }
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