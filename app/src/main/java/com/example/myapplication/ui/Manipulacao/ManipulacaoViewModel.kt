package com.example.myapplication.ui.Manipulacao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ManipulacaoViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Adicione ou delete produtos"
    }
    val text: LiveData<String> = _text
}