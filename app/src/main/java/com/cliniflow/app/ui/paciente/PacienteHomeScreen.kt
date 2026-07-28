package com.cliniflow.app.ui.paciente

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PacienteHomeScreen(
    viewModel: PacienteHomeViewModel = viewModel(),
    onNovoAgendamento: () -> Unit,
    onMinhasConsultas: () -> Unit,
    onEditarPerfil: () -> Unit,
    onLogout: () -> Unit
) {
    LaunchedEffect(Unit) { viewModel.carregarDados() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Olá, ${viewModel.usuario?.nome ?: ""}!") },
                actions = { TextButton(onClick = { viewModel.logout(); onLogout() }) { Text("Sair") } }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            if (viewModel.carregando) {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
                return@Column
            }

            val consulta = viewModel.proximaConsulta
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Próxima Consulta:", style = MaterialTheme.typography.labelLarge)
                    Spacer(Modifier.height(4.dp))
                    if (consulta != null) {
                        Text(consulta.medicoNome, style = MaterialTheme.typography.titleLarge)
                        Text("${consulta.data}, ${consulta.hora}")
                    } else {
                        Text("Nenhuma consulta agendada", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
            Button(onClick = onNovoAgendamento, modifier = Modifier.fillMaxWidth()) { Text("+ Novo Agendamento") }

            Spacer(Modifier.height(12.dp))
            OutlinedButton(onClick = onMinhasConsultas, modifier = Modifier.fillMaxWidth()) { Text("Minhas Consultas") }

            Spacer(Modifier.height(12.dp))
            OutlinedButton(onClick = onEditarPerfil, modifier = Modifier.fillMaxWidth()) { Text("Editar Perfil") }
        }
    }
}