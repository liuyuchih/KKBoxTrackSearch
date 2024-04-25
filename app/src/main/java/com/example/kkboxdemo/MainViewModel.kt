package com.example.kkboxdemo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.kkboxdemo.remote.Constants.Companion.LOGCAT_TAG
import com.example.kkboxdemo.remote.TokenResponse
import com.example.myapplication.remote.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel (private val repository: KKboxRepository): ViewModel() {

    class Factory(private val repository: KKboxRepository): ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(repository) as T
        }
    }

    val searchResults = MutableLiveData<List<Track>>()
    private var nextPageUrl: String? = null
    private var authResponse: TokenResponse? = null
    private val trackList = mutableListOf<Track>()
    val isLoading = MutableLiveData<Boolean>()

    fun searchTracks(query: String) {
        if(query.isBlank()) {
            return
        }

        viewModelScope.launch {
            isLoading.value = true
            try {
                if(authResponse == null) {
                    authResponse = repository.authApi.getToken()
                    Log.d(LOGCAT_TAG, authResponse.toString())
                }
                val header = authResponse?.getHeader() ?: ""
                val response = repository.api.searchTracks(header, query)
                Log.d(LOGCAT_TAG, "summary: " + response.summary.toString())
                Log.d(LOGCAT_TAG, "data size: " + response.tracks.data.size)
                Log.d(LOGCAT_TAG, "paging: " + response.paging)
                nextPageUrl = response.paging.next
                trackList.addAll(response.tracks.data)
                searchResults.postValue(trackList)
            } catch (e: Exception) {
                Log.e(LOGCAT_TAG, e.toString())
            }
            isLoading.value = false
        }
    }

    private var loadMoreJob: Job? = null

    fun loadMoreData() {
        Log.d(LOGCAT_TAG, "loadMoreData: " + nextPageUrl)
        if(loadMoreJob?.isActive == true) {
            return
        }
        loadMoreJob = viewModelScope.launch {
            isLoading.value = true
            if (nextPageUrl != null) {
                try {
                    if(authResponse == null) {
                        authResponse = repository.authApi.getToken()
                        Log.d(LOGCAT_TAG, authResponse.toString())
                    }
                    val header = authResponse?.getHeader() ?: ""
                    val response = repository.api.fetchTracksByUrl(header, nextPageUrl!!)
                    Log.d(LOGCAT_TAG, "loadmore result summary: " + response.summary.toString())
                    Log.d(LOGCAT_TAG, "loadmore result data size: " + response.tracks.data.size)
                    Log.d(LOGCAT_TAG, "loadmore result paging: " + response.paging)
                    nextPageUrl = response.paging.next
                    trackList.addAll(response.tracks.data)
                    searchResults.postValue(trackList)
                } catch (e: Exception) {
                    Log.e(LOGCAT_TAG, e.toString())
                }
            }
            isLoading.value = false
        }
    }

    fun clearResult() {
        viewModelScope.launch {
            trackList.clear()
            nextPageUrl = null
            searchResults.postValue(trackList)
        }
    }
}