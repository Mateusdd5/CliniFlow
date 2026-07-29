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
import com.cliniflow.app.ui.components.Avatar
import com.cliniflow.app.ui.components.BadgeTipo
import com.cliniflow.app.ui.components.StatusBadge
import com.cliniflow.app.utils.hoje

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

            if (lista.isEmpty()) {
                Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) { Text("Nenhuma consulta aqui.") }
            } else {
                LazyColumn(modifier = Modifier.padding(16.dp)) {
                    items(lista) { consulta ->
                        ConsultaCardMedico(
                            consulta = consulta,
                            mostrarAcoes = abaSelecionada == 0,
                            onConfirmar = { viewModel.confirmarConsulta(consulta.id) },
                            onMarcarRealizada = { viewModel.marcarComoRealizada(consulta.id) },
                            onCancelar = { viewModel.cancelarConsulta(consulta.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ConsultaCardMedico(
    consulta: Consulta,
    mostrarAcoes: Boolean,
    onConfirmar: () -> Unit,
    onMarcarRealizada: () -> Unit,
    onCancelar: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Column(Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Avatar(
                        nome = consulta.pacienteNome.substringBefore(" "),
                        sobrenome = consulta.pacienteNome.substringAfter(" ", ""),
                        tamanho = 44.dp
                    )
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text(consulta.pacienteNome, style = MaterialTheme.typography.titleMedium)
                        Text(consulta.especialidade, style = MaterialTheme.typography.bodySmall)
                        Text("${consulta.data} · ${consulta.hora}", style = MaterialTheme.typography.bodySmall)
                    }
                }
                val (texto, tipo) = statusInfo(consulta.status)
                StatusBadge(texto = texto, tipo = tipo)
            }

            if (mostrarAcoes && (consulta.status == "pendente" || consulta.status == "confirmada")) {
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    when (consulta.status) {
                        "pendente" -> TextButton(onClick = onConfirmar) { Text("Confirmar") }
                        "confirmada" -> {
                            val podeRealizar = consulta.data <= hoje()
                            TextButton(onClick = onMarcarRealizada, enabled = podeRealizar) { Text("Marcar como Realizada") }
                        }
                    }
                    TextButton(onClick = onCancelar) { Text("Cancelar") }
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