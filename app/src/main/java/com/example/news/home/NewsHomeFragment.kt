package com.example.news.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news.R
import com.example.news.base.BaseFragment
import com.example.news.base.NewsWebViewFragment
import com.example.news.databinding.FragmentNewsHomeBinding
import com.example.news.home.adapter.HomeEpoxyCallback
import com.example.news.home.adapter.HomeEpoxyController
import com.example.news.model.ArticlesBean
import com.example.news.search.SearchFragment
import com.example.news.util.EndlessScrollListener
import com.example.news.util.ViewStatus
import com.example.news.util.messageDialog
import com.example.news.util.setAnimation
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewsHomeFragment : BaseFragment(), HomeEpoxyCallback {

    companion object {
        const val TAG = "NewsHomeFragment"
        fun newInstance() = NewsHomeFragment()
    }

    private val mHomeViewModel: HomeViewModel by viewModel()

    private lateinit var mBinding: FragmentNewsHomeBinding

    private val mHomeEpoxyController by lazy { HomeEpoxyController(this) }

    override var navigationVisibility = View.VISIBLE

    override var isRootFragment = true

    override var optionsMenuId: Int? = R.menu.home_menu

    override fun getSupportActionBar(): Toolbar = mBinding.homeActionbar

    override fun onCreateView(
        inflater: LayoutInflater,
        viewGroup: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreateView(inflater, viewGroup, savedInstanceState)
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_news_home,
            viewGroup,
            false
        )
        mBinding.lifecycleOwner = viewLifecycleOwner
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.rvHome.apply {
            this.adapter = mHomeEpoxyController.adapter
            val layoutManager = LinearLayoutManager(activity)
            this.layoutManager = layoutManager
            this.addOnScrollListener(object : EndlessScrollListener(layoutManager) {
                override fun onLoadMore(currentPage: Int) {
                    mHomeViewModel.loadMore()
                }
            })
            mHomeEpoxyController.requestModelBuild()//t
        }

        mBinding.tvTitle.setOnClickListener {
            mHomeEpoxyController.changeMode().let { mBinding.rvHome.setAnimation(it) }
        }

        binding()
    }

    private fun binding() {

        mHomeViewModel.titleLiveData.observe(viewLifecycleOwner) {
            Log.d(TAG, "title = $it")
            it ?: return@observe
            mBinding.tvTitle.text = it
            mHomeEpoxyController.mSelectDomain = it
        }

        mHomeViewModel.newsEverythingLiveData.observe(viewLifecycleOwner) {
            Log.d(TAG, "data = $it")
            val newsData = it?.getContentIfNotHandled() ?: return@observe
            mHomeEpoxyController.setNewsData(newsData)
        }

        mHomeViewModel.viewStatusLiveData.observe(viewLifecycleOwner) {
            Log.d(TAG, "view status = $it")
            val viewStatus = it?.getContentIfNotHandled() ?: return@observe
            when (viewStatus) {
                is ViewStatus.ScrollToUp -> mBinding.rvHome.scrollToPosition(0)
                is ViewStatus.GetDataSuccess -> mBinding.rvHome.visibility = View.VISIBLE
                is ViewStatus.ShowDialog -> {
                    activity?.messageDialog(viewStatus.msg, viewStatus.title)?.show()
                }
                is ViewStatus.GetDataFail -> Log.d(TAG, "not implement.")
                is ViewStatus.Loading -> Log.d(TAG, "not implement.")
                is ViewStatus.ShowToast -> Log.d(TAG, "not implement.")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        mHomeViewModel.unsubscribe()
        mBinding.rvHome.clearOnScrollListeners()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_search) {
            Log.d(TAG, "onOptionsItemSelected menu_search.")
            pushFragment(SearchFragment.newInstance())
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDomainClick(domain: String) {
        Log.d(TAG, "onDomainClick domain:$domain")
        mHomeEpoxyController.clearNewsData()
        mHomeViewModel.resetDataPage()
        mHomeViewModel.getEverythingByDomain(domain)
    }

    override fun onArticleClick(articlesBean: ArticlesBean) {
        Log.d(TAG, "onArticleClick articlesBean:$articlesBean")
        pushWebViewFragment(articlesBean)
    }
}