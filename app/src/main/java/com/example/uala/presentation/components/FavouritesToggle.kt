package com.example.uala.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FavouritesToggle(
    isFavouritesOnly: Boolean,
    onToggleFavourites: (Boolean) -> Unit
) {
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
            onCheckedChange = onToggleFavourites
        )
    }
}
