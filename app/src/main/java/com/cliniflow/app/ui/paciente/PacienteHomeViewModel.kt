package com.cliniflow.app.ui.paciente

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cliniflow.app.model.Consulta
import com.cliniflow.app.model.Usuario
import com.cliniflow.app.repository.AuthRepository
import com.cliniflow.app.repository.ConsultaRepository
import com.cliniflow.app.repository.UsuarioRepository
import kotlinx.coroutines.launch

class PacienteHomeViewModel : ViewModel() {

    private val authRepository = AuthRepository()
    private val usuarioRepository = UsuarioRepository()
    private val consultaRepository = ConsultaRepository()

    var usuario by mutableStateOf<Usuario?>(null)
        private set
    var proximaConsulta by mutableStateOf<Consulta?>(null)
        private set
    var carregando by mutableStateOf(true)
        private set

    fun carregarDados() {
        val uid = authRepository.usuarioAtualId() ?: return
        carregando = true
        viewModelScope.launch {
            usuario = usuarioRepository.buscarPorId(uid)
            proximaConsulta = consultaRepository.listarPorPaciente(uid)
                .filter { it.status == "pendente" || it.status == "confirmada" }
                .minByOrNull { it.data + it.hora }
            carregando = false
        }
    }

    fun logout() = authRepository.logout()
}