package com.example.uala.presentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uala.data.CitiesRepositoryImpl
import com.example.uala.domain.models.City
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed class MainScreenEvents {
    data class NavigateToMap(val lat: Double, val lon: Double) : MainScreenEvents()
}

class MainViewModel(
    private val repository: CitiesRepositoryImpl
): ViewModel() {
    var cities by mutableStateOf< List<City>>( emptyList())
        private set

    //    var isLoading = mutableStateOf(true)
    //        private set
    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    val _isTogglingMap = MutableStateFlow<Map<Int, Boolean>>(emptyMap())
    var isTogglingMap = _isTogglingMap.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

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

//            isFavouritesOnly.value && city.isFavourite ||
//                    !isFavouritesOnly.value &&
//            city.name.lowercase().startsWith(searchQuery.value.lowercase(), ignoreCase = true)
            matchesFavouriteFilter && matchesSearchQuery
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
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
                    delay(1000)
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

    fun isCityToggling(cityId: Int): Boolean {
        return isTogglingMap.value[cityId] == true
    }

    fun onToggleFavourites(onlyFavourites: Boolean) {
        _isFavouritesOnly.value = onlyFavourites
        filterCities()
    }

    fun navigateToCityMap(cordinates: City.Coord) {
        viewModelScope.launch {
            eventChannel.send(MainScreenEvents.NavigateToMap(cordinates.lat, cordinates.lon))
        }
    }
}
