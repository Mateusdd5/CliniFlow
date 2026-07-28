package com.cliniflow.app.ui.paciente

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cliniflow.app.model.Usuario
import com.cliniflow.app.utils.proximosDias

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelecionarHorarioScreen(
    medico: Usuario,
    viewModel: SelecionarHorarioViewModel = viewModel(),
    onVoltar: () -> Unit,
    onConcluido: () -> Unit
) {
    viewModel.iniciar(medico)
    val dias = remember { proximosDias(14) }

    LaunchedEffect(viewModel.agendamentoConcluido, viewModel.entrouNaFila) {
        if (viewModel.agendamentoConcluido || viewModel.entrouNaFila) onConcluido()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Selecione Data e Hora") },
                navigationIcon = { TextButton(onClick = onVoltar) { Text("‹ Voltar") } }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)) {

            Text("Dr(a). ${medico.nome} ${medico.sobrenome}", style = MaterialTheme.typography.titleLarge)
            Text(medico.especialidade ?: "", style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(16.dp))

            Text("Data", style = MaterialTheme.typography.labelLarge)
            Spacer(Modifier.height(8.dp))
            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                dias.forEach { (iso, label) ->
                    FilterChip(
                        selected = viewModel.dataSelecionada == iso,
                        onClick = { viewModel.selecionarData(iso) },
                        label = { Text(label) },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }
            Spacer(Modifier.height(24.dp))

            if (viewModel.dataSelecionada.isNotBlank()) {
                Text("Horário", style = MaterialTheme.typography.labelLarge)
                Spacer(Modifier.height(8.dp))

                if (viewModel.carregandoHorarios) {
                    CircularProgressIndicator()
                } else if (viewModel.horariosDisponiveis.isEmpty()) {
                    Text("O médico ainda não abriu horários para esse dia.")
                } else {
                    LazyVerticalGrid(columns = GridCells.Fixed(3), modifier = Modifier.height(160.dp)) {
                        items(viewModel.horariosDisponiveis) { hora ->
                            val ocupado = hora in viewModel.horariosOcupados
                            OutlinedButton(
                                onClick = {
                                    if (ocupado) viewModel.selecionarHorarioOcupado(hora)
                                    else viewModel.agendar(hora)
                                },
                                modifier = Modifier.padding(4.dp)
                            ) {
                                Text(if (ocupado) "$hora ✕" else hora)
                            }
                        }
                    }
                }

                viewModel.horarioParaFila?.let { hora ->
                    Spacer(Modifier.height(12.dp))
                    Card {
                        Column(Modifier.padding(12.dp)) {
                            Text("O horário $hora já está ocupado.")
                            Spacer(Modifier.height(8.dp))
                            Row {
                                Button(onClick = { viewModel.confirmarEntradaNaFila() }) { Text("Entrar na Lista de Espera") }
                                Spacer(Modifier.width(8.dp))
                                TextButton(onClick = { viewModel.cancelarSelecaoFila() }) { Text("Cancelar") }
                            }
                        }
                    }
                }

                viewModel.mensagem?.let {
                    Spacer(Modifier.height(12.dp))
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
            }

            Text(
                "Horários ocupados são automaticamente bloqueados para evitar conflitos.",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 24.dp)
            )
        }
    }
}