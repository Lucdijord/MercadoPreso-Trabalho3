package com.example.myapplication.ui.enderecos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.repository.EnderecoRepository

class EnderecoViewModelFactory(
    private val repository: EnderecoRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EnderecoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EnderecoViewModel(repository) as T
        }
        throw IllegalArgumentException("ViewModel desconhecido")
    }
}
