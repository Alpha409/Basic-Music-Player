package com.example.musicplayer.presentation.artistScreen

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.musicplayer.R
import com.example.musicplayer.presentation.artistScreen.AllArtistAdapter
import com.example.musicplayer.common.MainActivity
import com.example.musicplayer.databinding.FragmentArtistBinding
import com.example.musicplayer.domain.models.Mp3FilesDataClass
import com.example.musicplayer.interfaces.BottomMenuClickInterface
import com.example.musicplayer.viewModel.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArtistFragment : Fragment(), BottomMenuClickInterface {
    private lateinit var binding: FragmentArtistBinding
    private lateinit var allArtistAdapter: AllArtistAdapter
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentArtistBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        lifecycleScope.launch(Dispatchers.IO) {

            mainViewModel.mp3Files.collect { allArtists ->

                 Log.i("test", "My Music Frag->${allArtists.size} ")
                 setUpRecyclerView(allArtists)
             }
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