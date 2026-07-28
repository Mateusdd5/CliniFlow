package com.cliniflow.app.ui.paciente

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cliniflow.app.model.Consulta
import com.cliniflow.app.model.ListaEspera
import com.cliniflow.app.repository.AuthRepository
import com.cliniflow.app.repository.ConsultaRepository
import com.cliniflow.app.repository.ListaEsperaRepository
import kotlinx.coroutines.launch

class MinhasConsultasViewModel : ViewModel() {

    private val authRepository = AuthRepository()
    private val consultaRepository = ConsultaRepository()
    private val listaEsperaRepository = ListaEsperaRepository()

    var consultasAtivas by mutableStateOf<List<Consulta>>(emptyList())
        private set
    var consultasHistorico by mutableStateOf<List<Consulta>>(emptyList())
        private set
    var filasDeEspera by mutableStateOf<List<ListaEspera>>(emptyList())
        private set
    var carregando by mutableStateOf(true)
        private set

    fun carregar() {
        val uid = authRepository.usuarioAtualId() ?: return
        carregando = true
        viewModelScope.launch {
            val todas = consultaRepository.listarPorPaciente(uid)
            consultasAtivas = todas.filter { it.status == "pendente" || it.status == "confirmada" }
                .sortedBy { it.data + it.hora }
            consultasHistorico = todas.filter { it.status == "realizada" || it.status == "cancelada" }
                .sortedByDescending { it.data + it.hora }
            filasDeEspera = listaEsperaRepository.listarPorPaciente(uid)
            carregando = false
        }
    }

    fun cancelarConsulta(consultaId: String) {
        viewModelScope.launch {
            consultaRepository.cancelar(consultaId)
            carregar()
        }
    }

    fun sairDaFila(itemId: String) {
        viewModelScope.launch {
            listaEsperaRepository.sairDaFila(itemId)
            carregar()
        }
    }
}