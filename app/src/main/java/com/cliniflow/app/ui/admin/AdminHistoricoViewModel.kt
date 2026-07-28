package com.cliniflow.app.ui.admin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cliniflow.app.model.Consulta
import com.cliniflow.app.model.ListaEspera
import com.cliniflow.app.repository.ConsultaRepository
import com.cliniflow.app.repository.ListaEsperaRepository
import kotlinx.coroutines.launch

class AdminHistoricoViewModel : ViewModel() {

    private val consultaRepository = ConsultaRepository()
    private val listaEsperaRepository = ListaEsperaRepository()

    var consultas by mutableStateOf<List<Consulta>>(emptyList())
        private set
    var filasDeEspera by mutableStateOf<List<ListaEspera>>(emptyList())
        private set
    var filtroStatus by mutableStateOf<String?>(null)
        private set
    var carregando by mutableStateOf(true)
        private set

    val consultasFiltradas: List<Consulta>
        get() = consultas
            .filter { filtroStatus == null || it.status == filtroStatus }
            .sortedByDescending { it.data + it.hora }

    fun carregar() {
        carregando = true
        viewModelScope.launch {
            consultas = consultaRepository.listarTodas()
            filasDeEspera = listaEsperaRepository.listarTodas()
            carregando = false
        }
    }

    fun selecionarFiltro(status: String?) { filtroStatus = status }
}