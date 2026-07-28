package com.cliniflow.app.ui.admin

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
fun AdminHomeScreen(
    viewModel: AdminHomeViewModel = viewModel(),
    onVerUsuarios: () -> Unit,
    onVerHistorico: () -> Unit,
    onEditarPerfil: () -> Unit,
    onLogout: () -> Unit
) {
    LaunchedEffect(Unit) { viewModel.carregarDados() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Painel Admin") },
                actions = { TextButton(onClick = { viewModel.logout(); onLogout() }) { Text("Sair") } }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            if (viewModel.carregando) {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
                return@Column
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                EstatisticaCard("Pacientes", viewModel.totalPacientes, Modifier.weight(1f))
                Spacer(Modifier.width(8.dp))
                EstatisticaCard("Médicos", viewModel.totalMedicos, Modifier.weight(1f))
            }
            Spacer(Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                EstatisticaCard("Consultas hoje", viewModel.consultasHoje, Modifier.weight(1f))
                Spacer(Modifier.width(8.dp))
                EstatisticaCard("Listas de espera", viewModel.totalListasEspera, Modifier.weight(1f))
            }

            Spacer(Modifier.height(24.dp))
            Text("Gerenciar", style = MaterialTheme.typography.labelLarge)
            Spacer(Modifier.height(8.dp))

            Button(onClick = onVerUsuarios, modifier = Modifier.fillMaxWidth()) { Text("Ver Usuários") }
            Spacer(Modifier.height(8.dp))
            OutlinedButton(onClick = onVerHistorico, modifier = Modifier.fillMaxWidth()) { Text("Histórico de Consultas") }
            Spacer(Modifier.height(8.dp))
            OutlinedButton(onClick = onEditarPerfil, modifier = Modifier.fillMaxWidth()) { Text("Editar Perfil") }
        }
    }
}

@Composable
private fun EstatisticaCard(titulo: String, valor: Int, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(Modifier.padding(16.dp)) {
            Text("$valor", style = MaterialTheme.typography.headlineMedium)
            Text(titulo, style = MaterialTheme.typography.bodySmall)
        }
    }
}