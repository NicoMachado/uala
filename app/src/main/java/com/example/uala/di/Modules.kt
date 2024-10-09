package com.example.uala.di

import androidx.lifecycle.SavedStateHandle
import com.example.uala.data.CitiesRepositoryImpl
import com.example.uala.data.datasource.RemoteDataSource
import com.example.uala.data.remote.ApiService
import com.example.uala.domain.repository.CitiesRepository
import com.example.uala.presentation.home.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single { provideRetrofit() }
    single { provideApiService(get()) }

    single { RemoteDataSource(get()) }
    single<CitiesRepository> { CitiesRepositoryImpl(get()) }

    viewModel { (handle: SavedStateHandle) -> MainViewModel(get(), handle) }
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