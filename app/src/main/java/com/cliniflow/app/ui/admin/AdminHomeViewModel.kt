package com.cliniflow.app.ui.admin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cliniflow.app.model.Usuario
import com.cliniflow.app.repository.AuthRepository
import com.cliniflow.app.repository.ConsultaRepository
import com.cliniflow.app.repository.ListaEsperaRepository
import com.cliniflow.app.repository.UsuarioRepository
import com.cliniflow.app.utils.hoje
import kotlinx.coroutines.launch

class AdminHomeViewModel : ViewModel() {

    private val authRepository = AuthRepository()
    private val usuarioRepository = UsuarioRepository()
    private val consultaRepository = ConsultaRepository()
    private val listaEsperaRepository = ListaEsperaRepository()

    var usuario by mutableStateOf<Usuario?>(null)
        private set
    var totalPacientes by mutableStateOf(0)
        private set
    var totalMedicos by mutableStateOf(0)
        private set
    var consultasHoje by mutableStateOf(0)
        private set
    var totalListasEspera by mutableStateOf(0)
        private set
    var carregando by mutableStateOf(true)
        private set

    fun carregarDados() {
        carregando = true
        viewModelScope.launch {
            val uid = authRepository.usuarioAtualId()
            if (uid != null) usuario = usuarioRepository.buscarPorId(uid)
            totalPacientes = usuarioRepository.listarPorTipo("paciente").size
            totalMedicos = usuarioRepository.listarPorTipo("medico").size
            val hojeStr = hoje()
            consultasHoje = consultaRepository.listarTodas().count { it.data == hojeStr }
            totalListasEspera = listaEsperaRepository.listarTodas().size
            carregando = false
        }
    }

    fun logout() = authRepository.logout()
}