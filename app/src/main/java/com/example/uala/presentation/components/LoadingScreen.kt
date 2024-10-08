package com.example.uala.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoadingScreen() {
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
}
