package com.example.news.base

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.webkit.*
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.databinding.DataBindingUtil
import com.example.news.R
import com.example.news.databinding.FragmentWebviewBinding

class NewsWebViewFragment : BaseFragment() {

    companion object {
        private const val TAG = "NewsWebViewFragment"
        private const val ARGS_URL = "ARGS_URL"
        private const val ARGS_TITLE = "ARGS_TITLE"

        fun newInstance(url: String, title: String?): NewsWebViewFragment {
            val fragment = NewsWebViewFragment()
            fragment.arguments = Bundle().apply {
                this.putString(ARGS_URL, url)
                this.putString(ARGS_TITLE, title)
            }
            return fragment
        }
    }

    private lateinit var mBinding: FragmentWebviewBinding

    override fun getSupportActionBar(): Toolbar = mBinding.webActionbar

    override fun onCreateView(
        inflater: LayoutInflater,
        viewGroup: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreateView(inflater, viewGroup, savedInstanceState)
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_webview,
            viewGroup,
            false
        )
        mBinding.lifecycleOwner = this
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onViewCreated.")
        initView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initView() {
        Log.i(TAG, "initView.")

        mBinding.tvTitle.text = arguments?.getString(ARGS_TITLE, "Title") ?: "Title"

        // 設定進度條的顏色
        // https://stackoverflow.com/questions/56716093/setcolorfilter-is-deprecated-on-api29/56717316
        mBinding.pbLoading.progressDrawable?.colorFilter = BlendModeColorFilterCompat
            .createBlendModeColorFilterCompat(
                Color.rgb(50, 150, 251), BlendModeCompat.SRC_IN
            )

        mBinding.webView.settings.apply {
            this.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            // 設定支援縮放手勢
            this.builtInZoomControls = true
            this.displayZoomControls = false
            this.javaScriptEnabled = true
            this.domStorageEnabled = true
        }

        // 設定進度條
        mBinding.webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                if (newProgress == 100) {
                    mBinding.pbLoading.visibility = ProgressBar.GONE
                } else {
                    mBinding.pbLoading.progress = newProgress
                }
            }
        }

        arguments?.getString(ARGS_URL, "")?.let {
            mBinding.webView.loadUrl(it)
        }
    }
}