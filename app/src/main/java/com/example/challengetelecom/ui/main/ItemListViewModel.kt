package com.example.challengetelecom.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.challengetelecom.data.model.Character
import com.example.challengetelecom.data.network.Resource
import com.example.challengetelecom.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemListViewModel @Inject constructor(
    application: Application,
    private val repository: MainRepository,
) : AndroidViewModel(application) {

    private var page = 1
    private val _charactersLiveData = MutableLiveData<Resource<List<Character>>>()
    fun getCharactersLiveData(): LiveData<Resource<List<Character>>> = _charactersLiveData

    init {
        getCharacters(true)
    }

    fun getCharacters(isReset: Boolean = false) {
        if (isReset)
            page = 1

        _charactersLiveData.postValue(Resource.Loading())
        viewModelScope.launch {
            repository.getCharacters(page).collect {
                if (it is Resource.Success)
                    page++

                _charactersLiveData.postValue(it)
            }
        }
    }
}