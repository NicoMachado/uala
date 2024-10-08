package com.example.uala.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.uala.domain.models.City
import com.example.uala.presentation.components.CityList
import com.example.uala.presentation.components.LoadingScreen
import com.example.uala.presentation.navigation.isLandscape
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    onBack: () -> Unit = {},
) {
    val viewModel: MainViewModel = koinViewModel()

    val cities = viewModel.filteredCities
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isTogglingMap by viewModel.isTogglingMap.collectAsState()
    val isFavouritesOnly by viewModel.isFavouritesOnly.collectAsState()

    val selectedCity = remember { mutableStateOf<City?>(null) }
    val isLandscape = isLandscape()

    if (isLoading) {
        LoadingScreen()
    } else {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                CenterAlignedTopAppBar(title = {
                    Text(text = "Home")
                }, navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                })
            }

        ) { padding ->
            if (isLandscape) {
                // landscape
                Row(
                    Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    Box(Modifier.weight(0.4f)) {
                        CityList(
                            cities = cities,
                            searchQuery = searchQuery,
                            isFavouritesOnly = isFavouritesOnly,
                            isTogglingMap = isTogglingMap,
                            onCitySelected = { city -> selectedCity.value = city },
                            onSearchQueryChanged = { viewModel.onSearchQueryChanged(it) },
                            onToggleFavourites = { viewModel.onToggleFavourites(it) },
                            viewModel = viewModel
                        )
                    }
                    Box(Modifier.weight(0.6f)) {
                        selectedCity.value?.let { selectedCity ->
                            CityMapShow(
                                name = selectedCity.name,
                                lat = selectedCity.coordinate.lat,
                                lon = selectedCity.coordinate.lon
                            )
                        } ?: Text(text = "Seleccione una ciudad", Modifier.padding(16.dp))
                    }
                }
            } else {
                //Portrait
                CityList(
                    padding = padding,
                    cities = cities,
                    searchQuery = searchQuery,
                    isFavouritesOnly = isFavouritesOnly,
                    isTogglingMap = isTogglingMap,
                    onCitySelected = { city -> navController.navigate("cityMapScreen/${city.coordinate.lat}/${city.coordinate.lon}") },
                    onSearchQueryChanged = { viewModel.onSearchQueryChanged(it) },
                    onToggleFavourites = { viewModel.onToggleFavourites(it) },
                    viewModel = viewModel
                )
            }

        }
    }
}

@Composable
private fun CityMapShow(
    name: String,
    lat: Double,
    lon: Double,
) {
    val cityLocation = LatLng(lat, lon)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(cityLocation, 10f)
    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        Marker(
            state = MarkerState(position = cityLocation),
            title = name,
            snippet = "Lat: $lat, Lon: $lon"
        )

    }
}
