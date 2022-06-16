package com.example.news.base

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.news.NewsActivity
import com.example.news.R

abstract class BaseFragment : Fragment() {

    protected open var navigationVisibility = View.GONE

    protected open var isRootFragment = false

    protected open fun getSupportActionBar(): Toolbar? {
        return null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity is NewsActivity) {
            (activity as NewsActivity).setBottomNavigationVisibility(navigationVisibility)
        }

        // toolbar setting
        getSupportActionBar()?.let {
            it.title = ""
            (activity as AppCompatActivity).setSupportActionBar(it)
            if (!isRootFragment) it.setNavigationIcon(R.drawable.back_arrow)
        }
    }

    override fun onResume() {
        super.onResume()
        if (activity is NewsActivity) {
            (activity as NewsActivity).setBottomNavigationVisibility(navigationVisibility)
        }
    }

    fun pushFragment(fragment: BaseFragment) {
        if (activity is NewsActivity) {
            (activity as NewsActivity).pushFragment(fragment)
        }
    }

    fun messageDialog(msg: String?, title: String? = null): AlertDialog? {
        msg ?: return null
        val context = activity ?: return null
        return AlertDialog.Builder(context)
            .setMessage(msg)
            .setTitle(title)
            .setNegativeButton(R.string.btn_confirm) { dialog, _ -> dialog.cancel() }
            .create()
    }
}