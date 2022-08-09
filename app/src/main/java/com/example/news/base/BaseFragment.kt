package com.example.news.base

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.news.NewsActivity
import com.example.news.R
import com.example.news.model.ArticlesBean
import com.example.news.util.messageDialog

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    protected open var navigationVisibility = View.GONE

    protected open var isRootFragment = false

    protected open var optionsMenuId: Int? = null

    private lateinit var _binding: VB
    protected val mBinding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = initViewBinding(inflater, container, false)
        return _binding.root
    }

    abstract fun initViewBinding(inflater: LayoutInflater, container: ViewGroup?, boolean: Boolean)
            : VB

    protected open fun initView() {}

    protected open fun initAction() {}

    protected open fun observeData() {}

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

    fun pushFragment(fragment: Fragment) {
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