package com.example.news.di

import com.example.news.home.HomeViewModel
import com.example.news.popularity.PopularityViewModel
import com.example.news.search.SearchViewModel
import com.example.news.usecase.GetEveryThingUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    factory { GetEveryThingUseCase(get()) }
}

val viewModelModule = module {
    viewModel { HomeViewModel(get(), get()) }
    viewModel { SearchViewModel(get(), get()) }
    viewModel { PopularityViewModel(get(), get()) }
}
