package com.cliniflow.app.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cliniflow.app.model.Usuario
import com.cliniflow.app.ui.components.Avatar
import com.cliniflow.app.ui.components.BadgeTipo
import com.cliniflow.app.ui.components.StatusBadge

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminUsuariosScreen(
    viewModel: AdminUsuariosViewModel = viewModel(),
    onVoltar: () -> Unit
) {
    LaunchedEffect(Unit) { viewModel.carregar() }
    var abaSelecionada by remember { mutableStateOf(0) }
    val abas = listOf("Todos" to null, "Pacientes" to "paciente", "Médicos" to "medico")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Usuários") },
                navigationIcon = { TextButton(onClick = onVoltar) { Text("‹ Voltar") } }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {

            OutlinedTextField(
                value = viewModel.termoBusca,
                onValueChange = { viewModel.atualizarBusca(it) },
                label = { Text("Buscar por nome ou e-mail") },
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )

            viewModel.mensagemErro?.let {
                Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(horizontal = 16.dp))
                Spacer(Modifier.height(8.dp))
            }

            TabRow(selectedTabIndex = abaSelecionada) {
                abas.forEachIndexed { index, (titulo, tipo) ->
                    Tab(
                        selected = abaSelecionada == index,
                        onClick = { abaSelecionada = index; viewModel.selecionarFiltro(tipo) },
                        text = { Text(titulo) }
                    )
                }
            }

            if (viewModel.carregando) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
                return@Column
            }

            if (viewModel.usuariosFiltrados.isEmpty()) {
                Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) { Text("Nenhum usuário encontrado.") }
            } else {
                LazyColumn(modifier = Modifier.padding(16.dp)) {
                    items(viewModel.usuariosFiltrados) { usuario ->
                        UsuarioCard(usuario) { viewModel.alternarAtivo(usuario) }
                    }
                }
            }
        }
    }
}

@Composable
private fun UsuarioCard(usuario: Usuario, onAlternarAtivo: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                Avatar(nome = usuario.nome, sobrenome = usuario.sobrenome, tamanho = 44.dp)
                Spacer(Modifier.width(10.dp))
                Column {
                    Text("${usuario.nome} ${usuario.sobrenome}", style = MaterialTheme.typography.titleMedium)
                    Text(usuario.email, style = MaterialTheme.typography.bodySmall)
                    Text(
                        when (usuario.tipo) { "medico" -> "Médico"; "admin" -> "Admin"; else -> "Paciente" },
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                val (texto, tipoBadge) = if (usuario.ativo) "Ativo" to BadgeTipo.SUCESSO else "Inativo" to BadgeTipo.ERRO
                StatusBadge(texto = texto, tipo = tipoBadge)
                Spacer(Modifier.height(6.dp))
                TextButton(onClick = onAlternarAtivo) { Text(if (usuario.ativo) "Desativar" else "Ativar") }
            }
        }
    }
}