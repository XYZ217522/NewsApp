package com.example.news.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news.R
import com.example.news.base.BaseFragment
import com.example.news.databinding.FragmentNewsHomeBinding
import com.example.news.home.adapter.HomeEpoxyCallback
import com.example.news.home.adapter.HomeEpoxyController
import com.example.news.model.ArticlesBean
import com.example.news.util.EndlessScrollListener
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
        mBinding.lifecycleOwner = this
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(mBinding.homeActionbar) {
            this.title = ""
//            this?.setNavigationIcon(R.drawable.back_arrow)
            (activity as AppCompatActivity).setSupportActionBar(this)
        }

        mBinding.rvHome.apply {
            this.adapter = mHomeEpoxyController.adapter
            val layoutManager = LinearLayoutManager(activity)
            this.layoutManager = layoutManager
            this.addOnScrollListener(object : EndlessScrollListener(layoutManager) {
                override fun onLoadMore(currentPage: Int) {
                    mHomeViewModel.loadMore()
                }
            })
        }

        mBinding.tvTitle.setOnClickListener {
            mHomeEpoxyController.changeMode().let { mBinding.rvHome.setAnimation(it) }
        }

        dataBinding()
    }

    private fun dataBinding() {

        mHomeViewModel.titleLiveData.observe(viewLifecycleOwner) {
            Log.d(TAG, "title = $it")
            it ?: return@observe
            mBinding.tvTitle.text = it
            mHomeEpoxyController.mSelectDomain = it
        }

        mHomeViewModel.newsEverythingLiveData.observe(viewLifecycleOwner) {
            Log.d(TAG, "data = $it")
            it ?: return@observe
            mHomeEpoxyController.setNewsData(it)
            if (it.currentPage == 1) mBinding.rvHome.scrollToPosition(0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        mHomeViewModel.unsubscribe()
        mBinding.rvHome.clearOnScrollListeners()
    }

    override fun onDomainClick(domain: String) {
        Log.d(TAG, "onDomainClick domain:$domain")
        mHomeEpoxyController.clearNewsData()
        mHomeViewModel.resetDataPage()
        mHomeViewModel.getEverythingByDomain(domain)
    }

    override fun onArticleClick(articlesBean: ArticlesBean) {
        Log.d(TAG, "onArticleClick articlesBean:$articlesBean")
    }
}