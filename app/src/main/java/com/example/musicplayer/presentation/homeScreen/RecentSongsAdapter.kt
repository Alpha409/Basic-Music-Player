package com.example.musicplayer.presentation.homeScreen

import android.content.ContentUris
import android.media.MediaMetadataRetriever
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicplayer.R
import com.example.musicplayer.databinding.RecyclerViewRecentItemBinding
import com.example.musicplayer.domain.models.Mp3FilesDataClass

class RecentSongsAdapter() : RecyclerView.Adapter<RecentSongsAdapter.RecentSongsViewHolder>() {

    var clickListener: PlaySongClickListenerInterface? = null
    var musicList: MutableList<Mp3FilesDataClass> = mutableListOf()



    class RecentSongsViewHolder(var binding: RecyclerViewRecentItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentSongsViewHolder {
        val binding = RecyclerViewRecentItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecentSongsViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return musicList.size
    }

    fun setData(songList:List<Mp3FilesDataClass>){
        musicList.clear()
        musicList.addAll(songList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecentSongsViewHolder, position: Int) {
        val maxLength = 10
        val context= holder.binding.root.context
        val truncatedTitle = musicList[position].title.take(maxLength)
        holder.binding.apply {
            val retriever = MediaMetadataRetriever()
            try {
                val uri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    musicList[position].id
                )
                retriever.setDataSource(holder.itemView.context, uri) // ✅ safer
            } catch (e: Exception) {
                e.printStackTrace()
            }


// 2. Extract the embedded picture (album art) as a byte array
            val albumArt = retriever.embeddedPicture

// 3. Close the retriever
            retriever.release()
            Glide.with(context).load(albumArt)
                .placeholder(R.drawable.iv_dummy_song).into(coverImage)
            txtSongName.text = truncatedTitle
            txtArtistName.text = musicList[position].artist
            mainLayout.setOnClickListener {
                clickListener?.playSong(musicList[position])
            }
        }
    }

    interface PlaySongClickListenerInterface {
        fun playSong(songModel: Mp3FilesDataClass)
    }
}