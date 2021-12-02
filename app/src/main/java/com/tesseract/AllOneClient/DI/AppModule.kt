package com.tesseract.AllOneClient.DI

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.tesseract.AllOneClient.API.APIInterface
import com.tesseract.AllOneClient.API.APIRouting
import com.tesseract.AllOneClient.BuildConfig
import com.tesseract.AllOneClient.constants.Links
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideBaseUrl() = Links.BASE_URL

    @Provides
    @Singleton
    fun provideRetrofitInstance(BASE_URL: String): APIInterface{

        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val builder = OkHttpClient.Builder()

        builder.connectTimeout(20, TimeUnit.SECONDS)
        builder.addInterceptor(interceptor)

        val chuckerInterceptor = ChuckerInterceptor.Builder(Links.context)
            // The previously created Collector
            //.collector(chuckerCollector)
            // The max body content length in bytes, after this responses will be truncated.
            .maxContentLength(500_000L)
            // List of headers to replace with ** in the Chucker UI
            .redactHeaders("Auth-Token", "Bearer")
            // Read the whole response body even when the client does not consume the response completely.
            // This is useful in case of parsing errors or when the response body
            // is closed before being read like in Retrofit with Void and Unit types.
            .alwaysReadResponseBody(true)
            // Use decoder when processing request and response bodies. When multiple decoders are installed they
            // are applied in an order they were added.
            //.addBodyDecoder(decoder)
            // Controls Android shortcut creation. Available in SNAPSHOTS versions only at the moment
            //.createShortcut(true)
            .build()

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(interceptor)
            builder.addInterceptor(chuckerInterceptor)
        }
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(builder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIInterface::class.java)
    }


    @Provides
    @Singleton
    fun provideRetrofitForRouting(): APIRouting =
        Retrofit.Builder()
            .baseUrl("http://188.120.232.38:5002/ors/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIRouting::class.java)


}