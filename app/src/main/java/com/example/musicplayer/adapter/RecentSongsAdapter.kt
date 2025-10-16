package com.example.musicplayer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicplayer.R
import com.example.musicplayer.data.Mp3FilesDataClass
import com.example.musicplayer.interfaces.PlaySongClickListernerInterface

class RecentSongsAdapter(
    private val context: Context,
    private var Mp3ModelClass: ArrayList<Mp3FilesDataClass>,
    private val PlaySongs: PlaySongClickListernerInterface
) : RecyclerView.Adapter<RecentSongsAdapter.RecentSongsViewHolder>() {
    class RecentSongsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtSongName: TextView = itemView.findViewById(R.id.txt_song_name)
        val txtArtistName: TextView = itemView.findViewById(R.id.txt_artist_name)
        val coverImage: ImageView = itemView.findViewById(R.id.cover)
        val playRecentSong: LinearLayout = itemView.findViewById(R.id.linear_play_recent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentSongsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_recent_item, parent, false)
        return RecentSongsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return Mp3ModelClass.size
    }

    override fun onBindViewHolder(holder: RecentSongsViewHolder, position: Int) {
        val maxLength = 10
        val truncatedTitle = Mp3ModelClass[position].title.substring(0, maxLength)
        Glide.with(context).asBitmap().load(Mp3ModelClass[position].albumArt)
            .placeholder(R.drawable.playingnow).into(holder.coverImage)
        holder.txtSongName.text = truncatedTitle
        holder.txtArtistName.text = Mp3ModelClass[position].artist
        holder.playRecentSong.setOnClickListener {
            PlaySongs.PlaySong(Mp3ModelClass[position])
        }
    }
}