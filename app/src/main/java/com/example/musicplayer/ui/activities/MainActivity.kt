package com.example.musicplayer.ui.activities


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
        myViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        allSongs = myViewModel.getMp3Files()
        val actionBar: ActionBar? = supportActionBar
        actionBar?.hide()
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        binding.topActionBar.menuButton.setOnClickListener {
            showPopupMenu(it)
        }
        val activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activityResultLauncher.launch(
                arrayOf(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.MANAGE_EXTERNAL_STORAGE
                )
            )
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                checkPermissionForReadExternalStorage()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("ObsoleteSdkInt")
    fun checkPermissionForReadExternalStorage(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            (ActivityCompat.checkSelfPermission(
                applicationContext, android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                applicationContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                applicationContext, android.Manifest.permission.MANAGE_EXTERNAL_STORAGE
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
}