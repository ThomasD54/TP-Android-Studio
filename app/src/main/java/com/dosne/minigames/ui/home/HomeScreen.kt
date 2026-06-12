package com.dosne.minigames.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private const val PLAYER_PREFS = "player_preferences"
private const val PLAYER_NAME_KEY = "player_name"

@Composable
fun HomeScreen(
    onReactionClick: (String) -> Unit,
    onWordGameClick: (String) -> Unit,
    onLeaderboardClick: () -> Unit
) {
    val context = LocalContext.current
    val preferences = remember {
        context.getSharedPreferences(PLAYER_PREFS, 0)
    }
    var playerName by remember {
        mutableStateOf(preferences.getString(PLAYER_NAME_KEY, "").orEmpty())
    }
    val cleanPlayerName = playerName.trim()
    val canPlay = cleanPlayerName.isNotEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "MiniGames",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F1F1F)
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = playerName,
            onValueChange = { newName ->
                playerName = newName
                preferences.edit().putString(PLAYER_NAME_KEY, newName.trim()).apply()
            },
            label = { Text("Pseudo") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        GameMenuCard(
            title = "Jeu de réaction",
            description = "Teste ta réactivité",
            enabled = canPlay,
            onClick = { onReactionClick(cleanPlayerName) }
        )

        Spacer(modifier = Modifier.height(12.dp))

        GameMenuCard(
            title = "Mot caché",
            description = "Trouve le mot dans la grille",
            enabled = canPlay,
            onClick = { onWordGameClick(cleanPlayerName) }
        )

        Spacer(modifier = Modifier.height(12.dp))

        GameMenuCard(
            title = "Leaderboard",
            description = "Consulte les meilleurs scores",
            enabled = true,
            onClick = onLeaderboardClick
        )
    }
}

@Composable
private fun GameMenuCard(
    title: String,
    description: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val background = if (enabled) MaterialTheme.colorScheme.primary else Color(0xFFC6B7B7)
    val content = if (enabled) Color.White else Color(0xFF6A5F5F)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(background, RoundedCornerShape(12.dp))
            .clickable(enabled = enabled, onClick = onClick)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = content
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = description,
            fontSize = 14.sp,
            color = content
        )
    }
}
