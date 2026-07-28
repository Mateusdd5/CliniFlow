package com.cliniflow.app.ui.medico

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
fun MedicoHomeScreen(
    viewModel: MedicoHomeViewModel = viewModel(),
    onEditarAgenda: () -> Unit,
    onMinhasConsultas: () -> Unit,
    onEditarPerfil: () -> Unit,
    onLogout: () -> Unit
) {
    LaunchedEffect(Unit) { viewModel.carregarDados() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bem-vindo, Dr(a). ${viewModel.usuario?.nome ?: ""}") },
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
                    Text("Próxima Consulta", style = MaterialTheme.typography.labelLarge)
                    Spacer(Modifier.height(4.dp))
                    if (consulta != null) {
                        Text(consulta.pacienteNome, style = MaterialTheme.typography.titleLarge)
                        Text("${consulta.data}, ${consulta.hora} · ${consulta.especialidade}")
                    } else {
                        Text("Nenhuma consulta agendada", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            Spacer(Modifier.height(12.dp))
            Card(modifier = Modifier.fillMaxWidth()) {
                Row(Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Consultas pendentes")
                    Text("${viewModel.totalPendentes}", style = MaterialTheme.typography.titleLarge)
                }
            }

            Spacer(Modifier.height(24.dp))
            Text("Ações Rápidas", style = MaterialTheme.typography.labelLarge)
            Spacer(Modifier.height(8.dp))

            Button(onClick = onEditarAgenda, modifier = Modifier.fillMaxWidth()) { Text("Editar Agenda") }
            Spacer(Modifier.height(8.dp))
            OutlinedButton(onClick = onMinhasConsultas, modifier = Modifier.fillMaxWidth()) { Text("Minhas Consultas") }
            Spacer(Modifier.height(8.dp))
            OutlinedButton(onClick = onEditarPerfil, modifier = Modifier.fillMaxWidth()) { Text("Editar Perfil") }
        }
    }
}