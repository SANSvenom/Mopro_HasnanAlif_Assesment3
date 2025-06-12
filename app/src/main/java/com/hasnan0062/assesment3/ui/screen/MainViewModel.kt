package com.hasnan0062.assesment3.ui.screen


import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasnan0062.assesment3.model.Kucing
import com.hasnan0062.assesment3.network.ApiStatus
import com.hasnan0062.assesment3.network.KucingApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    var data = mutableStateOf(emptyList<Kucing>())
        private set

    var status = MutableStateFlow(ApiStatus.LOADING)
        private set

    init {
        retrieveData()
    }

    fun retrieveData() {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            try {
                data.value = KucingApi.service.getKucing()
                    .take(4)
                    .mapIndexed { index, kucing ->
                        if (index == 0) {
                            // Bikin URL error di item pertama
                            kucing.copy(url = "https://example.com/image-not-found.jpg")
                        } else {
                            kucing
                        }
                    }
                Log.d("MainViewModel", "Data retrieved successfully: ${data.value}")
                status.value = ApiStatus.SUCCESS
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
                status.value = ApiStatus.FAILED
            }
        }
    }
}