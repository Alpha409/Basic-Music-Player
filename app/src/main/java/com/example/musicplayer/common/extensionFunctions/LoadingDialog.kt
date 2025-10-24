package com.example.musicplayer.common.extensionFunctions

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.musicplayer.R

object LoadingDialog {
    // Holds the current visible loading dialog instance
    private var currentLoadingDialog: AlertDialog? = null

    /**
     * Shows a loading dialog with an optional message.
     *
     * @param message Text to display under the loader (default: "Loading...")
     */
    @SuppressLint("MissingInflatedId")
    fun Activity.showLoadingDialog(message: String = "Loading...") {
        hideLoadingDialog() // ensure only one dialog is active

        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_loading, null)
        dialogView.findViewById<TextView>(R.id.loadingMessage)?.text = message

        currentLoadingDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        currentLoadingDialog?.show()
    }

    /**
     * Fragment variant for showing a loading dialog.
     */
    fun Fragment.showLoadingDialog(message: String = "Loading...") {
        activity?.showLoadingDialog(message)
    }

    /**
     * Safely dismiss the current loading dialog.
     */
    fun hideLoadingDialog() {
        currentLoadingDialog?.dismissSafely()
        currentLoadingDialog = null
    }

    /**
     * Safely dismiss the dialog instance if showing.
     */
    fun AlertDialog.dismissSafely() {
        if (isShowing) dismiss()
    }
}