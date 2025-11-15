package com.example.musicplayer.common

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.media.MediaMetadataRetriever
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.example.musicplayer.R
import com.example.musicplayer.common.extensionFunctions.ViewsExtensionF.hide
import com.example.musicplayer.common.extensionFunctions.ViewsExtensionF.setOnOneClickListener
import com.example.musicplayer.common.extensionFunctions.ViewsExtensionF.show
import com.example.musicplayer.common.utils.Utils
import com.example.musicplayer.databinding.ActivityMainBinding
import com.example.musicplayer.domain.models.Mp3FilesDataClass
import com.example.musicplayer.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding


    private val myViewModel: MainViewModel by viewModels()
    private lateinit var navController: NavController

    // Handle permission result once for all Android versions
    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val denied = permissions.filterValues { !it }.keys
            if (denied.isNotEmpty()) {
                Toast.makeText(this, "Permissions denied: $denied", Toast.LENGTH_SHORT).show()
            } else {
                loadSongs()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        _instance = this
        setupActionBar()
        setupViewModel()
        setupNavigation()
        setupMenuButton()
        requestStoragePermissionsIfNeeded()
        initClickListener()
        Utils.initPlayer(this)
    }

    fun initClickListener() {
        binding.linearMusic.setOnOneClickListener {
            binding.ivMusic.imageTintList = ColorStateList.valueOf(getColor(R.color.itemSelected))
            binding.ivArtist.imageTintList =
                ColorStateList.valueOf(getColor(R.color.itemUnSelected))
            binding.ivPlaylist.imageTintList =
                ColorStateList.valueOf(getColor(R.color.itemUnSelected))
            binding.ivFav.imageTintList = ColorStateList.valueOf(getColor(R.color.itemUnSelected))

            navController.navigate(R.id.myMusicFragment)
        }
        binding.linearArtist.setOnOneClickListener {
            binding.ivMusic.imageTintList = ColorStateList.valueOf(getColor(R.color.itemUnSelected))
            binding.ivArtist.imageTintList = ColorStateList.valueOf(getColor(R.color.itemSelected))
            binding.ivPlaylist.imageTintList =
                ColorStateList.valueOf(getColor(R.color.itemUnSelected))
            binding.ivFav.imageTintList = ColorStateList.valueOf(getColor(R.color.itemUnSelected))
            navController.navigate(R.id.artistFragment)
        }
        binding.linearHome.setOnOneClickListener {
            binding.ivMusic.imageTintList = ColorStateList.valueOf(getColor(R.color.itemUnSelected))
            binding.ivArtist.imageTintList =
                ColorStateList.valueOf(getColor(R.color.itemUnSelected))
            binding.ivPlaylist.imageTintList =
                ColorStateList.valueOf(getColor(R.color.itemUnSelected))
            binding.ivFav.imageTintList = ColorStateList.valueOf(getColor(R.color.itemUnSelected))
            navController.navigate(R.id.homeFragment)
        }
        binding.linearPlaylist.setOnOneClickListener {
            binding.ivMusic.imageTintList = ColorStateList.valueOf(getColor(R.color.itemUnSelected))
            binding.ivArtist.imageTintList =
                ColorStateList.valueOf(getColor(R.color.itemUnSelected))
            binding.ivPlaylist.imageTintList =
                ColorStateList.valueOf(getColor(R.color.itemSelected))
            binding.ivFav.imageTintList = ColorStateList.valueOf(getColor(R.color.itemUnSelected))
            navController.navigate(R.id.playListFragment)
        }
        binding.linearFavourite.setOnOneClickListener {
            binding.ivMusic.imageTintList = ColorStateList.valueOf(getColor(R.color.itemUnSelected))
            binding.ivArtist.imageTintList =
                ColorStateList.valueOf(getColor(R.color.itemUnSelected))
            binding.ivPlaylist.imageTintList =
                ColorStateList.valueOf(getColor(R.color.itemUnSelected))
            binding.ivFav.imageTintList = ColorStateList.valueOf(getColor(R.color.itemSelected))
            navController.navigate(R.id.favouriteFragment)
        }
    }

    /**
     * Hides the ActionBar for fullscreen experience.
     */
    private fun setupActionBar() {
        supportActionBar?.hide()
    }

    /**
     * Initializes ViewModel and loads songs.
     */
    private fun setupViewModel() {
        loadSongs()
    }

    private fun loadSongs() {
        myViewModel.getLocalMp3Files()
        myViewModel.getAllFavSongs()
    }

    override fun onDestroy() {
        super.onDestroy()
        _instance = null
        Utils.releasePlayer()
    }

    /**
     * Sets up NavController for navigation.
     */
    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
    }

    /**
     * Handles Menu button clicks safely with debounce.
     */
    private fun setupMenuButton() {
        binding.btnMenu.setOnOneClickListener { showPopupMenu(it) }
    }

    /**
     * Requests storage permissions dynamically based on Android version.
     */

    private fun requestStoragePermissionsIfNeeded() {
        when {
            // ✅ Android 13 (Tiramisu) and above
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.READ_MEDIA_AUDIO
                    )
                )
            }

            // ✅ Android 11–12 (R, S)
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.MANAGE_EXTERNAL_STORAGE
                    )
                )
            }

            // ✅ Android 10 (Q)
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                )
            }

            // ✅ Android 9 and below
            else -> {
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                )
            }
        }
    }


    fun hideTopBarAndBottomBar() {
        binding.groupTopBar.hide()
        binding.clBottomNav.hide()
    }

    fun showBottomPlayer(songItem: Mp3FilesDataClass, isPlaying: Boolean, isFav: Boolean) {

        var currentFavStatus = isFav
        binding.clBottomMusicPlayer.show()
        binding.txtSongName.text = songItem.title

        binding.txtArtistName.text = songItem.artist
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(songItem.path)

// 2. Extract the embedded picture (album art) as a byte array
        val albumArt = retriever.embeddedPicture

// 3. Close the retriever
        retriever.release()

        Glide.with(this).load(albumArt).placeholder(R.drawable.iv_dummy_song)
            .into(binding.ivSongImage)

        if (isPlaying) {
            binding.btnPlayPause.setImageResource(R.drawable.ic_pause)
        } else {
            binding.btnPlayPause.setImageResource(R.drawable.play)
        }

        if (currentFavStatus) {
            binding.btnFav.setImageResource(R.drawable.heartfilled)
        } else {
            binding.btnFav.setImageResource(R.drawable.heart_empty)
        }

        binding.btnPlayPause.setOnOneClickListener {
            if (Utils.getPlayer()?.isPlaying == true) {
                binding.btnPlayPause.setImageResource(R.drawable.play)
                Utils.getPlayer()?.pause()
            } else {
                binding.btnPlayPause.setImageResource(R.drawable.ic_pause)
                Utils.getPlayer()?.play()

            }
        }


        binding.btnFav.setOnOneClickListener {
            if (currentFavStatus) {
                lifecycleScope.launch(IO) {
                    myViewModel.removeFav(songItem)
                    currentFavStatus = false
                    withContext(Main) {

                        binding.btnFav.setImageResource(R.drawable.heart_empty)
                    }
                }
            } else {
                lifecycleScope.launch(IO) {
                    myViewModel.insertFav(songItem)

                    currentFavStatus = true
                    withContext(Main) {

                        binding.btnFav.setImageResource(R.drawable.heartfilled)
                    }
                }
            }
        }


    }

    fun hideBottomPlayer() {
        binding.clBottomMusicPlayer.hide()
    }

    fun showTopBarAndBottomBar() {
        binding.groupTopBar.show()
        binding.clBottomNav.show()
    }

    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("ObsoleteSdkInt")
    fun checkPermissionForReadExternalStorage(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            (ActivityCompat.checkSelfPermission(
                applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                applicationContext, Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                applicationContext, Manifest.permission.MANAGE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED)
        } else false
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(
            this,
            view,
            Gravity.END,
            androidx.appcompat.R.attr.actionOverflowMenuStyle,
            R.style.MyPopupMenu
        )
        popupMenu.inflate(R.menu.menu_main)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_item_favor -> {
                    // Handle item 1 click
                    true
                }

                R.id.menu_item_share -> {
                    // Handle item 2 click
                    true
                }

                R.id.menu_item_rate_us -> {
                    // Handle item 3 click
                    true
                }

                R.id.menu_item_feedback -> {
                    // Handle item 3 click
                    true
                }

                R.id.menu_item_privacy -> {
                    // Handle item 3 click
                    true
                }

                else -> false
            }
        }
        popupMenu.show()
    }



    companion object {
        @Volatile
        private var _instance: MainActivity? = null

        /**
         * Returns the current instance of [MainActivity] if it's available.
         * Use this carefully — it may return null if the activity is not alive.
         */
        val instance: MainActivity?
            get() = _instance
    }
}