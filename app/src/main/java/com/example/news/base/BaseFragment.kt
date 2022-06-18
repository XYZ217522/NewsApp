package com.example.news.base

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.news.NewsActivity
import com.example.news.R
import com.example.news.model.ArticlesBean
import com.example.news.util.messageDialog

abstract class BaseFragment : Fragment() {

    protected open var navigationVisibility = View.GONE

    protected open var isRootFragment = false

    protected open var optionsMenuId: Int? = null

    protected open fun getSupportActionBar(): Toolbar? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (optionsMenuId != null) {
            setHasOptionsMenu(true)
        }
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        val menuId = optionsMenuId ?: return
        inflater.inflate(menuId, menu)
    }

    fun pushFragment(fragment: BaseFragment) {
        if (activity is NewsActivity) {
            (activity as NewsActivity).pushFragment(fragment)
        }
    }

    fun pushWebViewFragment(articlesBean: ArticlesBean) {
        val url = articlesBean.url ?: run {
            activity?.messageDialog("url not founded")?.show()
            return
        }
        pushFragment(NewsWebViewFragment.newInstance(url, articlesBean.title))
    }
}