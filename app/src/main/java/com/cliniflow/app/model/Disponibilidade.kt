package com.cliniflow.app.model

data class Disponibilidade(
    val id: String = "",
    val medicoId: String = "",
    val data: String = "",
    val horarios: List<String> = emptyList()
)