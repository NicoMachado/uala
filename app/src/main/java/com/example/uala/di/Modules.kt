package com.example.uala.di

import com.example.uala.data.CitiesRepositoryImpl
import com.example.uala.data.datasource.RemoteDataSource
import com.example.uala.data.remote.ApiService
import com.example.uala.presentation.home.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single { provideRetrofit() }
    single { provideApiService(get()) }

    single { RemoteDataSource(get()) }
    single { CitiesRepositoryImpl(get()) }

    viewModel<MainViewModel> { MainViewModel(get()) }
}

fun provideRetrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl(ApiService.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

fun provideApiService(retrofit: Retrofit): ApiService {
    return retrofit.create(ApiService::class.java)
}