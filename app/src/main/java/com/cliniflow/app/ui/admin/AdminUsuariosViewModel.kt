package com.cliniflow.app.ui.admin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cliniflow.app.model.Usuario
import com.cliniflow.app.repository.UsuarioRepository
import kotlinx.coroutines.launch

class AdminUsuariosViewModel : ViewModel() {

    private val usuarioRepository = UsuarioRepository()

    var usuarios by mutableStateOf<List<Usuario>>(emptyList())
        private set
    var filtroTipo by mutableStateOf<String?>(null)
        private set
    var termoBusca by mutableStateOf("")
        private set
    var carregando by mutableStateOf(true)
        private set
    var mensagemErro by mutableStateOf<String?>(null)
        private set

    val usuariosFiltrados: List<Usuario>
        get() = usuarios.filter { usuario ->
            (filtroTipo == null || usuario.tipo == filtroTipo) &&
                    (termoBusca.isBlank() ||
                            usuario.nome.contains(termoBusca, ignoreCase = true) ||
                            usuario.sobrenome.contains(termoBusca, ignoreCase = true) ||
                            usuario.email.contains(termoBusca, ignoreCase = true))
        }

    fun carregar() {
        carregando = true
        viewModelScope.launch {
            usuarios = usuarioRepository.listarTodos()
            carregando = false
        }
    }

    fun selecionarFiltro(tipo: String?) { filtroTipo = tipo }
    fun atualizarBusca(texto: String) { termoBusca = texto }

    fun alternarAtivo(usuario: Usuario) {
        mensagemErro = null
        viewModelScope.launch {
            usuarioRepository.alterarStatusAtivo(usuario.uid, !usuario.ativo)
                .onSuccess { carregar() }
                .onFailure { mensagemErro = "Erro ao atualizar status. Verifique sua permissão." }
        }
    }
}