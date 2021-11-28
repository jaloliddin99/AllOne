package com.tesseract.AllOneClient.fragments.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.model.chat.ChatSocketModel
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel  @Inject constructor(private val networkRepository: NetworkRepository) :
    ViewModel() {
    val chatModel = MutableLiveData<ChatSocketModel>()

    val error = MutableLiveData<String>()

    fun getChatModel(token:Map<String, String>, orderId:Int) = viewModelScope.launch {
        try {
            networkRepository.chatModel(token, orderId).let {
                if (it.isSuccessful) {
                    chatModel.postValue(it.body())
                } else {
                    error.postValue("error routing ${it.code()} ${it.message()}")
                }
            }
        } catch (ex: java.lang.Exception) {
            error.postValue("${ex.message} ${ex.message}")
        }
    }
}