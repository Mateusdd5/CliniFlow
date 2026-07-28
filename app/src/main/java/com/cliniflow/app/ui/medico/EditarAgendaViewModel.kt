package com.cliniflow.app.ui.medico

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cliniflow.app.repository.AuthRepository
import com.cliniflow.app.repository.DisponibilidadeRepository
import kotlinx.coroutines.launch

class EditarAgendaViewModel : ViewModel() {

    private val authRepository = AuthRepository()
    private val disponibilidadeRepository = DisponibilidadeRepository()

    val horariosPadrao = listOf("09:00", "09:30", "10:00", "10:30", "14:00", "14:30", "15:00")

    var dataSelecionada by mutableStateOf("")
        private set
    var horariosSelecionados by mutableStateOf<Set<String>>(emptySet())
        private set
    var carregando by mutableStateOf(false)
        private set
    var salvando by mutableStateOf(false)
        private set
    var mensagem by mutableStateOf<String?>(null)
        private set

    fun selecionarData(data: String) {
        dataSelecionada = data
        mensagem = null
        carregando = true
        viewModelScope.launch {
            val uid = authRepository.usuarioAtualId()
            horariosSelecionados = if (uid != null) disponibilidadeRepository.buscar(uid, data).toSet() else emptySet()
            carregando = false
        }
    }

    fun alternarHorario(hora: String) {
        horariosSelecionados = if (hora in horariosSelecionados) horariosSelecionados - hora else horariosSelecionados + hora
    }

    fun salvarDisponibilidade() {
        val uid = authRepository.usuarioAtualId() ?: return
        if (dataSelecionada.isBlank()) return
        salvando = true
        viewModelScope.launch {
            disponibilidadeRepository.salvar(uid, dataSelecionada, horariosSelecionados.toList())
                .onSuccess { mensagem = "Disponibilidade salva para $dataSelecionada." }
                .onFailure { mensagem = "Erro ao salvar. Tente novamente." }
            salvando = false
        }
    }
}