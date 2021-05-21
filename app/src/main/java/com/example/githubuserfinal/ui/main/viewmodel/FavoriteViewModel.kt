package com.example.githubuserfinal.ui.main.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.githubuserfinal.data.repository.FavoriteRepository

class FavoriteViewModel(private val favoriteRepository: FavoriteRepository) : ViewModel(){
    fun getAll(context: Context) = favoriteRepository.getAllStored(context)
}