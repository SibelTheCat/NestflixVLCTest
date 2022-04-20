package com.example.nestflixtest


import android.app.AlertDialog
import android.app.Dialog
import android.content.Context



class ProgressDialog  {
    private val delayMs = 10
    private var isVisible = false
    private var startTime: Long = 0
    private var dialog: Dialog? = null

    fun ProgressDialog(context: Context?) {
        dialog = AlertDialog.Builder(context)
            .setView(R.layout.progress)
            .setCancelable(false)
            .create()
    }

    fun show() {
        if (isVisible) {
            return
        }
        if (startTime == 0L) {
            startTime = System.currentTimeMillis()
        }
        if (System.currentTimeMillis() - startTime > delayMs) {
            isVisible = true
            dialog!!.show()
        }
    }

    fun hide() {
        startTime = 0
        isVisible = false
        dialog!!.hide()
    }
}
