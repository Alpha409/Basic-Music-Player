package com.example.musicplayer.presentation.activities


import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.PopupMenu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.musicplayer.R
import com.example.musicplayer.common.extensionFunctions.ViewsExtensionF.hide
import com.example.musicplayer.common.extensionFunctions.ViewsExtensionF.setOnOneClickListener
import com.example.musicplayer.common.extensionFunctions.ViewsExtensionF.show
import com.example.musicplayer.data.Mp3FilesDataClass
import com.example.musicplayer.databinding.ActivityMainBinding
import com.example.musicplayer.viewModel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    lateinit var binding: ActivityMainBinding
    private lateinit var myViewModel: MainViewModel
    var allSongs: ArrayList<Mp3FilesDataClass> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        _instance = this
        myViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        allSongs = myViewModel.getMp3Files()
        val actionBar: ActionBar? = supportActionBar
        actionBar?.hide()
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        binding.btnMenu.setOnOneClickListener {
            showPopupMenu(it)
        }
        val activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activityResultLauncher.launch(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.MANAGE_EXTERNAL_STORAGE
                )
            )
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                checkPermissionForReadExternalStorage()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _instance = null
    }

    fun hideTopBarAndBottomBar() {
        binding.groupTopBar.hide()
        binding.clBottomNav.hide()
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