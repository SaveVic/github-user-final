package com.example.githubuserfinal.ui.main.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserfinal.R
import com.example.githubuserfinal.data.api.ApiHelper
import com.example.githubuserfinal.data.model.QueryUser
import com.example.githubuserfinal.databinding.ActivityMainBinding
import com.example.githubuserfinal.ui.base.MainViewModelFactory
import com.example.githubuserfinal.ui.main.adapter.UserListAdapter
import com.example.githubuserfinal.ui.main.viewmodel.MainViewModel
import com.example.githubuserfinal.utils.ListLoadStatus

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: UserListAdapter
    private var emptyMsg = ""
    private var errorMsg = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        setupViewModel()
        setupObserver()
    }

    private fun setupUI() {
        emptyMsg = resources.getString(R.string.status_empty)
        errorMsg = resources.getString(R.string.status_error)
        binding.mainRv.layoutManager = LinearLayoutManager(this)
        adapter = UserListAdapter(arrayListOf())
        binding.mainRv.adapter = adapter
        adapter.setOnItemClickCallback(object : UserListAdapter.OnItemClickCallback {
            override fun onItemClicked(user: QueryUser) {
                onNavigateUser(user)
            }
        })
        binding.mainSetting.setOnClickListener { onSettingClick() }
        binding.mainFavorite.setOnClickListener { onNavigateFavorite() }
        binding.mainSearch.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                onSearch(query)
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun setupObserver() {
        mainViewModel.getListUsers().observe(this, {
            when (it.status) {
                ListLoadStatus.FILLED -> {
                    binding.mainLoading.visibility = View.GONE
                    binding.mainStatus.visibility = View.GONE
                    it.data?.let { users -> renderList(users) }
                    binding.mainRv.visibility = View.VISIBLE
                }
                ListLoadStatus.EMPTY -> {
                    binding.mainLoading.visibility = View.GONE
                    binding.mainRv.visibility = View.GONE
                    binding.mainStatus.text = emptyMsg
                    binding.mainStatus.visibility = View.VISIBLE
                }
                ListLoadStatus.ERROR -> {
                    binding.mainLoading.visibility = View.GONE
                    binding.mainRv.visibility = View.GONE
                    var display = errorMsg
                    it.message?.let { msg ->
                        display += "\n$msg"
                    }
                    binding.mainStatus.text = display
                    binding.mainStatus.visibility = View.VISIBLE
                }
                ListLoadStatus.LOADING -> {
                    binding.mainLoading.visibility = View.VISIBLE
                    binding.mainRv.visibility = View.GONE
                    binding.mainStatus.visibility = View.GONE
                }
            }
        })
    }

    private fun renderList(users: List<QueryUser>) {
        adapter.replaceList(users)
        adapter.notifyDataSetChanged()
    }

    private fun setupViewModel() {
//        val token = resources.getString(R.string.github_token)
        mainViewModel = ViewModelProvider(
            this,
            MainViewModelFactory(ApiHelper())
        )[MainViewModel::class.java]
    }

    private fun onSearch(query: String?) {
        if (query == null || query.isEmpty()) return
        mainViewModel.fetchQuery(query)
    }

    private fun onSettingClick() {
        val intent = Intent(this@MainActivity, SettingActivity::class.java)
        startActivity(intent)
    }

    private fun onNavigateFavorite() {
        val intent = Intent(this@MainActivity, FavoriteActivity::class.java)
        startActivity(intent)
    }

    private fun onNavigateUser(user: QueryUser) {
        val detailIntent = Intent(this@MainActivity, DetailActivity::class.java)
        detailIntent.putExtra(DetailActivity.PARAM_USER, user)
        startActivity(detailIntent)
    }
}