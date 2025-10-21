package com.example.musicplayer.common.extensionFunctions

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.musicplayer.R

object LoadingDialog {
    /**
     * Shows a loading dialog with an optional message.
     *
     * @param message Text to display under the loader (default: "Loading...")
     * @return The created [AlertDialog] instance so it can be dismissed later.
     */
    @SuppressLint("MissingInflatedId")
    fun Activity.showLoadingDialog(message: String = "Loading..."): AlertDialog {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_loading, null)
        dialogView.findViewById<TextView>(R.id.loadingMessage)?.text = message

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()
        dialog.show()
        return dialog
    }

    /**
     * Fragment variant for showing a loading dialog.
     */
    fun Fragment.showLoadingDialog(message: String = "Loading..."): AlertDialog? {
        return activity?.showLoadingDialog(message)
    }

    /**
     * Dismiss the loading dialog safely.
     */
    fun AlertDialog.dismissSafely() {
        if (isShowing) dismiss()
    }
}