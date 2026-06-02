package com.dosne.minigames.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class MiniGame(
    val id: String,
    val title: String,
    val description: String,
    val color: Color
)

@Composable
fun HomeScreen(onGameSelected: (String) -> Unit) {
    val games = listOf(
        MiniGame(
            id = "reaction",
            title = "Jeu de réaction",
            description = "Teste ta réactivité",
            color = Color(0xFF9B7979)
        ),
        MiniGame(
            id = "words",
            title = "Jeu de Mots",
            description = "À venir...",
            color = Color(0xFF9B7979)
        ),
        MiniGame(
            id = "quiz",
            title = "Quiz",
            description = "À venir...",
            color = Color(0xFF9B7979)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Titre
        Text(
            text = "MiniGames",
            style = TextStyle(
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F1F1F)
            ),
            modifier = Modifier.padding(top = 32.dp, bottom = 48.dp)
        )

        // Grille de jeux
        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(games) { game ->
                GameCard(
                    game = game,
                    onClick = { onGameSelected(game.id) }
                )
            }
        }
    }
}

@Composable
fun GameCard(game: MiniGame, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = game.color,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(enabled = game.id == "reaction") { onClick() }
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = game.title,
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = game.description,
            style = TextStyle(
                fontSize = 14.sp,
                color = Color.White
            )
        )
    }
}

