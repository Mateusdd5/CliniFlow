package com.cliniflow.app.model

data class ListaEspera(
    val id: String = "",
    val medicoId: String = "",
    val medicoNome: String = "",
    val pacienteId: String = "",
    val pacienteNome: String = "",
    val especialidade: String = "",
    val data: String = "",
    val hora: String = "",
    val timestamp: Long = System.currentTimeMillis()
)