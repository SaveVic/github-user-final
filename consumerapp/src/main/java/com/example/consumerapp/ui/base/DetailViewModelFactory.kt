package com.example.consumerapp.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.consumerapp.data.api.ApiHelper
import com.example.consumerapp.data.db.StoredUserHelper
import com.example.consumerapp.data.model.StoredQueryUser
import com.example.consumerapp.data.repository.DetailRepository
import com.example.consumerapp.ui.main.viewmodel.DetailViewModel

class DetailViewModelFactory(private val apiHelper: ApiHelper, private val user: StoredQueryUser) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(DetailRepository(apiHelper), user) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}