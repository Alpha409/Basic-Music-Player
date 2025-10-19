package com.example.musicplayer.ui.activities

import android.os.Bundle
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.example.musicplayer.databinding.ActivityPlayMusicBinding
import com.google.android.exoplayer2.SimpleExoPlayer




class PlayMusicActivity : AppCompatActivity() {
    private lateinit var binding:ActivityPlayMusicBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayMusicBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}
