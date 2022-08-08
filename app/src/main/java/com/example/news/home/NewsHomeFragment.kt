package com.example.news.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news.R
import com.example.news.base.BaseFragment
import com.example.news.databinding.FragmentNewsHomeBinding
import com.example.news.home.adapter.HomeEpoxyCallback
import com.example.news.home.adapter.HomeEpoxyController
import com.example.news.model.ArticlesBean
import com.example.news.search.SearchFragment
import com.example.news.util.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewsHomeFragment : BaseFragment<FragmentNewsHomeBinding>(), HomeEpoxyCallback {

    companion object {
        const val TAG = "NewsHomeFragment"
        fun newInstance() = NewsHomeFragment()
    }

    private val mHomeViewModel: HomeViewModel by viewModel()

    private val mHomeEpoxyController by lazy { HomeEpoxyController(this) }

    override var navigationVisibility = View.VISIBLE

    override var isRootFragment = true

    override var optionsMenuId: Int? = R.menu.home_menu

    override fun getSupportActionBar(): Toolbar = mBinding.homeActionbar

    override fun initViewBinding(inflater: LayoutInflater, container: ViewGroup?, boolean: Boolean) =
        FragmentNewsHomeBinding.inflate(inflater, container, boolean)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e(TAG, "onViewCreated")
        initView()
        observeData()
        initAction()
    }

    override fun initView() {
        mBinding.rvHome.apply {
            this.adapter = mHomeEpoxyController.adapter
            val layoutManager = LinearLayoutManager(activity)
            this.layoutManager = layoutManager
            this.addOnScrollListener(object : EndlessScrollListener(layoutManager) {
                override fun onLoadMore(currentPage: Int) {
                    if (!mHomeEpoxyController.isSelectDomainMode) {
                        mHomeViewModel.onLoadMoreEveryThing()
                    }
                }
            })
        }

        mBinding.tvTitle.setOnClickListener {
            mHomeEpoxyController.changeDomainMode()
            val animation = mHomeEpoxyController.getSelectDomainAnimation()
            mBinding.rvHome.setAnimation(animation)
        }
    }

    // 應該要在 ViewModel 寫 LoadMore 是否觸發的邏輯，Epoxy只是被控制的 View
    // 用 ViewState 是因為如果有多隻 API ，用 Intent 很容易造成 UI 上的錯亂，畢竟每隻 API Call 的狀態都不一樣會互相干擾

    override fun observeData() {
        mHomeViewModel.titleLiveData.observe(viewLifecycleOwner) {
            Log.d(TAG, "titleLiveData = $it")
            val title = it ?: return@observe
            mBinding.tvTitle.text = title
            mHomeEpoxyController.mSelectDomain = title
        }

        mHomeViewModel.newsEverythingLiveData.observe(viewLifecycleOwner) {
            Log.d(TAG, "newsEverythingLiveData = ${it.print()}")
            val newsData = it?.getContentIfNotHandled() ?: return@observe
            mHomeEpoxyController.mAlArticles = newsData.articles ?: mutableListOf()
        }

        mHomeViewModel.viewStatusLiveData.observe(viewLifecycleOwner) {
            Log.d(TAG, "viewStatusLiveData = ${it.print()}")
            val viewStatus = it?.getContentIfNotHandled() ?: return@observe
            when (viewStatus) {
                is HomeViewState.ShowToast -> {
                    mHomeEpoxyController.clearNewsData()
                    showRecyclerView(false)
                }
                is HomeViewState.GetDataSuccess -> {
                    mHomeEpoxyController.mIsLoadMore = viewStatus.isShow
                    showRecyclerView(true)
                }
                is HomeViewState.GetDataFail -> {
                    showRecyclerView(true)
                    if (viewStatus.msg.isNotEmpty()) activity?.messageDialog(viewStatus.msg)?.show()
                }
                is HomeViewState.ShowDialog -> {
                    activity?.messageDialog(viewStatus.msg, viewStatus.title)?.show()
                }
                is HomeViewState.ScrollToUp -> mBinding.rvHome.scrollToPosition(0)
                is HomeViewState.GetLoadMoreDataFail -> showRecyclerView(true)
                else -> Log.d(TAG, "not implement.")
            }
        }
    }

    // start viewmodel process ex: getData, FetchAPI
    override fun initAction() {
        Log.e(TAG, "initAction")
        mHomeViewModel.onStartRequestEveryThing()
    }


    private fun showRecyclerView(show: Boolean) {
        mBinding.rvHome.apply { if (show) visible() else invisible() }
        mBinding.pbLoading.apply { if (!show) visible() else gone() }
    }

    override fun onDestroyView() {
        mBinding.rvHome.adapter = null
        mBinding.rvHome.clearOnScrollListeners()
        super.onDestroyView()
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
        mHomeEpoxyController.changeDomainMode()
        mHomeViewModel.onDomainClickRequestEveryThing(domain)
    }

    override fun onArticleClick(articlesBean: ArticlesBean) {
        Log.d(TAG, "onArticleClick articlesBean:$articlesBean")
        pushWebViewFragment(articlesBean)
    }
}