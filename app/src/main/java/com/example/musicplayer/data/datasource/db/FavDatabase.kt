package com.example.musicplayer.data.datasource.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.musicplayer.domain.models.Mp3FilesDataClass


@Database(entities = [Mp3FilesDataClass::class], version = 1, exportSchema = false)
abstract class FavDatabase : RoomDatabase() {
    abstract fun favDao(): FavSongsDao
}