package com.cliniflow.app.ui.admin

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cliniflow.app.model.Consulta
import com.cliniflow.app.model.ListaEspera

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminHistoricoScreen(
    viewModel: AdminHistoricoViewModel = viewModel(),
    onVoltar: () -> Unit
) {
    LaunchedEffect(Unit) { viewModel.carregar() }
    var abaSelecionada by remember { mutableStateOf(0) }
    val abas = listOf("Consultas", "Listas de Espera")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Histórico do Sistema") },
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

            if (abaSelecionada == 0) ConsultasTab(viewModel) else ListaEsperaGeralTab(viewModel.filasDeEspera)
        }
    }
}

@Composable
private fun ConsultasTab(viewModel: AdminHistoricoViewModel) {
    val filtros = listOf("Todos" to null, "Pendentes" to "pendente", "Confirmadas" to "confirmada", "Realizadas" to "realizada", "Canceladas" to "cancelada")

    Column {
        Row(modifier = Modifier.horizontalScroll(rememberScrollState()).padding(16.dp)) {
            filtros.forEach { (titulo, status) ->
                FilterChip(
                    selected = viewModel.filtroStatus == status,
                    onClick = { viewModel.selecionarFiltro(status) },
                    label = { Text(titulo) },
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }

        if (viewModel.consultasFiltradas.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) { Text("Nenhuma consulta encontrada.") }
        } else {
            LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
                items(viewModel.consultasFiltradas) { consulta -> ConsultaCardAdmin(consulta) }
            }
        }
    }
}

@Composable
private fun ConsultaCardAdmin(consulta: Consulta) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Column(Modifier.padding(16.dp)) {
            Text("Paciente: ${consulta.pacienteNome}", style = MaterialTheme.typography.titleSmall)
            Text("Médico: ${consulta.medicoNome} · ${consulta.especialidade}", style = MaterialTheme.typography.bodySmall)
            Text("${consulta.data} · ${consulta.hora}", style = MaterialTheme.typography.bodySmall)
            Text(statusLegivelAdmin(consulta.status), style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
private fun ListaEsperaGeralTab(filas: List<ListaEspera>) {
    if (filas.isEmpty()) {
        Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) { Text("Nenhuma fila de espera ativa no momento.") }
        return
    }
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(filas) { item ->
            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                Column(Modifier.padding(16.dp)) {
                    Text("Paciente: ${item.pacienteNome}", style = MaterialTheme.typography.titleSmall)
                    Text("Médico: ${item.medicoNome} · ${item.especialidade}", style = MaterialTheme.typography.bodySmall)
                    Text("${item.data} · ${item.hora}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

private fun statusLegivelAdmin(status: String): String = when (status) {
    "pendente" -> "Pendente"
    "confirmada" -> "Confirmada"
    "realizada" -> "Realizada"
    "cancelada" -> "Cancelada"
    else -> status
}