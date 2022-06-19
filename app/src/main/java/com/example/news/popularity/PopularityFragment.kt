package com.example.news.popularity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news.R
import com.example.news.base.BaseFragment
import com.example.news.databinding.FragmentPopularityBinding
import com.example.news.model.ArticlesBean
import com.example.news.popularity.adapter.PopularityEpoxyCallback
import com.example.news.popularity.adapter.PopularityEpoxyController
import com.example.news.util.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PopularityFragment : BaseFragment(), PopularityEpoxyCallback {

    companion object {
        private const val TAG = "PopularityFragment"
        fun newInstance() = PopularityFragment()
    }

    private val mPopularityViewModel: PopularityViewModel by viewModel()

    private lateinit var mBinding: FragmentPopularityBinding

    private val mPopularityEpoxyController by lazy { PopularityEpoxyController(this) }

    override var navigationVisibility = View.VISIBLE

    override var isRootFragment = true

    override fun getSupportActionBar(): Toolbar = mBinding.popularityActionbar

    override fun onCreateView(
        inflater: LayoutInflater,
        viewGroup: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreateView(inflater, viewGroup, savedInstanceState)
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_popularity,
            viewGroup,
            false
        )
        mBinding.lifecycleOwner = viewLifecycleOwner
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.rvPopularity.apply {
            this.adapter = mPopularityEpoxyController.adapter
            val layoutManager = LinearLayoutManager(activity)
            this.layoutManager = layoutManager
            this.addOnScrollListener(object : EndlessScrollListener(layoutManager) {
                override fun onLoadMore(currentPage: Int) {
                    mPopularityViewModel.loadMore()
                }
            })
            mPopularityEpoxyController.requestModelBuild()//t
        }

        mBinding.tvTitle.setOnClickListener {
            mPopularityEpoxyController.changeMode().let {
                mBinding.rvPopularity.setAnimation(it)
                run { if (R.anim.slide_down == it) View.VISIBLE else View.GONE }
                    .apply { mBinding.btnDone.setBottomViewVisibilityAnimation(this) }
            }
        }

        mBinding.btnDone.apply {
            visibility = if (mPopularityEpoxyController.isSelectMode) View.VISIBLE else View.GONE
            setOnClickListener {
                this.setBottomViewVisibilityAnimation(View.GONE)
                mPopularityEpoxyController.let {
                    it.clearNewsData()
                    mPopularityViewModel.fetchDataByApi(it.mSelectCountry, it.mSelectCategory)
                }
            }
        }

        binding()
    }

    private fun binding() {

        mPopularityViewModel.titleLiveData.observe(viewLifecycleOwner) {
            Log.d(TAG, "title = $it")
            it ?: return@observe
            mBinding.tvTitle.text = "${it.second} in country=${it.first}"
            mPopularityEpoxyController.setCountryAndCategory(it)
        }

        mPopularityViewModel.topHeadLineLiveData.observe(viewLifecycleOwner) {
            Log.d(TAG, "topHeadLineLiveData = $it")
            val topHeadLineData = it?.getContentIfNotHandled() ?: return@observe
            mPopularityEpoxyController.setTopHeadLineData(topHeadLineData)
        }

        mPopularityViewModel.popularityLiveData.observe(viewLifecycleOwner) {
            Log.d(TAG, "popularityLiveData = $it")
            val newsData = it?.getContentIfNotHandled() ?: return@observe
            mPopularityEpoxyController.setPopularityData(newsData)
        }

        mPopularityViewModel.viewStatusLiveData.observe(viewLifecycleOwner) {
            Log.d(TAG, "view status = $it")
            val viewStatus = it?.getContentIfNotHandled() ?: return@observe
            when (viewStatus) {
                is ViewStatus.ScrollToUp -> mBinding.rvPopularity.scrollToPosition(0)
                is ViewStatus.ShowDialog -> {
                    activity?.messageDialog(viewStatus.msg, viewStatus.title)?.show()
                }
                else -> Log.d(TAG, "not implement.")
            }
        }
    }

    override fun onCountryClick(country: String) {
        Log.d(TAG, "onCountryClick=$country")
        mPopularityEpoxyController.mSelectCountry = country
    }

    override fun onCategoryClick(category: String) {
        Log.d(TAG, "onCategoryClick=$category")
        mPopularityEpoxyController.mSelectCategory = category
    }

    override fun onArticleClick(articlesBean: ArticlesBean) {
        Log.d(TAG, "onArticleClick articlesBean:$articlesBean")
        pushWebViewFragment(articlesBean)
    }
}