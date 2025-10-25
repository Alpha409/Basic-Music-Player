package com.example.musicplayer.viewModel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.musicplayer.domain.models.Mp3FilesDataClass
import com.example.musicplayer.domain.repository.FavSongsRepo
import com.example.musicplayer.domain.repository.GetMusicLocalRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    var localRepo: GetMusicLocalRepo, var favRepo: FavSongsRepo
) : ViewModel() {


    private val _favList = MutableStateFlow<List<Mp3FilesDataClass>>(emptyList())
    val favList: StateFlow<List<Mp3FilesDataClass>> = _favList

    fun getLocalMp3Files(): Flow<List<Mp3FilesDataClass>> {
        return localRepo.getMp3LocalFiles()
    }

    suspend fun getAllFavSongs(): Flow<List<Mp3FilesDataClass>> {
        return favRepo.getAllFavSongs()

    }

    suspend fun insertFav(favSong: Mp3FilesDataClass) {
        favRepo.insertFav(favSong)
    }

}
