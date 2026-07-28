package com.cliniflow.app.ui.paciente

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
import com.cliniflow.app.model.Usuario

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuscarMedicosScreen(
    viewModel: BuscarMedicosViewModel = viewModel(),
    onMedicoSelecionado: (Usuario) -> Unit
) {
    Scaffold(topBar = { TopAppBar(title = { Text("Buscar Médicos") }) }) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)) {

            OutlinedTextField(
                value = viewModel.termoBusca,
                onValueChange = { viewModel.atualizarBusca(it) },
                label = { Text("Pesquisar por nome") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            if (viewModel.carregando) {
                Box(Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
                return@Column
            }

            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                viewModel.especialidades.forEach { especialidade ->
                    FilterChip(
                        selected = viewModel.especialidadeSelecionada == especialidade,
                        onClick = { viewModel.selecionarEspecialidade(especialidade) },
                        label = { Text(especialidade) },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }
            Spacer(Modifier.height(16.dp))

            if (viewModel.medicosFiltrados.isEmpty()) {
                Text("Nenhum médico encontrado.")
            } else {
                LazyColumn {
                    items(viewModel.medicosFiltrados) { medico ->
                        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                            Column(Modifier.padding(16.dp)) {
                                Text("Dr(a). ${medico.nome} ${medico.sobrenome}", style = MaterialTheme.typography.titleMedium)
                                Text(medico.especialidade ?: "", style = MaterialTheme.typography.bodyMedium)
                                Spacer(Modifier.height(8.dp))
                                Button(onClick = { onMedicoSelecionado(medico) }) { Text("Ver Agenda") }
                            }
                        }
                    }
                }
            }
        }
    }
}