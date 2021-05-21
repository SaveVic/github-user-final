package com.example.githubuserfinal.ui.main.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuserfinal.R
import com.example.githubuserfinal.data.model.QueryUser
import com.example.githubuserfinal.databinding.FragmentFollowingsBinding
import com.example.githubuserfinal.ui.main.adapter.UserListAdapter
import com.example.githubuserfinal.ui.main.viewmodel.DetailViewModel
import com.example.githubuserfinal.utils.ListLoadStatus

class FollowingsFragment : Fragment() {
    private var binding: FragmentFollowingsBinding? = null
    private var adapter: UserListAdapter? = null
    private var detailViewModel: DetailViewModel? = null
    private var emptyMsg = ""
    private var errorMsg = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailViewModel = activity?.let {
            ViewModelProvider(
                it,
                ViewModelProvider.NewInstanceFactory()
            )[DetailViewModel::class.java]
        }
        emptyMsg = resources.getString(R.string.status_empty)
        errorMsg = resources.getString(R.string.status_error)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowingsBinding.inflate(inflater, container, false)
        setupUI()
        setupObserver()
        return binding?.root
    }

    private fun setupUI() {
        binding?.followingsRv?.layoutManager =
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        adapter = UserListAdapter(arrayListOf(), false)
        binding?.followingsRv?.adapter = adapter
    }

    private fun renderList(users: List<QueryUser>) {
        adapter?.replaceList(users)
        adapter?.notifyDataSetChanged()
    }

    private fun setupObserver() {
        detailViewModel?.getFollowingsList()?.observe(this, {
            when (it.status) {
                ListLoadStatus.FILLED -> {
                    binding?.followingsLoading?.visibility = View.GONE
                    binding?.followingsStatus?.visibility = View.GONE
                    it.data?.let { users -> renderList(users) }
                    binding?.followingsRv?.visibility = View.VISIBLE
                }
                ListLoadStatus.EMPTY -> {
                    binding?.followingsLoading?.visibility = View.GONE
                    binding?.followingsRv?.visibility = View.GONE
                    binding?.followingsStatus?.text = emptyMsg
                    binding?.followingsStatus?.visibility = View.VISIBLE
                }
                ListLoadStatus.ERROR -> {
                    binding?.followingsLoading?.visibility = View.GONE
                    binding?.followingsRv?.visibility = View.GONE
                    var display = errorMsg
                    it.message?.let { msg ->
                        display += "\n$msg"
                    }
                    binding?.followingsStatus?.text = display
                    binding?.followingsStatus?.visibility = View.VISIBLE
                }
                ListLoadStatus.LOADING -> {
                    binding?.followingsLoading?.visibility = View.VISIBLE
                    binding?.followingsRv?.visibility = View.GONE
                    binding?.followingsStatus?.visibility = View.GONE
                }
            }
        })
    }
}