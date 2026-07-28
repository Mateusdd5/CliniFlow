package com.cliniflow.app.model

data class Consulta(
    val id: String = "",
    val pacienteId: String = "",
    val pacienteNome: String = "",
    val medicoId: String = "",
    val medicoNome: String = "",
    val especialidade: String = "",
    val data: String = "",       // formato: "yyyy-MM-dd", ex: "2026-05-25"
    val hora: String = "",       // formato: "HH:mm", ex: "09:00"
    val status: String = "pendente" // "pendente", "confirmada", "realizada", "cancelada"
)