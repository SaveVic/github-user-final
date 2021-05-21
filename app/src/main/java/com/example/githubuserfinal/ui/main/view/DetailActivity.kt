package com.example.githubuserfinal.ui.main.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.githubuserfinal.R
import com.example.githubuserfinal.data.api.ApiHelper
import com.example.githubuserfinal.data.db.StoredUserHelper
import com.example.githubuserfinal.data.model.QueryUser
import com.example.githubuserfinal.data.model.StoredQueryUser
import com.example.githubuserfinal.data.model.User
import com.example.githubuserfinal.databinding.ActivityDetailBinding
import com.example.githubuserfinal.ui.base.DetailViewModelFactory
import com.example.githubuserfinal.ui.main.adapter.DetailPagerAdapter
import com.example.githubuserfinal.ui.main.viewmodel.DetailViewModel
import com.example.githubuserfinal.utils.NetworkStatus
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var storedUser: StoredQueryUser
//    private lateinit var username: String
    private var errorMsg = ""
    private var noName = ""
    private var noEmail = ""
    private var liked = false

    companion object {
        private val TAB_TITLES = intArrayOf(
            R.string.detail_tab_1,
            R.string.detail_tab_2,
        )
        const val PARAM_USER = "user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = intent.getSerializableExtra(PARAM_USER) as QueryUser
//        username = user.login
        storedUser = StoredQueryUser(user.id, user.login, user.avatar_url)

        setupUI()
        setupViewModel()
        setupObserver()
    }

    private fun setupUI() {
        noName = resources.getString(R.string.detail_no_name)
        noEmail = resources.getString(R.string.detail_no_email)
        errorMsg = resources.getString(R.string.status_error)
        val pagerAdapter = DetailPagerAdapter(this)
        binding.detailPager.adapter = pagerAdapter
        TabLayoutMediator(binding.detailTabs, binding.detailPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        binding.detailBack.setOnClickListener { finish() }
        binding.detailSetting.setOnClickListener { onSettingClick() }
        renderLike()
    }

    private fun onSettingClick() {
        val intent = Intent(this@DetailActivity, SettingActivity::class.java)
        startActivity(intent)
    }

    private fun setupViewModel() {
        detailViewModel = ViewModelProvider(
            this@DetailActivity,
            DetailViewModelFactory(ApiHelper(), storedUser)
        )[DetailViewModel::class.java]
        binding.detailLike.setOnClickListener {
            liked = !liked
            detailViewModel.setStored(this, storedUser, liked)
        }
    }

    private fun setupObserver() {
        detailViewModel.initStored(this)
        detailViewModel.isStored().observe(this, {
            liked = it
            renderLike()
        })
        detailViewModel.getInfoUser().observe(this, {
            when (it.status) {
                NetworkStatus.SUCCESS -> {
                    binding.detailLoading.visibility = View.GONE
                    binding.detailStatus.visibility = View.GONE
                    it.data?.let { info -> renderInfoUser(info) }
                    binding.detailInfo.visibility = View.VISIBLE
                }
                NetworkStatus.LOADING -> {
                    binding.detailLoading.visibility = View.VISIBLE
                    binding.detailStatus.visibility = View.GONE
                    binding.detailInfo.visibility = View.INVISIBLE
                }
                NetworkStatus.ERROR -> {
                    binding.detailLoading.visibility = View.GONE
                    binding.detailInfo.visibility = View.INVISIBLE
                    var display = errorMsg
                    it.message?.let { msg ->
                        display += "\n$msg"
                    }
                    binding.detailStatus.text = display
                    binding.detailStatus.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun renderInfoUser(info: User) {
        Glide.with(this)
            .load(info.avatar_url)
            .into(binding.detailImg)
        val name = info.name ?: ""
        binding.detailName.text = if (name.isNotEmpty()) name else "-$noName-"
        val email = info.email ?: ""
        binding.detailEmail.text = if (email.isNotEmpty()) email else "-$noEmail-"
        binding.detailFollowers.text = info.followers.toString()
        binding.detailFollowings.text = info.following.toString()
    }

    private fun renderLike(){
        binding.detailLike.setImageResource(if (liked) R.drawable.like_true else R.drawable.like_false)
    }

    override fun finish() {
        val intent = Intent()
        intent.putExtra(FavoriteActivity.EXTRA_LIKED, liked)
        intent.putExtra(FavoriteActivity.EXTRA_ID, storedUser.id)
        setResult(RESULT_OK, intent)
        super.finish()
    }
}