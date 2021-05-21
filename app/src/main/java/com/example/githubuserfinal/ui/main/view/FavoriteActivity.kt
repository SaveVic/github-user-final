package com.example.githubuserfinal.ui.main.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserfinal.R
import com.example.githubuserfinal.data.db.StoredUserHelper
import com.example.githubuserfinal.data.model.QueryUser
import com.example.githubuserfinal.data.model.StoredQueryUser
import com.example.githubuserfinal.databinding.ActivityFavoriteBinding
import com.example.githubuserfinal.ui.base.FavoriteViewModelFactory
import com.example.githubuserfinal.ui.main.adapter.UserListAdapter
import com.example.githubuserfinal.ui.main.viewmodel.FavoriteViewModel

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var viewModel: FavoriteViewModel
    private lateinit var adapter: UserListAdapter
    private var users = arrayListOf<QueryUser>()
    private var emptyMsg = ""

    companion object{
        const val EXTRA_LIKED = "extra-liked"
        const val EXTRA_ID = "extra-id"
        const val RESULT_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        setupViewModel()
        setupObserver()
    }

    private fun setupObserver() {
        viewModel.getAll(this).observe(this, {
            when(it.size){
                0 -> {
                    binding.favoriteStatus.visibility = View.VISIBLE
                    renderList(it)
                    binding.favoriteRv.visibility = View.GONE
                }
                else -> {
                    binding.favoriteStatus.visibility = View.GONE
                    renderList(it)
                    binding.favoriteRv.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun renderList(data: List<StoredQueryUser>) {
        val storedUser = data.map {
            QueryUser(it.username, it.id, it.avatar_url)
        }
        users = ArrayList(storedUser)
        adapter.replaceList(storedUser)
        adapter.notifyDataSetChanged()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            FavoriteViewModelFactory()
        )[FavoriteViewModel::class.java]
    }

    private fun setupUI() {
        emptyMsg = resources.getString(R.string.status_empty)
        binding.favoriteRv.layoutManager = LinearLayoutManager(this)
        adapter = UserListAdapter(users)
        binding.favoriteRv.adapter = adapter
        adapter.setOnItemClickCallback(object : UserListAdapter.OnItemClickCallback {
            override fun onItemClicked(user: QueryUser) {
                onNavigateUser(user)
            }
        })
        binding.favoriteBack.setOnClickListener {
            finish()
        }
        binding.favoriteSetting.setOnClickListener { onSettingClick() }
        binding.favoriteStatus.visibility = View.VISIBLE
        binding.favoriteRv.visibility = View.GONE
    }

    private fun onNavigateUser(user: QueryUser) {
        val detailIntent = Intent(this@FavoriteActivity, DetailActivity::class.java)
        detailIntent.putExtra(DetailActivity.PARAM_USER, user)
        startActivityForResult(detailIntent, RESULT_CODE)
    }

    private fun onSettingClick() {
        val intent = Intent(this@FavoriteActivity, SettingActivity::class.java)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RESULT_CODE){
            if(resultCode == RESULT_OK){
//                Log.e("RESULT", "YES")
                val liked = data?.getBooleanExtra(EXTRA_LIKED, true) ?: true
                if(!liked){
                    data?.let {
                        val id = it.getIntExtra(EXTRA_ID, -1)
                        users.removeIf { value -> value.id == id }
                        adapter.replaceList(users)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }
}