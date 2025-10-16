package com.example.musicplayer.data

import android.graphics.Bitmap

data class Mp3FilesDataClass(
    val id: Long,
    val title: String,
    val artist: String?,
    val album: String?,
    val duration: Long,
    val path: String,
    val albumArt: Bitmap?
)