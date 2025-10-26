package com.example.musicplayer.viewModel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.domain.models.Mp3FilesDataClass
import com.example.musicplayer.domain.repository.FavSongsRepo
import com.example.musicplayer.domain.repository.GetMusicLocalRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    var localRepo: GetMusicLocalRepo, var favRepo: FavSongsRepo
) : ViewModel() {


    private val _favList = MutableStateFlow<List<Mp3FilesDataClass>>(emptyList())
    val favList: StateFlow<List<Mp3FilesDataClass>> = _favList
    private val _mp3Files = MutableStateFlow<List<Mp3FilesDataClass>>(emptyList())
    val mp3Files: StateFlow<List<Mp3FilesDataClass>> = _mp3Files.asStateFlow()


    fun getLocalMp3Files() {
        viewModelScope.launch(IO) {

            localRepo.getMp3LocalFiles().collect {
                _mp3Files.emit(it)
            }
        }
    }

    fun getAllFavSongs() {
        viewModelScope.launch(IO) {
            favRepo.getAllFavSongs().collect {
                _favList.emit(it)
            }
        }

    }

    suspend fun insertFav(favSong: Mp3FilesDataClass) {
        favRepo.insertFav(favSong)
    }

    suspend fun removeFav(removeSong: Mp3FilesDataClass) {
        favRepo.removeFav(removeSong)
    }

}
