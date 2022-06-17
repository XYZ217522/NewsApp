package com.example.news.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import java.net.URLEncoder

const val TAG = "ShareExtensions"
const val EXTRA_PROTOCOL_VERSION = "com.facebook.orca.extra.PROTOCOL_VERSION"
const val EXTRA_APP_ID = "com.facebook.orca.extra.APPLICATION_ID"
const val PROTOCOL_VERSION = 20150314

@Throws(Exception::class)
fun Context?.shareFacebookMessage(content: String, url: String) {
    this ?: return
    if (PackageUtil.isFacebookMessengerInstalled(this)) {
        val shareContent = "$content $url"
        val intent = Intent(Intent.ACTION_SEND)
        intent.`package` = "com.facebook.orca"
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, shareContent)
        intent.putExtra(EXTRA_PROTOCOL_VERSION, PROTOCOL_VERSION)
//        intent.putExtra(EXTRA_APP_ID, appID)
        try {
            this.startActivity(intent)
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }
}

//fun Context?.shareFacebook(url: String) {
//    this ?: return
//    if (PackageUtil.isFacebookInstalled(this)) {
//        if (this !is Activity) return
//        val linkContent = ShareLinkContent.Builder().setContentUrl(Uri.parse(url)).build()
//        ShareDialog(this).show(linkContent)
//    }
//}

fun Context?.shareLine(content: String, url: String) {
    this ?: return
    if (PackageUtil.isLineInstalled(this)) {
        val shareContent = URLEncoder.encode("$content $url", "UTF-8")
        val uriString = String.format("line://msg/text/?%s", shareContent)
        val uri = Uri.parse(uriString)
        val intentTest = Intent(Intent.ACTION_VIEW, uri)
        this.startActivity(intentTest)
    }
}

fun Context?.copyTextToClipboard(text: String, toastText: String = "") {
    this ?: return
    val clipboardManager = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = ClipData.newPlainText(null, text)
    clipboardManager.setPrimaryClip(clipData)
    this.showToast(toastText)
}

