package com.example.musicplayer.viewModel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.musicplayer.data.Mp3FilesDataClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext


class MainViewModel(application: Application) : AndroidViewModel(application) {
    fun getMp3Files(): Flow<List<Mp3FilesDataClass>> = flow {
        val mp3List = mutableListOf<Mp3FilesDataClass>()
        val resolver = getApplication<Application>().contentResolver

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

        withContext(Dispatchers.IO) {
            resolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                sortOrder
            )?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
                val albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
                val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
                val pathColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
                val albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)

                val albumArtProjection = arrayOf(MediaStore.Audio.Albums.ALBUM_ART)

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val title = cursor.getString(titleColumn)
                    val artist = cursor.getString(artistColumn)
                    val album = cursor.getString(albumColumn)
                    val duration = cursor.getLong(durationColumn)
                    val path = cursor.getString(pathColumn)
                    val albumId = cursor.getLong(albumIdColumn)

                    var albumArt: Bitmap? = null
                    resolver.query(
                        MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                        albumArtProjection,
                        "${MediaStore.Audio.Albums._ID}=?",
                        arrayOf(albumId.toString()),
                        null
                    )?.use { albumCursor ->
                        if (albumCursor.moveToFirst()) {
                            val albumArtPath = albumCursor.getString(
                                albumCursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_ART)
                            )
                            albumArt = BitmapFactory.decodeFile(albumArtPath)
                        }
                    }

                    mp3List.add(
                        Mp3FilesDataClass(
                            id = id,
                            title = title,
                            artist = artist,
                            album = album,
                            duration = duration,
                            path = path,
                            albumArt = albumArt
                        )
                    )
                }
            }
        }

        Log.i("Mp3Loader", "Loaded ${mp3List.size} mp3 files")
        emit(mp3List)
    }

}
