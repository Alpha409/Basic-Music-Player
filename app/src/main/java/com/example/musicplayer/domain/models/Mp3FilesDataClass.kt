package com.example.musicplayer.domain.models

import android.graphics.Bitmap
import androidx.room.Entity


@Entity(tableName = "fav_songs")
data class Mp3FilesDataClass(
    val id: Long,
    val title: String,
    val artist: String?,
    val album: String?,
    val duration: Long,
    val path: String,
    val albumArt: Bitmap?,
    var isFav: Boolean = false
)