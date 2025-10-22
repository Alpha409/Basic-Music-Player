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
import com.example.musicplayer.domain.models.Mp3FilesDataClass

class AllSongsAdapter(
    private val context: Context, private var Mp3ModelClass: List<Mp3FilesDataClass>
) : RecyclerView.Adapter<AllSongsAdapter.AllSongsViewHolder>() {
    class AllSongsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtSongName: TextView = itemView.findViewById(R.id.txt_song_name)
        val txtArtistName: TextView = itemView.findViewById(R.id.txt_artist_name)
        val songImage: ImageView = itemView.findViewById(R.id.iv_song_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllSongsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_all_songs_item, parent, false)
        return AllSongsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return Mp3ModelClass.size
    }

    override fun onBindViewHolder(holder: AllSongsViewHolder, position: Int) {
//        val maxLength = 20
//        val truncatedTitle = Mp3ModelClass[position].title.substring(0, maxLength)
        Glide.with(context).asBitmap().load(Mp3ModelClass[position].albumArt)
            .placeholder(R.drawable.playingnow).into(holder.songImage)
        holder.txtSongName.text = Mp3ModelClass[position].title
        holder.txtArtistName.text = Mp3ModelClass[position].artist
    }
}