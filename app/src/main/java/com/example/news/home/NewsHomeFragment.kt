package com.example.news.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news.R
import com.example.news.databinding.FragmentNewsHomeBinding
import com.example.news.home.adapter.HomeEpoxyController
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewsHomeFragment : Fragment() {

    companion object {
        const val TAG = "NewsHomeFragment"
        fun newInstance() = NewsHomeFragment()
    }

    private val mHomeViewModel: HomeViewModel by viewModel()
    private lateinit var mBinding: FragmentNewsHomeBinding

    private val mHomeEpoxyController by lazy { HomeEpoxyController() }

    override fun onCreateView(
        inflater: LayoutInflater,
        viewGroup: ViewGroup?,
        savedInstanceState: Bundle?
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
            this.layoutManager = LinearLayoutManager(activity)
        }

        mHomeViewModel.newsEverythingLiveData.observe(viewLifecycleOwner) {
            Log.d(TAG, "data = $it")
            it ?: return@observe
            mHomeEpoxyController.mNewsData = it
        }

        mHomeViewModel.subscribe()
    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        mHomeViewModel.unsubscribe()
//    }
}