package org.captaindmitro.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.captaindmitro.data.API_KEY
import org.captaindmitro.data.BASE_URL
import org.captaindmitro.data.network.ImagesApi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object Network {

    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.NONE })
        .addInterceptor(Interceptor { chain ->
            val origRequest = chain.request()
            val requestBuilder = origRequest.newBuilder()
                .addHeader("Authorization", API_KEY)
            val newRequest = requestBuilder.build()

            chain.proceed(newRequest)
        })
        .build()

    @Provides
    fun provideRetrofitClient(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    @Provides
    fun provideImagesApi(retrofit: Retrofit): ImagesApi = retrofit.create(ImagesApi::class.java)

}