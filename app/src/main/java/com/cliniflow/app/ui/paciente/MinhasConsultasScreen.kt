package com.cliniflow.app.ui.paciente

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
import com.cliniflow.app.model.ListaEspera

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MinhasConsultasScreen(viewModel: MinhasConsultasViewModel = viewModel()) {
    LaunchedEffect(Unit) { viewModel.carregar() }
    var abaSelecionada by remember { mutableStateOf(0) }
    val abas = listOf("Ativas", "Histórico", "Lista de Espera")

    Scaffold(topBar = { TopAppBar(title = { Text("Minhas Consultas") }) }) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            TabRow(selectedTabIndex = abaSelecionada) {
                abas.forEachIndexed { index, titulo ->
                    Tab(
                        selected = abaSelecionada == index,
                        onClick = { abaSelecionada = index },
                        text = { Text(titulo) }
                    )
                }
            }

            if (viewModel.carregando) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
                return@Column
            }

            when (abaSelecionada) {
                0 -> ListaConsultas(viewModel.consultasAtivas, mostrarCancelar = true, onCancelar = { viewModel.cancelarConsulta(it) })
                1 -> ListaConsultas(viewModel.consultasHistorico, mostrarCancelar = false, onCancelar = {})
                2 -> ListaDeEsperaTab(viewModel.filasDeEspera, onSair = { viewModel.sairDaFila(it) })
            }
        }
    }
}

@Composable
private fun ListaConsultas(consultas: List<Consulta>, mostrarCancelar: Boolean, onCancelar: (String) -> Unit) {
    if (consultas.isEmpty()) {
        Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) { Text("Nenhuma consulta aqui.") }
        return
    }
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(consultas) { consulta ->
            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text(consulta.medicoNome, style = MaterialTheme.typography.titleMedium)
                        Text(consulta.especialidade, style = MaterialTheme.typography.bodySmall)
                        Text("${consulta.data} · ${consulta.hora}", style = MaterialTheme.typography.bodySmall)
                        Text(statusLegivel(consulta.status), style = MaterialTheme.typography.labelMedium)
                    }
                    if (mostrarCancelar && (consulta.status == "pendente" || consulta.status == "confirmada")) {
                        TextButton(onClick = { onCancelar(consulta.id) }) { Text("Cancelar") }
                    }
                }
            }
        }
    }
}

@Composable
private fun ListaDeEsperaTab(filas: List<ListaEspera>, onSair: (String) -> Unit) {
    if (filas.isEmpty()) {
        Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) { Text("Você não está em nenhuma lista de espera.") }
        return
    }
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(filas) { item ->
            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text(item.medicoNome, style = MaterialTheme.typography.titleMedium)
                        Text("${item.data} · ${item.hora}", style = MaterialTheme.typography.bodySmall)
                    }
                    TextButton(onClick = { onSair(item.id) }) { Text("Sair da fila") }
                }
            }
        }
    }
}

private fun statusLegivel(status: String): String = when (status) {
    "pendente" -> "Pendente"
    "confirmada" -> "Confirmada"
    "realizada" -> "Realizada"
    "cancelada" -> "Cancelada"
    else -> status
}