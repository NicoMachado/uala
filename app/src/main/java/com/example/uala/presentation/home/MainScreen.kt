package com.example.uala.presentation.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.uala.presentation.components.CityCard
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.get

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = get(),
    navController: NavController,
    onBack: () -> Unit = {},
) {
    val cities = viewModel.filteredCities
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isTogglingMap by viewModel.isTogglingMap.collectAsState()
    val isFavouritesOnly by viewModel.isFavouritesOnly.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.eventsFlow.collectLatest { event ->
            when (event) {
                is MainScreenEvents.NavigateToMap -> {
                    navController.navigate("cityMapScreen/${event.lat}/${event.lon}")
                }
            }
        }
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(.9f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Loading...",
                    style = TextStyle(fontSize = 24.sp),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Please wait...",
                    style = TextStyle(fontSize = 20.sp),
                    textAlign = TextAlign.Center
                )
            }
        }
        return
    }

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
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(start = 20.dp),
            contentPadding = PaddingValues(
                horizontal = 8.dp
            )
        ) {
            stickyHeader {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(vertical = 4.dp),
                    style = TextStyle(fontSize = 20.sp),
                    textAlign = TextAlign.Center,
                    text = "Uala - Challenge"
                )
            }

            item {
                Spacer(modifier = Modifier.height(4.dp))
                TextField(
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                        .fillParentMaxWidth(),
                    value = searchQuery,
                    onValueChange = { q -> viewModel.onSearchQueryChanged(q) },
                    maxLines = 1,
                    textStyle = TextStyle(
                        fontSize = 14.sp
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "Search Icon"
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear text"
                                )
                            }
                        }
                    }
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Mostrar solo favoritos")
                    Spacer(modifier = Modifier.weight(1f))
                    Switch(
                        checked = isFavouritesOnly,
                        onCheckedChange = { viewModel.onToggleFavourites(it) }
                    )
                }
            }
            items(cities, key = { it.id }) { city ->
                CityCard(
                    city = city,
                    isLoading = isTogglingMap[city.id] == true,
                    onToggleFavourite = { viewModel.toggleFavourite(it) },
                    onNavigateToMap = { viewModel.navigateToCityMap(it) },
                    onOpenInfoScreen = { /* viewModel.openInfoScreen(it)*/ }
                )
            }
        }

    }


}