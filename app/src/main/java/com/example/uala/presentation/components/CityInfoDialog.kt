package com.example.uala.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.example.uala.domain.models.City

@Composable
fun CityInfoDialog(city: City, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Id: ${city.id}") },
        text = {
            Column {
                Text("City: ${city.name}")
                Text("Country: ${city.country}")
                Text("Coordinates: Lat ${city.coordinate.lat}, Lon ${city.coordinate.lon}")
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}