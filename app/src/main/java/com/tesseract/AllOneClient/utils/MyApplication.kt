package com.tesseract.AllOneClient.utils

import androidx.multidex.MultiDexApplication
import com.tesseract.AllOneClient.constants.Links
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication: MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        Links.context = this
    }
}