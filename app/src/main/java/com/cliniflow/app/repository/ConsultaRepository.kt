package com.cliniflow.app.repository

import com.cliniflow.app.model.Consulta
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ConsultaRepository {

    private val colecao = FirebaseFirestore.getInstance().collection("consultas")

    suspend fun horarioOcupado(medicoId: String, data: String, hora: String): Boolean {
        val resultado = colecao
            .whereEqualTo("medicoId", medicoId)
            .whereEqualTo("data", data)
            .whereEqualTo("hora", hora)
            .whereIn("status", listOf("pendente", "confirmada"))
            .get()
            .await()
        return !resultado.isEmpty
    }

    suspend fun agendar(consulta: Consulta): Result<String> {
        return try {
            if (horarioOcupado(consulta.medicoId, consulta.data, consulta.hora)) {
                return Result.failure(Exception("HORARIO_OCUPADO"))
            }
            val docRef = colecao.document()
            docRef.set(consulta.copy(id = docRef.id)).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun listarPorMedicoEData(medicoId: String, data: String): List<Consulta> = try {
        colecao.whereEqualTo("medicoId", medicoId)
            .whereEqualTo("data", data)
            .whereIn("status", listOf("pendente", "confirmada"))
            .get().await().toObjects(Consulta::class.java)
    } catch (e: Exception) { emptyList() }

    suspend fun listarPorPaciente(pacienteId: String): List<Consulta> = try {
        colecao.whereEqualTo("pacienteId", pacienteId).get().await().toObjects(Consulta::class.java)
    } catch (e: Exception) { emptyList() }

    suspend fun listarPorMedico(medicoId: String): List<Consulta> = try {
        colecao.whereEqualTo("medicoId", medicoId).get().await().toObjects(Consulta::class.java)
    } catch (e: Exception) { emptyList() }

    suspend fun listarTodas(): List<Consulta> = try {
        colecao.get().await().toObjects(Consulta::class.java)
    } catch (e: Exception) { emptyList() }

    suspend fun cancelar(consultaId: String): Result<Unit> = try {
        colecao.document(consultaId).update("status", "cancelada").await()
        Result.success(Unit)
    } catch (e: Exception) { Result.failure(e) }

    suspend fun confirmar(consultaId: String): Result<Unit> = try {
        colecao.document(consultaId).update("status", "confirmada").await()
        Result.success(Unit)
    } catch (e: Exception) { Result.failure(e) }

    suspend fun marcarComoRealizada(consultaId: String): Result<Unit> = try {
        colecao.document(consultaId).update("status", "realizada").await()
        Result.success(Unit)
    } catch (e: Exception) { Result.failure(e) }
}