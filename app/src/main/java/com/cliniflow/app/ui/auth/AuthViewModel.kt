package com.cliniflow.app.ui.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cliniflow.app.model.Usuario
import com.cliniflow.app.repository.AuthRepository
import com.cliniflow.app.repository.UsuarioRepository
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val authRepository = AuthRepository()
    private val usuarioRepository = UsuarioRepository()

    var carregando by mutableStateOf(false)
        private set
    var erro by mutableStateOf<String?>(null)
        private set
    var usuarioLogado by mutableStateOf<Usuario?>(null)
        private set

    fun login(email: String, senha: String) {
        if (email.isBlank() || senha.isBlank()) {
            erro = "Preencha e-mail e senha"
            return
        }
        carregando = true
        erro = null
        viewModelScope.launch {
            authRepository.login(email, senha)
                .onSuccess { uid ->
                    val usuario = usuarioRepository.buscarPorId(uid)
                    when {
                        usuario == null -> erro = "Usuário autenticado, mas dados não encontrados"
                        !usuario.ativo -> {
                            authRepository.logout()
                            erro = "Sua conta foi desativada. Entre em contato com a administração."
                        }
                        else -> usuarioLogado = usuario
                    }
                }
                .onFailure { erro = "E-mail ou senha inválidos" }
            carregando = false
        }
    }

    fun cadastrar(usuario: Usuario, senha: String) {
        if (usuario.nome.isBlank() || usuario.email.isBlank() || senha.isBlank()) {
            erro = "Preencha todos os campos obrigatórios"
            return
        }
        carregando = true
        erro = null
        viewModelScope.launch {
            authRepository.cadastrar(usuario, senha)
                .onSuccess { uid -> usuarioLogado = usuario.copy(uid = uid) }
                .onFailure { erro = it.message ?: "Erro ao cadastrar" }
            carregando = false
        }
    }
}