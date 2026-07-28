package com.cliniflow.app.model

data class Usuario(
    val uid: String = "",
    val nome: String = "",
    val sobrenome: String = "",
    val dataNascimento: String = "",
    val cpf: String = "",
    val email: String = "",
    val sexo: String = "", // "M" ou "F"
    val tipo: String = "", // "paciente", "medico" ou "admin"
    val crm: String? = null, // só preenchido se tipo == "medico"
    val especialidade: String? = null, // só preenchido se tipo == "medico"
    val ativo: Boolean = true
)