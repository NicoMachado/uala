package com.example.uala.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.uala.domain.models.City

@Composable
fun CityCard(
    city: City,
    isToggling: Boolean = false,
    onToggleFavourite: (City) -> Unit,
    onNavigateToMap: (City) -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onNavigateToMap(city) }, // Navega al mapa al hacer clic en la Card
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Título: Ciudad y País
            Text(
                text = "${city.name}, ${city.country}",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Subtítulo: Coordenadas
            Text(
                text = "Lat: ${city.coordinate.lat}, Lon: ${city.coordinate.lon}",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isToggling) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(8.dp).padding(end = 16.dp)
                    )
                } else {
                    IconButton(onClick = {
                        onToggleFavourite(city)
                    }) {
                        Icon(
                            imageVector = if (city.isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (city.isFavourite) "Remove from favourites" else "Add to favourites",
                            tint = if (city.isFavourite) Color.Red else Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(onClick = { showDialog = true }) {
                    Text("Info")
                }
            }
        }
    }

    if (showDialog) {
        CityInfoDialog(city = city, onDismiss = { showDialog = false })
    }
}
