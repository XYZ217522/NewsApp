package com.example.news.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news.R
import com.example.news.base.BaseFragment
import com.example.news.databinding.FragmentSearchBinding
import com.example.news.model.ArticlesBean
import com.example.news.search.adapter.SearchEpoxyCallback
import com.example.news.search.adapter.SearchEpoxyController
import com.example.news.util.ViewStatus
import com.example.news.util.hideKeyboard
import com.example.news.util.messageDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : BaseFragment(), SearchEpoxyCallback {

    companion object {
        const val TAG = "SearchFragment"
        fun newInstance(): SearchFragment = SearchFragment()
    }

    private val mSearchViewModel: SearchViewModel by viewModel()
    private lateinit var mBinding: FragmentSearchBinding
    private val mSearchEpoxyController by lazy { SearchEpoxyController(this) }

    override fun getSupportActionBar(): Toolbar = mBinding.searchActionbar

    override fun onCreateView(
        inflater: LayoutInflater,
        viewGroup: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreateView(inflater, viewGroup, savedInstanceState)
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_search,
            viewGroup,
            false
        )
        mBinding.lifecycleOwner = viewLifecycleOwner
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.rvSearch.apply {
            this.adapter = mSearchEpoxyController.adapter
            this.layoutManager = LinearLayoutManager(activity)
        }

        mBinding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(editable: Editable?) {
                if (editable.toString().isEmpty()) {
                    mSearchEpoxyController.changeMode(true)
                }
            }
        })

        mBinding.edtSearch.setOnEditorActionListener { _, actionId, event ->
            Log.d(TAG, "onEditorAction.")
            if (actionId == EditorInfo.IME_ACTION_SEARCH || event.keyCode == KeyEvent.KEYCODE_ENTER) {
                val searchText = mBinding.edtSearch.editableText?.toString() ?: ""
                search(searchText)
                return@setOnEditorActionListener true
            }
            false
        }

        mBinding.ivSearchClear.setOnClickListener { mBinding.edtSearch.setText("") }

        binding()
    }

    private fun binding() {

        mSearchViewModel.historyLiveData.observe(viewLifecycleOwner) {
            it ?: return@observe
            mSearchEpoxyController.setHistoryData(it)
        }

        mSearchViewModel.searchResultLiveData.observe(viewLifecycleOwner) {
            val newsData = it?.getContentIfNotHandled() ?: return@observe
            mSearchEpoxyController.setNewsData(newsData)
        }

        mSearchViewModel.viewStatusLiveData.observe(viewLifecycleOwner) {
            Log.d(TAG, "view status = $it")
            val viewStatus = it.getContentIfNotHandled() ?: return@observe
            when (viewStatus) {
                is ViewStatus.ShowDialog -> {
                    activity?.messageDialog(viewStatus.msg, viewStatus.title)?.show()
                }
                is ViewStatus.GetDataSuccess -> activity.hideKeyboard()
                else -> Log.d(TAG, "not implement.")
            }
        }

    }

    private fun search(searchText: String) {
        mSearchEpoxyController.clearNewsData()
        mSearchViewModel.search(searchText)
    }

    override fun onHistoryTextClick(historyText: String) {
        Log.d(TAG, "onHistoryTextClick historyText:$historyText")
        mBinding.edtSearch.setText(historyText)
        search(historyText)
    }

    override fun onClearHistoryClick() {
        Log.d(TAG, "onClearHistoryClick.")
        mSearchViewModel.clearHistory()
    }

    override fun onArticleClick(articlesBean: ArticlesBean) {
        Log.d(TAG, "onArticleClick articlesBean:$articlesBean")
        pushWebViewFragment(articlesBean)
    }
}