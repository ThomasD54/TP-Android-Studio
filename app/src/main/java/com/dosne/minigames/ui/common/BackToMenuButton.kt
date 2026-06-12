package com.dosne.minigames.ui.common

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp

@Composable
fun BackToMenuButton(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text("Retour au menu", fontSize = 16.sp)
    }
}