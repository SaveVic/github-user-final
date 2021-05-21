package com.example.githubuserfinal.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubuserfinal.data.db.StoredUserHelper
import com.example.githubuserfinal.data.repository.FavoriteRepository
import com.example.githubuserfinal.ui.main.viewmodel.FavoriteViewModel

class FavoriteViewModelFactory : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(FavoriteRepository()) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}