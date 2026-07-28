package com.cliniflow.app.ui.paciente

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cliniflow.app.model.Usuario
import com.cliniflow.app.repository.UsuarioRepository
import kotlinx.coroutines.launch

class BuscarMedicosViewModel : ViewModel() {

    private val usuarioRepository = UsuarioRepository()

    var medicos by mutableStateOf<List<Usuario>>(emptyList())
        private set
    var especialidadeSelecionada by mutableStateOf<String?>(null)
        private set
    var termoBusca by mutableStateOf("")
        private set
    var carregando by mutableStateOf(true)
        private set

    val especialidades: List<String>
        get() = medicos.mapNotNull { it.especialidade }.distinct().sorted()

    val medicosFiltrados: List<Usuario>
        get() = medicos.filter { medico ->
            medico.ativo &&
                    (especialidadeSelecionada == null || medico.especialidade == especialidadeSelecionada) &&
                    (termoBusca.isBlank() ||
                            medico.nome.contains(termoBusca, ignoreCase = true) ||
                            medico.sobrenome.contains(termoBusca, ignoreCase = true))
        }

    init {
        viewModelScope.launch {
            medicos = usuarioRepository.listarPorTipo("medico")
            especialidadeSelecionada = medicos.mapNotNull { it.especialidade }.distinct().sorted().firstOrNull()
            carregando = false
        }
    }

    fun selecionarEspecialidade(especialidade: String) { especialidadeSelecionada = especialidade }
    fun atualizarBusca(texto: String) { termoBusca = texto }
}