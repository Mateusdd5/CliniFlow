package com.cliniflow.app.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cliniflow.app.ui.components.Avatar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarPerfilAdminScreen(
    viewModel: EditarPerfilAdminViewModel = viewModel(),
    onVoltar: () -> Unit,
    onLogout: () -> Unit
) {
    LaunchedEffect(Unit) { viewModel.carregar() }

    var nome by remember { mutableStateOf("") }
    var sobrenome by remember { mutableStateOf("") }
    var dataNascimento by remember { mutableStateOf("") }
    var sexo by remember { mutableStateOf("M") }

    LaunchedEffect(viewModel.usuario) {
        viewModel.usuario?.let {
            nome = it.nome; sobrenome = it.sobrenome
            dataNascimento = it.dataNascimento; sexo = it.sexo
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Perfil") },
                navigationIcon = { TextButton(onClick = onVoltar) { Text("‹ Voltar") } }
            )
        }
    ) { padding ->
        if (viewModel.carregando) {
            Box(Modifier.padding(padding).fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            return@Scaffold
        }

        Column(
            modifier = Modifier.padding(padding).fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                Avatar(nome = nome, sobrenome = sobrenome, tamanho = 84.dp)
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            OutlinedTextField(nome, { nome = it }, label = { Text("Nome") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(sobrenome, { sobrenome = it }, label = { Text("Sobrenome") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(dataNascimento, { dataNascimento = it }, label = { Text("Data de Nascimento") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(viewModel.usuario?.cpf ?: "", {}, label = { Text("CPF") }, enabled = false, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(viewModel.usuario?.email ?: "", {}, label = { Text("E-mail") }, enabled = false, modifier = Modifier.fillMaxWidth())

            Spacer(Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Sexo:")
                RadioButton(selected = sexo == "M", onClick = { sexo = "M" }); Text("M")
                RadioButton(selected = sexo == "F", onClick = { sexo = "F" }); Text("F")
            }

            viewModel.mensagem?.let {
                Spacer(Modifier.height(8.dp))
                Text(it, color = if (viewModel.salvoComSucesso) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error)
            }

            Spacer(Modifier.height(24.dp))
            Button(
                onClick = { viewModel.salvar(nome, sobrenome, dataNascimento, sexo) },
                enabled = !viewModel.salvando,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (viewModel.salvando) CircularProgressIndicator(Modifier.size(20.dp), color = MaterialTheme.colorScheme.onPrimary)
                else Text("Salvar Alterações")
            }

            Spacer(Modifier.height(12.dp))
            OutlinedButton(
                onClick = { viewModel.logout(); onLogout() },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                modifier = Modifier.fillMaxWidth()
            ) { Text("Sair da Conta") }
        }
    }
}