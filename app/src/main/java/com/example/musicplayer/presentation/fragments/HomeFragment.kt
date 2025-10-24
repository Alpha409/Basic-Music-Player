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
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch


class HomeFragment : Fragment(), RecentSongsAdapter.PlaySongClickListenerInterface {
    private lateinit var binding: FragmentHomeBinding
    private val recentAdapter: RecentSongsAdapter by lazy {
        RecentSongsAdapter()
    }

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
//        initClickListener()

        activity?.showLoadingDialog()
        lifecycleScope.launch(IO) {
            (activity as MainActivity).mp3Files.collect { filesFetched ->
                LoadingDialog.hideLoadingDialog()
                Log.i("test", "${filesFetched.size}")
                mp3Files.addAll(filesFetched)

            }
        }
        setUpRecyclerView()
        recentAdapter.clickListener = this
        setHomePlayerUI()
    }

    fun initClickListener() {
        (activity as MainActivity).apply {
            binding.linearMusic.setOnOneClickListener {
                binding.ivMusic.imageTintList =
                    ColorStateList.valueOf(getColor(R.color.itemSelected))
                binding.ivArtist.imageTintList =
                    ColorStateList.valueOf(getColor(R.color.itemUnSelected))
                binding.ivPlaylist.imageTintList =
                    ColorStateList.valueOf(getColor(R.color.itemUnSelected))
                binding.ivFav.imageTintList =
                    ColorStateList.valueOf(getColor(R.color.itemUnSelected))

                findNavControllerSafely()?.navigate(R.id.myMusicFragment)
            }
            binding.linearArtist.setOnOneClickListener {
                binding.ivMusic.imageTintList =
                    ColorStateList.valueOf(getColor(R.color.itemUnSelected))
                binding.ivArtist.imageTintList =
                    ColorStateList.valueOf(getColor(R.color.itemSelected))
                binding.ivPlaylist.imageTintList =
                    ColorStateList.valueOf(getColor(R.color.itemUnSelected))
                binding.ivFav.imageTintList =
                    ColorStateList.valueOf(getColor(R.color.itemUnSelected))
                findNavControllerSafely()?.navigate(R.id.artistFragment)
            }
            binding.linearHome.setOnOneClickListener {
                binding.ivMusic.imageTintList =
                    ColorStateList.valueOf(getColor(R.color.itemUnSelected))
                binding.ivArtist.imageTintList =
                    ColorStateList.valueOf(getColor(R.color.itemUnSelected))
                binding.ivPlaylist.imageTintList =
                    ColorStateList.valueOf(getColor(R.color.itemUnSelected))
                binding.ivFav.imageTintList =
                    ColorStateList.valueOf(getColor(R.color.itemUnSelected))
                findNavControllerSafely()?.navigate(R.id.homeFragment)
            }
            binding.linearPlaylist.setOnOneClickListener {
                binding.ivMusic.imageTintList =
                    ColorStateList.valueOf(getColor(R.color.itemUnSelected))
                binding.ivArtist.imageTintList =
                    ColorStateList.valueOf(getColor(R.color.itemUnSelected))
                binding.ivPlaylist.imageTintList =
                    ColorStateList.valueOf(getColor(R.color.itemSelected))
                binding.ivFav.imageTintList =
                    ColorStateList.valueOf(getColor(R.color.itemUnSelected))
                findNavControllerSafely()?.navigate(R.id.playListFragment)
            }
            binding.linearFavourite.setOnOneClickListener {
                binding.ivMusic.imageTintList =
                    ColorStateList.valueOf(getColor(R.color.itemUnSelected))
                binding.ivArtist.imageTintList =
                    ColorStateList.valueOf(getColor(R.color.itemUnSelected))
                binding.ivPlaylist.imageTintList =
                    ColorStateList.valueOf(getColor(R.color.itemUnSelected))
                binding.ivFav.imageTintList = ColorStateList.valueOf(getColor(R.color.itemSelected))
                findNavControllerSafely()?.navigate(R.id.favouriteFragment)
            }
        }
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