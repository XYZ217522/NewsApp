package com.example.news.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import java.net.URLEncoder

fun Context?.shareFacebook(content: String, url: String) {
    this ?: return
    if (PackageUtil.isFacebookInstalled(this)) {
        Intent(Intent.ACTION_SEND)
            .setType("text/plain")
            .setPackage(PackageUtil.FACEBOOK_PACKAGE)
            .putExtra(Intent.EXTRA_TEXT, content.plus("\n$url"))
            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            .let { this.startActivity(it) }
    }
}

fun Context?.shareLine(content: String, url: String) {
    this ?: return
    if (PackageUtil.isLineInstalled(this)) {
        val shareContent = URLEncoder.encode("$content $url", "UTF-8")
        val uriString = String.format("line://msg/text/?%s", shareContent)
        val uri = Uri.parse(uriString)
        this.startActivity(Intent(Intent.ACTION_VIEW, uri))
    }
}

fun Context?.shareWhatsApp(content: String, url: String) {
    this ?: return
    if (PackageUtil.isWhatsAppInstalled(this)) {
        Intent(Intent.ACTION_SEND)
            .setType("text/plain")
            .setPackage(PackageUtil.WHATSAPP_PACKAGE)
            .putExtra(Intent.EXTRA_TEXT, content.plus("\n$url"))
            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            .let { this.startActivity(it) }
    }
}

fun Context?.copyTextToClipboard(text: String, toastText: String = "") {
    this ?: return
    val clipboardManager = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = ClipData.newPlainText(null, text)
    clipboardManager.setPrimaryClip(clipData)
    this.showToast(toastText)
}

