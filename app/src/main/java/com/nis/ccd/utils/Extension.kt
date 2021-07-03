package com.nis.ccd.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.nis.ccd.R

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.showToast(msg: Int) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun dismissKeyboard(view: View) {
    val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun View.showKeyboard() {
    this.requestFocus()
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun Activity.unblockInput() {
    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
}

fun ImageView.loadPic(url: String) {
    Glide.with(this)
        .load(url)
        .placeholder(R.drawable.ic_broken)
        .into(this)
}

fun ImageView.loadPic(url: String, placeholder: Int) {
    Glide.with(this)
        .load(url)
        .placeholder(placeholder)
        .into(this)
}

fun Context.showAlert(msg: String, confirmation: OnSuccess<Boolean>) {
    AlertDialog.Builder(this).apply {
        setMessage(msg)
        setCancelable(true)
        setPositiveButton("Yes") { dialog, which ->
            confirmation.invoke(true)
            dialog.dismiss()
        }
        setNegativeButton("No") { dialog, which ->
            dialog.cancel()
        }
    }.show()
}
