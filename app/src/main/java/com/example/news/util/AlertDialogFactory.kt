package com.example.news.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.news.R

fun Context.messageDialog(msg: String?, title: String? = null): AlertDialog? {
    msg ?: return null
    return AlertDialog.Builder(this)
        .setMessage(msg)
        .setTitle(title)
        .setNegativeButton(R.string.btn_confirm) { dialog, _ -> dialog.cancel() }
        .create()
}

fun Context.shareDialog(url: String, title: String, content: String): AlertDialog {

    val titleView = LayoutInflater.from(this).inflate(R.layout.dialog_title, null)
    val contentView = LayoutInflater.from(this).inflate(R.layout.dialog_share, null)
    val dialog = AlertDialog.Builder(this).setCustomTitle(titleView).setView(contentView).create()

    titleView.findViewById<TextView>(R.id.tv_dialog_title).text = title
    titleView.findViewById<View>(R.id.fl_close).setOnClickListener { dialog.dismiss() }

    /** Line */
    contentView.findViewById<ImageView>(R.id.iv_share_line).setOnClickListener {
        dialog.dismiss()
        this.shareLine(content, url)
    }

    /** WhatsApp */
    contentView.findViewById<ImageView>(R.id.iv_share_whatsapp).setOnClickListener {
        dialog.dismiss()
        this.shareWhatsApp(content, url)
    }

    /** Facebook */
    contentView.findViewById<ImageView>(R.id.iv_share_fb).setOnClickListener {
        dialog.dismiss()
        this.shareFacebook(content, url)
    }


    /** chrome */
    contentView.findViewById<ImageView>(R.id.iv_chrome).setOnClickListener {
        dialog.dismiss()
        this.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    /** copyTextToClipboard */
    contentView.findViewById<ImageView>(R.id.iv_copy_url).setOnClickListener {
        this.copyTextToClipboard(url, "Link to this page Copy to Clipboard!")
    }

    return dialog
}