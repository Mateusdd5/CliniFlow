package com.cliniflow.app.ui.paciente

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Accessibility
import androidx.compose.material.icons.outlined.EventAvailable
import androidx.compose.material.icons.outlined.Healing
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.SelfImprovement
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

private val especialidadesAtalho = listOf("Ortopedia", "Fisiatria", "Reumatologia")

private fun iconePara(especialidade: String): ImageVector = when (especialidade) {
    "Ortopedia" -> Icons.Outlined.Accessibility
    "Fisiatria" -> Icons.Outlined.SelfImprovement
    "Reumatologia" -> Icons.Outlined.Healing
    else -> Icons.Outlined.MedicalServices
}

@Composable
fun PacienteHomeScreen(
    viewModel: PacienteHomeViewModel = viewModel(),
    onNovoAgendamento: () -> Unit,
    onEspecialidadeSelecionada: (String) -> Unit,
    onMinhasConsultas: () -> Unit,
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
                        text = (viewModel.usuario?.nome?.firstOrNull() ?: ' ').uppercase(),
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
                Spacer(Modifier.width(12.dp))
                Text(
                    "Olá, ${viewModel.usuario?.nome ?: ""}!",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            TextButton(onClick = { viewModel.logout(); onLogout() }) {
                Text("Sair", color = MaterialTheme.colorScheme.onPrimary)
            }
        }

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            if (viewModel.carregando) {
                Box(Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
                return@Column
            }

            val consulta = viewModel.proximaConsulta
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.EventAvailable,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            "Próxima Consulta",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        if (consulta != null) {
                            Text(
                                consulta.medicoNome,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                "${consulta.data}, ${consulta.hora}",
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        } else {
                            Text(
                                "Nenhuma consulta agendada",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))
            Text("Especialidades", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                especialidadesAtalho.forEach { especialidade ->
                    Column(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .clickable { onEspecialidadeSelecionada(especialidade) }
                            .padding(vertical = 14.dp, horizontal = 12.dp)
                            .width(88.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            iconePara(especialidade),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            especialidade,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
            Button(onClick = onNovoAgendamento, modifier = Modifier.fillMaxWidth().height(48.dp)) {
                Text("+ Novo Agendamento")
            }
            Spacer(Modifier.height(12.dp))
            OutlinedButton(onClick = onMinhasConsultas, modifier = Modifier.fillMaxWidth()) { Text("Minhas Consultas") }
            Spacer(Modifier.height(12.dp))
            OutlinedButton(onClick = onEditarPerfil, modifier = Modifier.fillMaxWidth()) { Text("Editar Perfil") }
        }
    }
}