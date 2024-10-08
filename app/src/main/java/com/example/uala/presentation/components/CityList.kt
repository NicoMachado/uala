package com.example.uala.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.uala.domain.models.City
import com.example.uala.presentation.home.MainViewModel

@Composable
fun CityList(
    padding: PaddingValues = PaddingValues(0.dp),
    cities: List<City>,
    searchQuery: String,
    isFavouritesOnly: Boolean,
    isTogglingMap: Map<Int, Boolean>,
    onCitySelected: (City) -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onToggleFavourites: (Boolean) -> Unit,
    viewModel: MainViewModel
) {
    LazyColumn(
        modifier = Modifier. padding(padding).fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        item {
            SearchBar(searchQuery = searchQuery, onSearchQueryChanged = onSearchQueryChanged)
        }
        item {
            FavouritesToggle(isFavouritesOnly = isFavouritesOnly, onToggleFavourites = onToggleFavourites)
        }
        items(cities, key = { it.id }) { city ->
            CityCard(
                city = city,
                isToggling = isTogglingMap[city.id] == true,
                onToggleFavourite = { viewModel.toggleFavourite(it) },
                onNavigateToMap = { onCitySelected(it) },
            )
        }
    }
}
