package com.example.news

import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.news.base.BaseFragment
import com.example.news.databinding.ActivityNewsBinding
import com.example.news.home.NewsHomeFragment
import com.example.news.popularity.PopularityFragment


class NewsActivity : AppCompatActivity() {

//    private val preferences: Preferences by inject()
//    private val exampleFragment: WeatherFragment by inject()

    companion object {
        const val TAG = "NewsActivity"
        const val BUNDLE_ITEM_ID = "BUNDLE_ITEM_ID"
    }

    private lateinit var activityMainBinding: ActivityNewsBinding

    // root fragments
    private val newsHomeFragment: BaseFragment by lazy { NewsHomeFragment.newInstance() }
    private val popularityFragment: BaseFragment by lazy { PopularityFragment.newInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_news)

        activityMainBinding.navigationView.apply {
            this.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.tab_everything -> pushFragment(newsHomeFragment, true)
                    R.id.tab_popularity -> pushFragment(popularityFragment, true)
                }
                true
            }
            this.selectedItemId = savedInstanceState?.getInt(BUNDLE_ITEM_ID) ?: R.id.tab_everything
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putInt(BUNDLE_ITEM_ID, activityMainBinding.navigationView.selectedItemId)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    fun pushFragment(fragment: Fragment, isRootFragment: Boolean = false) {
        val tag = fragment.javaClass.simpleName
        val transaction = supportFragmentManager
            .beginTransaction()
            .replace(R.id.root, fragment, tag)

        if (!isRootFragment) {
            transaction.addToBackStack(tag)
        }

        transaction.commit()
    }

    fun setBottomNavigationVisibility(visibility: Int) {
        activityMainBinding.navigationView.visibility = visibility
    }
}