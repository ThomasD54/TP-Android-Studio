package com.dosne.minigames.ui.reaction

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dosne.minigames.viewmodel.ReactionPhase
import com.dosne.minigames.viewmodel.ReactionViewModel
import java.util.Locale

private fun formatSeconds(valueMs: Long): String {
    return String.format(Locale.getDefault(), "%.2f s", valueMs / 1000.0)
}

@Composable
fun ReactionScreen(
    playerName: String,
    onBackClick: () -> Unit,
    viewModel: ReactionViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Jeu de réaction",
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(28.dp))

        Surface(
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Cible : ${formatSeconds(state.targetMs)}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(state.speedLabel, fontSize = 14.sp, color = MaterialTheme.colorScheme.secondary)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = if (state.isBlind) "????" else formatSeconds(state.elapsedMs),
            fontSize = 58.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        if (state.isBlind) {
            Text("Timer masqué près de la cible", color = MaterialTheme.colorScheme.tertiary)
        }

        Spacer(modifier = Modifier.height(28.dp))

        when (state.phase) {
            ReactionPhase.READY -> {
                Button(
                    onClick = { viewModel.startGame(playerName) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Démarrer")
                }
            }

            ReactionPhase.PLAYING -> {
                Button(
                    onClick = viewModel::stopTimer,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Stop !")
                }
            }

            ReactionPhase.RESULT -> {
                Text(
                    text = "Écart : ${formatSeconds(state.differenceMs)}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Score : ${state.score}",
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(viewModel.feedbackMessage(), fontSize = 20.sp)

                Spacer(modifier = Modifier.height(20.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = { viewModel.startGame(playerName) }) {
                        Text("Rejouer")
                    }
                    OutlinedButton(onClick = onBackClick) {
                        Text("Retour")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (state.phase != ReactionPhase.RESULT) {
            OutlinedButton(onClick = onBackClick, modifier = Modifier.fillMaxWidth()) {
                Text("Retour au menu")
            }
        }
    }
}
