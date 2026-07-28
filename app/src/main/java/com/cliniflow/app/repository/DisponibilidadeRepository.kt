package com.cliniflow.app.repository

import com.cliniflow.app.model.Disponibilidade
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class DisponibilidadeRepository {

    private val colecao = FirebaseFirestore.getInstance().collection("disponibilidade")

    private fun idPara(medicoId: String, data: String) = "${medicoId}_$data"

    suspend fun salvar(medicoId: String, data: String, horarios: List<String>): Result<Unit> = try {
        val id = idPara(medicoId, data)
        colecao.document(id).set(Disponibilidade(id, medicoId, data, horarios)).await()
        Result.success(Unit)
    } catch (e: Exception) { Result.failure(e) }

    suspend fun buscar(medicoId: String, data: String): List<String> = try {
        colecao.document(idPara(medicoId, data)).get().await()
            .toObject(Disponibilidade::class.java)?.horarios ?: emptyList()
    } catch (e: Exception) { emptyList() }
}