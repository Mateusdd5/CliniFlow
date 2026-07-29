package com.cliniflow.app.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.HourglassEmpty
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.Today
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AdminHomeScreen(
    viewModel: AdminHomeViewModel = viewModel(),
    onVerUsuarios: () -> Unit,
    onVerHistorico: () -> Unit,
    onEditarPerfil: () -> Unit,
    onLogout: () -> Unit
) {
    LaunchedEffect(Unit) { viewModel.carregarDados() }

    Column(modifier = Modifier.fillMaxSize()) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(horizontal = 20.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.18f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (viewModel.usuario?.nome?.firstOrNull() ?: 'A').uppercase(),
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        "Painel Admin",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        viewModel.usuario?.nome ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f)
                    )
                }
            }
            TextButton(onClick = { viewModel.logout(); onLogout() }) {
                Text("Sair", color = MaterialTheme.colorScheme.onPrimary)
            }
        }

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            if (viewModel.carregando) {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
                return@Column
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                EstatisticaCard(
                    "Pacientes", viewModel.totalPacientes, Icons.Outlined.Groups,
                    MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.onPrimaryContainer,
                    Modifier.weight(1f)
                )
                Spacer(Modifier.width(8.dp))
                EstatisticaCard(
                    "Médicos", viewModel.totalMedicos, Icons.Outlined.MedicalServices,
                    MaterialTheme.colorScheme.secondaryContainer, MaterialTheme.colorScheme.onSecondaryContainer,
                    Modifier.weight(1f)
                )
            }
            Spacer(Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                EstatisticaCard(
                    "Consultas hoje", viewModel.consultasHoje, Icons.Outlined.Today,
                    MaterialTheme.colorScheme.secondaryContainer, MaterialTheme.colorScheme.onSecondaryContainer,
                    Modifier.weight(1f)
                )
                Spacer(Modifier.width(8.dp))
                EstatisticaCard(
                    "Listas de espera", viewModel.totalListasEspera, Icons.Outlined.HourglassEmpty,
                    MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.onPrimaryContainer,
                    Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(24.dp))
            Text("Gerenciar", style = MaterialTheme.typography.labelLarge)
            Spacer(Modifier.height(8.dp))

            Button(onClick = onVerUsuarios, modifier = Modifier.fillMaxWidth().height(48.dp)) { Text("Ver Usuários") }
            Spacer(Modifier.height(8.dp))
            OutlinedButton(onClick = onVerHistorico, modifier = Modifier.fillMaxWidth()) { Text("Histórico de Consultas") }
            Spacer(Modifier.height(8.dp))
            OutlinedButton(onClick = onEditarPerfil, modifier = Modifier.fillMaxWidth()) { Text("Editar Perfil") }
        }
    }
}

@Composable
private fun EstatisticaCard(
    titulo: String,
    valor: Int,
    icone: ImageVector,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Column(Modifier.padding(16.dp)) {
            Icon(icone, contentDescription = null, tint = contentColor, modifier = Modifier.size(22.dp))
            Spacer(Modifier.height(8.dp))
            Text("$valor", style = MaterialTheme.typography.headlineMedium, color = contentColor)
            Text(titulo, style = MaterialTheme.typography.bodySmall, color = contentColor)
        }
    }
}