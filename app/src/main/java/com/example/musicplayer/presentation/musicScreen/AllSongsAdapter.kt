package com.example.musicplayer.presentation.musicScreen

import android.content.ContentUris
import android.media.MediaMetadataRetriever
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
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


        val retriever = MediaMetadataRetriever()
        try {
            val uri = ContentUris.withAppendedId(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                allSongs[position].id
            )
            retriever.setDataSource(holder.itemView.context, uri) // ✅ safer
        } catch (e: Exception) {
            e.printStackTrace()
        }


// 2. Extract the embedded picture (album art) as a byte array
        val albumArt = retriever.embeddedPicture

// 3. Close the retriever
        retriever.release()
        holder.binding.apply {
        if (albumArt != null) {
            // 4. Use Glide to load the byte array directly into the ImageView
            Glide.with(context)
                .load(albumArt) // Glide can load byte arrays!
                .placeholder(R.drawable.iv_dummy_song)
                .into(this.ivSongImage)
        } else {
            // 5. If no album art is embedded, just load the placeholder
            this.ivSongImage.setImageResource(R.drawable.iv_dummy_song)
        }


/*            Glide.with(context).asBitmap().load(allSongs[position].path)
                .placeholder(R.drawable.iv_dummy_song).into(this.ivSongImage)*/
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

            this.linearNames.setOnOneClickListener {
                allSongsClick?.onItemClick(allSongs[position],position)
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

        fun onItemClick(songClick: Mp3FilesDataClass,position: Int)
    }
}