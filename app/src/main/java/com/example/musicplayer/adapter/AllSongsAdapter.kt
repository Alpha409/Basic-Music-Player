package com.example.musicplayer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicplayer.R
import com.example.musicplayer.common.extensionFunctions.ViewsExtensionF.setOnOneClickListener
import com.example.musicplayer.databinding.RecyclerViewAllSongsItemBinding
import com.example.musicplayer.domain.models.Mp3FilesDataClass

class AllSongsAdapter(

) : RecyclerView.Adapter<AllSongsAdapter.AllSongsViewHolder>() {

    private var allSongs: MutableList<Mp3FilesDataClass> = mutableListOf()
    var allSongsClick: AllSongsClickListener? = null

    inner class AllSongsViewHolder(val binding: RecyclerViewAllSongsItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllSongsViewHolder {
        val binding = RecyclerViewAllSongsItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AllSongsViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return allSongs.size
    }

    override fun onBindViewHolder(holder: AllSongsViewHolder, position: Int) {
        val context = holder.binding.root.context
        holder.binding.apply {
            Glide.with(context).asBitmap().load(allSongs[position].path)
                .placeholder(R.drawable.playingnow).into(this.ivSongImage)
            this.txtSongName.text = allSongs[position].title
            this.txtArtistName.text = allSongs[position].artist

            val drawableRes = if (allSongs[position].isFav) {
                R.drawable.heartfilled
            } else {
                R.drawable.heart_empty
            }
            this.ivFav.setImageResource(drawableRes)
            this.ivFav.setOnOneClickListener {
                val song = allSongs[position]

                // Toggle the favorite state
                song.isFav = !song.isFav

                // Update the icon
                val drawableRes = if (song.isFav) {
                    R.drawable.heartfilled
                } else {
                    R.drawable.heart_empty
                }
                this.ivFav.setImageResource(drawableRes)

                // Notify callback
                allSongsClick?.onFavClick(song, position)
            }

        }
    }

    fun setAllSongsData(allList: List<Mp3FilesDataClass>) {
        allSongs.clear()
        allSongs.addAll(allList)
        notifyDataSetChanged()
    }

    interface AllSongsClickListener {
        fun onFavClick(favSong: Mp3FilesDataClass, position: Int)
    }
}