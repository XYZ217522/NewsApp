package com.example.news

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.news.base.BaseFragment
import com.example.news.databinding.ActivityNewsBinding
import com.example.news.home.NewsHomeFragment


class NewsActivity : AppCompatActivity() {

//    private val preferences: Preferences by inject()
//    private val exampleFragment: WeatherFragment by inject()

    companion object {
        const val TAG = "NewsActivity"
    }

    private lateinit var activityMainBinding: ActivityNewsBinding

    // root fragments
    private val newsHomeFragment: BaseFragment by lazy { NewsHomeFragment.newInstance() }
    private val popularityFragment: Fragment by lazy { Fragment() } //todo PopularityFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_news)

        activityMainBinding.navigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.tab_everything -> pushFragment(newsHomeFragment, true)
                R.id.tab_popularity -> pushFragment(popularityFragment, true)
            }
            true
        }
        activityMainBinding.navigationView.selectedItemId = R.id.tab_everything
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