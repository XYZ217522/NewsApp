package com.example.news.util

/** Activity or Fragment controller class*/
sealed class ViewStatus {

    class ShowToast(val msg: String) : ViewStatus()

    class ShowDialog(val msg: String, val title: String? = null) : ViewStatus()

    class GetDataFail(val msg: String) : ViewStatus()

    object Loading : ViewStatus()

    object ScrollToUp : ViewStatus()

    object GetDataSuccess : ViewStatus()
}