package com.cliniflow.app.ui.medico

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cliniflow.app.utils.proximosDias

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarAgendaScreen(
    viewModel: EditarAgendaViewModel = viewModel(),
    onVoltar: () -> Unit
) {
    val dias = remember { proximosDias(30) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Agenda") },
                navigationIcon = { TextButton(onClick = onVoltar) { Text("‹ Voltar") } }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.CalendarToday, contentDescription = null, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(6.dp))
                Text("Selecione o dia", style = MaterialTheme.typography.labelLarge)
            }
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Schedule, contentDescription = null, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(6.dp))
                    Text("Marque os horários disponíveis nesse dia", style = MaterialTheme.typography.labelLarge)
                }
                Spacer(Modifier.height(8.dp))

                if (viewModel.carregando) {
                    CircularProgressIndicator()
                } else {
                    Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                        viewModel.horariosPadrao.forEach { hora ->
                            FilterChip(
                                selected = hora in viewModel.horariosSelecionados,
                                onClick = { viewModel.alternarHorario(hora) },
                                label = { Text(hora) },
                                modifier = Modifier.padding(end = 8.dp)
                            )
                        }
                    }

                    Spacer(Modifier.height(24.dp))
                    Button(
                        onClick = { viewModel.salvarDisponibilidade() },
                        enabled = !viewModel.salvando,
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    ) {
                        if (viewModel.salvando) CircularProgressIndicator(Modifier.size(20.dp), color = MaterialTheme.colorScheme.onPrimary)
                        else Text("Salvar Disponibilidade")
                    }
                }

                viewModel.mensagem?.let {
                    Spacer(Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (viewModel.salvoComSucesso) {
                            Icon(
                                Icons.Outlined.CheckCircle,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(it, color = MaterialTheme.colorScheme.primary)
                        } else {
                            Text(it, color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }
}