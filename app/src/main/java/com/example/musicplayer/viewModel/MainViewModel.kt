package com.example.musicplayer.viewModel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.data.Mp3FilesDataClass
import kotlinx.coroutines.launch

private var mp3Files: ArrayList<Mp3FilesDataClass> = arrayListOf()

class MainViewModel(application: Application) : AndroidViewModel(application) {
    fun getMp3Files(): ArrayList<Mp3FilesDataClass> {
        viewModelScope.launch {
            val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
            val projection = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ALBUM_ID
            )
            val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"
            val cursor = getApplication<Application>().contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, null, sortOrder
            )
            cursor?.let { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
                val albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
                val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
                val pathColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
                val albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
                val albumArtColumn = arrayOf(MediaStore.Audio.Albums.ALBUM_ART)
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val title = cursor.getString(titleColumn)
                    val artist = cursor.getString(artistColumn)
                    val album = cursor.getString(albumColumn)
                    val duration = cursor.getLong(durationColumn)
                    val path = cursor.getString(pathColumn)
                    val albumId = cursor.getLong(albumIdColumn)
                    val albumArtCursor = getApplication<Application>().contentResolver.query(
                        MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                        albumArtColumn,
                        MediaStore.Audio.Albums._ID + " = ?",
                        arrayOf(albumId.toString()),
                        null
                    )
                    var albumArt: Bitmap? = null
                    if (albumArtCursor?.moveToFirst() == true) {
                        val albumArtPath =
                            albumArtCursor.getString(albumArtCursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_ART))
                        albumArt = BitmapFactory.decodeFile(albumArtPath)
                    }
                    albumArtCursor?.close()
                    val mp3File =
                        Mp3FilesDataClass(id, title, artist, album, duration, path, albumArt)
                    mp3Files.add(mp3File)
                }
            }
            cursor?.close()
            Log.i("test", "size of item gotten-> ${mp3Files.size}")
        }
        return mp3Files
    }
}
