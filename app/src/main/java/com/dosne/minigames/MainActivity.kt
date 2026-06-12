package com.dosne.minigames

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.dosne.minigames.ui.home.HomeScreen
import com.dosne.minigames.ui.leaderboard.LeaderboardScreen
import com.dosne.minigames.ui.reaction.ReactionScreen
import com.dosne.minigames.ui.theme.MiniGamesAppTheme
import com.dosne.minigames.ui.wordgame.WordGameScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MiniGamesAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background
                ) { _ ->
                    MiniGamesApp()
                }
            }
        }
    }
}

@Composable
fun MiniGamesApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Home) {
        composable<Home> {
            HomeScreen(
                onReactionClick = { playerName -> navController.navigate(Reaction(playerName)) },
                onWordGameClick = { playerName -> navController.navigate(WordGame(playerName)) },
                onLeaderboardClick = { navController.navigate(Leaderboard) }
            )
        }
        composable<Reaction> { entry ->
            val route = entry.toRoute<Reaction>()
            ReactionScreen(
                playerName = route.playerName,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable<WordGame> { entry ->
            val route = entry.toRoute<WordGame>()
            WordGameScreen(
                playerName = route.playerName,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable<Leaderboard> {
            LeaderboardScreen(onBackClick = { navController.popBackStack() })
        }
    }
}
