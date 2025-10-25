package com.example.musicplayer.data.datasource

import com.example.musicplayer.data.datasource.db.FavSongsDao
import com.example.musicplayer.domain.models.Mp3FilesDataClass
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavSongsDataSource @Inject constructor(val favDao: FavSongsDao) {

    suspend fun getAllFavSongs(): Flow<List<Mp3FilesDataClass>>{
        return favDao.getAllFavSongs()
    }

    suspend fun insertFav(favSong: Mp3FilesDataClass){
        favDao.insertFav(favSong)
    }

}