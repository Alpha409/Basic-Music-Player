package com.example.musicplayer.presentation.homeScreen

import android.media.MediaMetadataRetriever
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
            retriever.setDataSource(musicList[position].path)

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