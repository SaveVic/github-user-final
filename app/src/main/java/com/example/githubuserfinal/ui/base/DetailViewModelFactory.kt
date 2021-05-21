package com.example.githubuserfinal.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubuserfinal.data.api.ApiHelper
import com.example.githubuserfinal.data.db.StoredUserHelper
import com.example.githubuserfinal.data.model.StoredQueryUser
import com.example.githubuserfinal.data.repository.DetailRepository
import com.example.githubuserfinal.ui.main.viewmodel.DetailViewModel

class DetailViewModelFactory(private val apiHelper: ApiHelper, private val user: StoredQueryUser) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(DetailRepository(apiHelper), user) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}