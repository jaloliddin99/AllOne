package com.tesseract.AllOneClient.fragments.login.signIn

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(private val repository: NetworkRepository):ViewModel() {


     val text=MutableLiveData<String>()

     fun saveDetails(phone:String) = viewModelScope.launch {
        repository.saveDetails(phone).let {
            Log.i("hello ", "hello hello1")
            if (it.isSuccessful){
                Log.i("hello ", "hello hello2")
                Log.i("hello ", "hello hello234"+it.body()?.content+it.body()?.success)
                if (it.body()?.success == true){
                    text.postValue("Ok")
                }else{
                    text.postValue("No")
                }
            }else{
                text.postValue(it.message())
            }
        }
    }

    fun isNotEmpty(text:String):Boolean{
        if (text.isNullOrEmpty()){
            return false
        }
            return true
    }



}