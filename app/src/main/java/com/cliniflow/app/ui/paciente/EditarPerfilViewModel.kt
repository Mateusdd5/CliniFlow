package com.cliniflow.app.ui.paciente

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cliniflow.app.model.Usuario
import com.cliniflow.app.repository.AuthRepository
import com.cliniflow.app.repository.UsuarioRepository
import kotlinx.coroutines.launch

class EditarPerfilViewModel : ViewModel() {

    private val authRepository = AuthRepository()
    private val usuarioRepository = UsuarioRepository()

    var usuario by mutableStateOf<Usuario?>(null)
        private set
    var carregando by mutableStateOf(true)
        private set
    var salvando by mutableStateOf(false)
        private set
    var mensagem by mutableStateOf<String?>(null)
        private set
    var salvoComSucesso by mutableStateOf(false)
        private set

    fun carregar() {
        val uid = authRepository.usuarioAtualId() ?: return
        carregando = true
        viewModelScope.launch {
            usuario = usuarioRepository.buscarPorId(uid)
            carregando = false
        }
    }

    fun salvar(nome: String, sobrenome: String, dataNascimento: String, sexo: String) {
        val atual = usuario ?: return
        salvando = true
        mensagem = null
        viewModelScope.launch {
            val atualizado = atual.copy(nome = nome, sobrenome = sobrenome, dataNascimento = dataNascimento, sexo = sexo)
            usuarioRepository.atualizar(atualizado)
                .onSuccess {
                    usuario = atualizado
                    salvoComSucesso = true
                    mensagem = "Perfil atualizado com sucesso."
                }
                .onFailure { mensagem = "Erro ao salvar. Tente novamente." }
            salvando = false
        }
    }

    fun logout() = authRepository.logout()
}