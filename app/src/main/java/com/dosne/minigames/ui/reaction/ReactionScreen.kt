package com.dosne.minigames.ui.reaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dosne.minigames.viewmodel.ReactionViewModel
import kotlinx.coroutines.delay

@Composable
fun ReactionScreen(onBackClick: () -> Unit) {
    // Créer ou récupérer le ViewModel
    val viewModel: ReactionViewModel = viewModel()
    
    // Gérer le timer avec LaunchedEffect
    LaunchedEffect(viewModel.isRunning) {
        while (viewModel.isRunning) {
            delay(10L) // Attendre 10ms
            viewModel.updateTimer() // Mettre à jour le timer
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Jeu de Réaction",
            style = TextStyle(
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6200EE)
            ),
            modifier = Modifier.padding(bottom = 32.dp)
        )
        
        Text(
            text = "Valeur cible: ${viewModel.targetValue}ms",
            style = TextStyle(
                fontSize = 18.sp,
                color = Color(0xFF1F1F1F)
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Affichage du timer avec couleur dynamique
        Text(
            text = "${viewModel.elapsedMs}ms",
            style = TextStyle(
                fontSize = 60.sp,
                fontWeight = FontWeight.Bold,
                color = Color(viewModel.getTimerColor())
            ),
            modifier = Modifier.padding(bottom = 32.dp)
        )
        
        // Phase de jeu
        if (viewModel.gamePhase == "playing") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                Button(
                    onClick = { viewModel.startTimer() },
                    enabled = !viewModel.isRunning
                ) {
                    Text("Démarrer", fontSize = 16.sp)
                }
                
                Button(
                    onClick = { viewModel.stopTimer() },
                    enabled = viewModel.isRunning
                ) {
                    Text("Stop!", fontSize = 16.sp)
                }
            }
        }
        // Phase de résultat
        else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                Text(
                    text = "Écart: ${viewModel.difference}ms",
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F1F1F)
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                Text(
                    text = viewModel.getFeedbackMessage(),
                    style = TextStyle(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(viewModel.getFeedbackColor())
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(onClick = { viewModel.restartGame() }) {
                    Text("Rejouer", fontSize = 16.sp)
                }
                
                Button(onClick = onBackClick) {
                    Text("Retour", fontSize = 16.sp)
                }
            }
        }
    }
}
