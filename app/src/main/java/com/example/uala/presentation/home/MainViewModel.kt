package com.example.uala.presentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uala.domain.models.City
import com.example.uala.domain.repository.CitiesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed class MainScreenEvents {
    data class NavigateToMap(val lat: Double, val lon: Double) : MainScreenEvents()
}

class MainViewModel(
    private val repository: CitiesRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private var cities by mutableStateOf< List<City>>( emptyList())
        private set

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    val _isTogglingMap = MutableStateFlow<Map<Int, Boolean>>(emptyMap())
    var isTogglingMap = _isTogglingMap.asStateFlow()

    //private val _searchQuery = MutableStateFlow("")
    //val searchQuery = _searchQuery.asStateFlow()
    val searchQuery = savedStateHandle.getStateFlow("searchQuery", "")

    private val _isFavouritesOnly = MutableStateFlow(false)
    val isFavouritesOnly = _isFavouritesOnly.asStateFlow()

    var filteredCities by mutableStateOf<List<City>>(emptyList())
        private set

    private val eventChannel = Channel<MainScreenEvents>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    init {
        fetchCities()
    }

    private fun fetchCities() {
        viewModelScope.launch(Dispatchers.IO) {
            cities = repository.getCities()
            filterCities()
            _isLoading.value = false
        }
    }

    private fun filterCities() {
        val searchQueryLower = searchQuery.value.lowercase()

        filteredCities = cities.filter { city ->
            val matchesFavouriteFilter = !isFavouritesOnly.value || city.isFavourite
            val matchesSearchQuery = city.name.lowercase().startsWith(searchQueryLower, ignoreCase = true)
            matchesFavouriteFilter && matchesSearchQuery
        }
    }

    fun onSearchQueryChanged(query: String) {
        savedStateHandle["searchQuery"] = query
        filterCities() // Filtramos la lista cuando el texto de búsqueda cambia
    }
    fun toggleFavourite(city: City) {
        viewModelScope.launch {
            _isTogglingMap.value = _isTogglingMap.value.toMutableMap().apply {
                put(city.id, true)
            }
            withContext(Dispatchers.IO) {
                try {
                    repository.toggleFavourite(city)
                } catch (e: Exception){
                    e.printStackTrace()
                }
            }
            fetchCities()
            _isTogglingMap.value = _isTogglingMap.value.toMutableMap().apply {
                put(city.id, false)
            }
        }
    }

    fun onToggleFavourites(onlyFavourites: Boolean) {
        _isFavouritesOnly.value = onlyFavourites
        filterCities()
    }

}
