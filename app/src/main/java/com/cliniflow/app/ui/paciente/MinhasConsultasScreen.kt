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
import com.cliniflow.app.ui.components.BadgeTipo
import com.cliniflow.app.ui.components.StatusBadge

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MinhasConsultasScreen(
    viewModel: MinhasConsultasViewModel = viewModel(),
    onVoltar: () -> Unit
) {
    LaunchedEffect(Unit) { viewModel.carregar() }
    var abaSelecionada by remember { mutableStateOf(0) }
    val abas = listOf("Ativas", "Histórico", "Lista de Espera")

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
                Column(Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(consulta.medicoNome, style = MaterialTheme.typography.titleMedium)
                            Text(consulta.especialidade, style = MaterialTheme.typography.bodySmall)
                            Text("${consulta.data} · ${consulta.hora}", style = MaterialTheme.typography.bodySmall)
                        }
                        val (texto, tipo) = statusInfo(consulta.status)
                        StatusBadge(texto = texto, tipo = tipo)
                    }
                    if (mostrarCancelar && (consulta.status == "pendente" || consulta.status == "confirmada")) {
                        Spacer(Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                            TextButton(onClick = { onCancelar(consulta.id) }) { Text("Cancelar") }
                        }
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
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
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

private fun statusInfo(status: String): Pair<String, BadgeTipo> = when (status) {
    "pendente" -> "Pendente" to BadgeTipo.ATENCAO
    "confirmada" -> "Confirmada" to BadgeTipo.SUCESSO
    "realizada" -> "Realizada" to BadgeTipo.INFO
    "cancelada" -> "Cancelada" to BadgeTipo.ERRO
    else -> status to BadgeTipo.NEUTRO
}