package com.tesseract.AllOneClient.DI

import com.tesseract.AllOneClient.API.APIInterface
import com.tesseract.AllOneClient.API.APIRouting
import com.tesseract.AllOneClient.constants.Links
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideBaseUrl() = Links.BASE_URL

    @Provides
    @Singleton
    fun provideRetrofitInstance(BASE_URL: String): APIInterface =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIInterface::class.java)


    @Provides
    @Singleton
    fun provideRetrofitForRouting(): APIRouting =
        Retrofit.Builder()
            .baseUrl("http://188.120.232.38:5002/ors/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIRouting::class.java)


}