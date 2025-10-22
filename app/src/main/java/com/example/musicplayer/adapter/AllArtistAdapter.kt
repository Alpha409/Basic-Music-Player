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
import com.example.musicplayer.domain.models.Mp3FilesDataClass
import com.example.musicplayer.interfaces.BottomMenuClickInterface

class AllArtistAdapter(
    private val context: Context, private var Mp3ModelClass: List<Mp3FilesDataClass>,private val showBottomMenu:BottomMenuClickInterface
) : RecyclerView.Adapter<AllArtistAdapter.AllArtistViewHolder>() {
    class AllArtistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtArtistName: TextView = itemView.findViewById(R.id.txt_artist_name)
        val txtArtistSongs: TextView = itemView.findViewById(R.id.txt_artist_songs)
        val songImage: ImageView = itemView.findViewById(R.id.iv_song_image)
        val bottomMenu:LinearLayout=itemView.findViewById(R.id.linear_menu)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllArtistViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_artist_item, parent, false)
        return AllArtistViewHolder(view)
    }

    override fun getItemCount(): Int {
        return Mp3ModelClass.size
    }

    override fun onBindViewHolder(holder: AllArtistViewHolder, position: Int) {
        holder.bottomMenu.setOnClickListener {
            showBottomMenu.showBottomMenu(Mp3ModelClass[position])
        }
        Glide.with(context).asBitmap().load(Mp3ModelClass[position].albumArt)
            .placeholder(R.drawable.playingnow).into(holder.songImage)
        holder.txtArtistName.text = Mp3ModelClass[position].artist
        holder.txtArtistSongs.text = Mp3ModelClass[position].artist
    }
}