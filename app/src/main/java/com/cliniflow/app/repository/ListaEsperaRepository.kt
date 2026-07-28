package com.cliniflow.app.repository

import com.cliniflow.app.model.ListaEspera
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class ListaEsperaRepository {

    private val colecao = FirebaseFirestore.getInstance().collection("listaEspera")

    suspend fun entrarNaFila(item: ListaEspera): Result<String> = try {
        val docRef = colecao.document()
        docRef.set(item.copy(id = docRef.id)).await()
        Result.success(docRef.id)
    } catch (e: Exception) { Result.failure(e) }

    suspend fun sairDaFila(id: String): Result<Unit> = try {
        colecao.document(id).delete().await()
        Result.success(Unit)
    } catch (e: Exception) { Result.failure(e) }

    // Ordenada por ordem de chegada — posição = índice na lista + 1
    suspend fun listarFila(medicoId: String, data: String, hora: String): List<ListaEspera> = try {
        colecao.whereEqualTo("medicoId", medicoId)
            .whereEqualTo("data", data)
            .whereEqualTo("hora", hora)
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .get().await().toObjects(ListaEspera::class.java)
    } catch (e: Exception) { emptyList() }

    suspend fun listarPorPaciente(pacienteId: String): List<ListaEspera> = try {
        colecao.whereEqualTo("pacienteId", pacienteId).get().await().toObjects(ListaEspera::class.java)
    } catch (e: Exception) { emptyList() }
}