package com.dosne.minigames.ui.wordgame

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun WordGameScreen(
    playerName: String,
    onBackClick: () -> Unit,
    viewModel: WordGameViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(playerName) {
        viewModel.startGame(playerName)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Mot caché",
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(18.dp))

        if (state.phase == WordGamePhase.GAME_OVER) {
            Text("Temps écoulé", fontSize = 22.sp)
            Text("Score final : ${state.score}", fontSize = 28.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = { viewModel.restartGame(playerName) }, modifier = Modifier.fillMaxWidth()) {
                Text("Rejouer")
            }
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(onClick = onBackClick, modifier = Modifier.fillMaxWidth()) {
                Text("Retour au menu")
            }
        } else {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Temps : ${state.timeLeft}s", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text("Score : ${state.score}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text("Mot de ${state.hiddenWordLength} lettres")

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = state.selectedWord.ifBlank { "..." },
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = viewModel::eraseLast, enabled = state.selectedWord.isNotEmpty()) {
                    Text("Effacer")
                }
            }

            Text(
                text = state.message.ifBlank { state.hint },
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.height(24.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(state.grid) { index, cell ->
                    Button(
                        onClick = { viewModel.selectCell(index) },
                        enabled = !cell.isSelected,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(cell.char.toString(), fontSize = 26.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                Button(onClick = viewModel::validate, modifier = Modifier.weight(1f)) {
                    Text("Valider")
                }
                OutlinedButton(onClick = viewModel::pass, modifier = Modifier.weight(1f)) {
                    Text("Passer")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = viewModel::revealHint,
                    enabled = !state.hintUsed,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Indice")
                }
                OutlinedButton(onClick = onBackClick, modifier = Modifier.weight(1f)) {
                    Text("Retour")
                }
            }
        }
    }
}
