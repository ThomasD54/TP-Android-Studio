package com.dosne.minigames

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.dosne.minigames.ui.home.HomeScreen
import com.dosne.minigames.ui.reaction.ReactionScreen
import com.dosne.minigames.ui.theme.MiniGamesAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MiniGamesAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
                    MiniGamesApp()
                }
            }
        }
    }
}

@Composable
fun MiniGamesApp() {
    var selectedGame by remember { mutableStateOf<String?>(null) }
    
    when (selectedGame) {
        "reaction" -> ReactionScreen(onBackClick = { selectedGame = null })
        else -> HomeScreen(onGameSelected = { gameId -> selectedGame = gameId })
    }
}