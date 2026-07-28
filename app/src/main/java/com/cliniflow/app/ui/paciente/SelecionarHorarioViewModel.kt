package com.cliniflow.app.ui.paciente

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cliniflow.app.model.Consulta
import com.cliniflow.app.model.ListaEspera
import com.cliniflow.app.model.Usuario
import com.cliniflow.app.repository.AuthRepository
import com.cliniflow.app.repository.ConsultaRepository
import com.cliniflow.app.repository.DisponibilidadeRepository
import com.cliniflow.app.repository.ListaEsperaRepository
import com.cliniflow.app.repository.UsuarioRepository
import kotlinx.coroutines.launch

class SelecionarHorarioViewModel : ViewModel() {

    private val authRepository = AuthRepository()
    private val usuarioRepository = UsuarioRepository()
    private val consultaRepository = ConsultaRepository()
    private val listaEsperaRepository = ListaEsperaRepository()
    private val disponibilidadeRepository = DisponibilidadeRepository()

    private lateinit var medico: Usuario

    var dataSelecionada by mutableStateOf("")
        private set
    var horariosDisponiveis by mutableStateOf<List<String>>(emptyList())
        private set
    var horariosOcupados by mutableStateOf<Set<String>>(emptySet())
        private set
    var carregandoHorarios by mutableStateOf(false)
        private set
    var horarioParaFila by mutableStateOf<String?>(null)
        private set
    var mensagem by mutableStateOf<String?>(null)
        private set
    var agendamentoConcluido by mutableStateOf(false)
        private set
    var entrouNaFila by mutableStateOf(false)
        private set

    fun iniciar(medicoSelecionado: Usuario) { medico = medicoSelecionado }

    fun selecionarData(data: String) {
        dataSelecionada = data
        horarioParaFila = null
        mensagem = null
        carregandoHorarios = true
        viewModelScope.launch {
            horariosDisponiveis = disponibilidadeRepository.buscar(medico.uid, data)
            val consultasDoDia = consultaRepository.listarPorMedicoEData(medico.uid, data)
            horariosOcupados = consultasDoDia.map { it.hora }.toSet()
            carregandoHorarios = false
        }
    }

    fun selecionarHorarioOcupado(hora: String) { horarioParaFila = hora }
    fun cancelarSelecaoFila() { horarioParaFila = null }

    fun agendar(hora: String) {
        val uid = authRepository.usuarioAtualId() ?: return
        viewModelScope.launch {
            val usuarioAtual = usuarioRepository.buscarPorId(uid) ?: return@launch
            val consulta = Consulta(
                pacienteId = uid,
                pacienteNome = "${usuarioAtual.nome} ${usuarioAtual.sobrenome}",
                medicoId = medico.uid,
                medicoNome = "${medico.nome} ${medico.sobrenome}",
                especialidade = medico.especialidade ?: "",
                data = dataSelecionada,
                hora = hora
            )
            consultaRepository.agendar(consulta)
                .onSuccess { agendamentoConcluido = true }
                .onFailure {
                    mensagem = "Esse horário acabou de ser ocupado. Escolha outro."
                    selecionarData(dataSelecionada)
                }
        }
    }

    fun confirmarEntradaNaFila() {
        val hora = horarioParaFila ?: return
        val uid = authRepository.usuarioAtualId() ?: return
        viewModelScope.launch {
            val usuarioAtual = usuarioRepository.buscarPorId(uid) ?: return@launch
            val item = ListaEspera(
                medicoId = medico.uid,
                medicoNome = "${medico.nome} ${medico.sobrenome}",
                pacienteId = uid,
                pacienteNome = "${usuarioAtual.nome} ${usuarioAtual.sobrenome}",
                especialidade = medico.especialidade ?: "",
                data = dataSelecionada,
                hora = hora
            )
            listaEsperaRepository.entrarNaFila(item)
                .onSuccess { entrouNaFila = true }
                .onFailure { mensagem = "Erro ao entrar na lista de espera." }
        }
    }
}