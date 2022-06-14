package com.example.news.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.news.NewsActivity

abstract class BaseFragment : Fragment() {

    protected open var navigationVisibility = View.GONE

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity is NewsActivity) {
            (activity as NewsActivity).setBottomNavigationVisibility(navigationVisibility)
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
}