package com.cliniflow.app.ui.medico

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cliniflow.app.model.Consulta
import com.cliniflow.app.repository.AuthRepository
import com.cliniflow.app.repository.ConsultaRepository
import kotlinx.coroutines.launch

class MedicoConsultasViewModel : ViewModel() {

    private val authRepository = AuthRepository()
    private val consultaRepository = ConsultaRepository()

    var consultasAtivas by mutableStateOf<List<Consulta>>(emptyList())
        private set
    var consultasHistorico by mutableStateOf<List<Consulta>>(emptyList())
        private set
    var carregando by mutableStateOf(true)
        private set

    fun carregar() {
        val uid = authRepository.usuarioAtualId() ?: return
        carregando = true
        viewModelScope.launch {
            val todas = consultaRepository.listarPorMedico(uid)
            consultasAtivas = todas.filter { it.status == "pendente" || it.status == "confirmada" }
                .sortedBy { it.data + it.hora }
            consultasHistorico = todas.filter { it.status == "realizada" || it.status == "cancelada" }
                .sortedByDescending { it.data + it.hora }
            carregando = false
        }
    }

    fun cancelarConsulta(consultaId: String) {
        viewModelScope.launch {
            consultaRepository.cancelar(consultaId)
            carregar()
        }
    }
}