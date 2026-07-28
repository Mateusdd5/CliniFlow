package com.cliniflow.app.ui.medico

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cliniflow.app.model.Consulta

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicoConsultasScreen(
    viewModel: MedicoConsultasViewModel = viewModel(),
    onVoltar: () -> Unit
) {
    LaunchedEffect(Unit) { viewModel.carregar() }
    var abaSelecionada by remember { mutableStateOf(0) }
    val abas = listOf("Ativas", "Histórico")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Minhas Consultas") },
                navigationIcon = { TextButton(onClick = onVoltar) { Text("‹ Voltar") } }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            TabRow(selectedTabIndex = abaSelecionada) {
                abas.forEachIndexed { index, titulo ->
                    Tab(selected = abaSelecionada == index, onClick = { abaSelecionada = index }, text = { Text(titulo) })
                }
            }

            if (viewModel.carregando) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
                return@Column
            }

            val lista = if (abaSelecionada == 0) viewModel.consultasAtivas else viewModel.consultasHistorico
            val mostrarCancelar = abaSelecionada == 0

            if (lista.isEmpty()) {
                Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) { Text("Nenhuma consulta aqui.") }
            } else {
                LazyColumn(modifier = Modifier.padding(16.dp)) {
                    items(lista) { consulta ->
                        ConsultaCardMedico(consulta, mostrarCancelar) { viewModel.cancelarConsulta(consulta.id) }
                    }
                }
            }
        }
    }
}

@Composable
private fun ConsultaCardMedico(consulta: Consulta, mostrarCancelar: Boolean, onCancelar: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text(consulta.pacienteNome, style = MaterialTheme.typography.titleMedium)
                Text(consulta.especialidade, style = MaterialTheme.typography.bodySmall)
                Text("${consulta.data} · ${consulta.hora}", style = MaterialTheme.typography.bodySmall)
                Text(statusLegivelMedico(consulta.status), style = MaterialTheme.typography.labelMedium)
            }
            if (mostrarCancelar && (consulta.status == "pendente" || consulta.status == "confirmada")) {
                TextButton(onClick = onCancelar) { Text("Cancelar") }
            }
        }
    }
}

private fun statusLegivelMedico(status: String): String = when (status) {
    "pendente" -> "Pendente"
    "confirmada" -> "Confirmada"
    "realizada" -> "Realizada"
    "cancelada" -> "Cancelada"
    else -> status
}