package com.example.musicplayer.interfaces

import com.example.musicplayer.domain.models.Mp3FilesDataClass

interface PlaySongClickListernerInterface {
    fun PlaySong(Mp3Songs: Mp3FilesDataClass)
}