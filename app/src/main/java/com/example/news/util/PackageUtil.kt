package com.example.news.util

import android.content.Context

object PackageUtil {

    fun isFacebookMessengerInstalled(context: Context): Boolean {
        val isInstall = checkIsInstallApp(context, "com.facebook.orca")
        if (!isInstall) context.showToast("FacebookMessenger is not Installed!!")
        return isInstall
    }

    fun isFacebookInstalled(context: Context): Boolean {
        val isInstall = checkIsInstallApp(context, "com.facebook.katana")
        if (!isInstall) context.showToast("Facebook is not Installed!!")
        return isInstall
    }

    fun isLineInstalled(context: Context): Boolean {
        val isInstall = checkIsInstallApp(context, "jp.naver.line.android")
        if (!isInstall) context.showToast("Line not Installed!!")
        return isInstall
    }

    private fun checkIsInstallApp(context: Context, packageName: String): Boolean {
        val packageManager = context.packageManager
        val packageInfoList = packageManager.getInstalledPackages(0)
        for (i in packageInfoList.indices) {
            packageInfoList[i] ?: continue
            val installPackageName = packageInfoList[i].packageName
            if (installPackageName != null && packageName == installPackageName) {
                return true
            }
        }
        return false
    }
}