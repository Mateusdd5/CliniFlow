package com.cliniflow.app.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cliniflow.app.model.Usuario

private val especialidadesDisponiveis = listOf("Ortopedia", "Fisiatria", "Reumatologia")

@Composable
fun CadastroScreen(
    viewModel: AuthViewModel = viewModel(),
    onCadastroSucesso: (Usuario) -> Unit,
    onVoltarParaLogin: () -> Unit
) {
    var tipo by remember { mutableStateOf("paciente") }
    var nome by remember { mutableStateOf("") }
    var sobrenome by remember { mutableStateOf("") }
    var dataNascimento by remember { mutableStateOf("") }
    var cpf by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var sexo by remember { mutableStateOf("M") }
    var crm by remember { mutableStateOf("") }
    var especialidade by remember { mutableStateOf(especialidadesDisponiveis.first()) }

    LaunchedEffect(viewModel.usuarioLogado) {
        viewModel.usuarioLogado?.let { onCadastroSucesso(it) }
    }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Criar Conta", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        Row {
            FilterChip(selected = tipo == "paciente", onClick = { tipo = "paciente" }, label = { Text("Paciente") })
            Spacer(Modifier.width(8.dp))
            FilterChip(selected = tipo == "medico", onClick = { tipo = "medico" }, label = { Text("Médico") })
        }
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(nome, { nome = it }, label = { Text("Nome") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(sobrenome, { sobrenome = it }, label = { Text("Sobrenome") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(dataNascimento, { dataNascimento = it }, label = { Text("Data de Nascimento") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(cpf, { cpf = it }, label = { Text("CPF") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(email, { email = it }, label = { Text("E-mail") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(senha, { senha = it }, label = { Text("Senha") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())

        if (tipo == "medico") {
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(crm, { crm = it }, label = { Text("CRM") }, modifier = Modifier.fillMaxWidth())

            Spacer(Modifier.height(12.dp))
            Text("Especialidade", style = MaterialTheme.typography.labelLarge)
            Spacer(Modifier.height(4.dp))
            Row {
                especialidadesDisponiveis.forEach { opcao ->
                    FilterChip(
                        selected = especialidade == opcao,
                        onClick = { especialidade = opcao },
                        label = { Text(opcao) },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Sexo:")
            RadioButton(selected = sexo == "M", onClick = { sexo = "M" }); Text("M")
            RadioButton(selected = sexo == "F", onClick = { sexo = "F" }); Text("F")
        }

        viewModel.erro?.let {
            Spacer(Modifier.height(8.dp))
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(24.dp))
        Button(
            onClick = {
                val usuario = Usuario(
                    nome = nome, sobrenome = sobrenome, dataNascimento = dataNascimento,
                    cpf = cpf, email = email, sexo = sexo, tipo = tipo,
                    crm = if (tipo == "medico") crm else null,
                    especialidade = if (tipo == "medico") especialidade else null
                )
                viewModel.cadastrar(usuario, senha)
            },
            enabled = !viewModel.carregando,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (viewModel.carregando) CircularProgressIndicator(Modifier.size(20.dp), color = MaterialTheme.colorScheme.onPrimary)
            else Text("Concluir Cadastro")
        }

        Spacer(Modifier.height(16.dp))
        TextButton(onClick = onVoltarParaLogin) { Text("Já tem conta? Entrar") }
    }
}