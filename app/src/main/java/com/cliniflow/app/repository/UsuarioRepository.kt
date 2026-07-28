package com.cliniflow.app.repository

import com.cliniflow.app.model.Usuario
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UsuarioRepository {

    private val colecao = FirebaseFirestore.getInstance().collection("usuarios")

    suspend fun buscarPorId(uid: String): Usuario? = try {
        colecao.document(uid).get().await().toObject(Usuario::class.java)
    } catch (e: Exception) { null }

    suspend fun listarPorTipo(tipo: String): List<Usuario> = try {
        colecao.whereEqualTo("tipo", tipo).get().await().toObjects(Usuario::class.java)
    } catch (e: Exception) { emptyList() }

    suspend fun listarTodos(): List<Usuario> = try {
        colecao.get().await().toObjects(Usuario::class.java)
    } catch (e: Exception) { emptyList() }

    suspend fun atualizar(usuario: Usuario): Result<Unit> = try {
        colecao.document(usuario.uid).set(usuario).await()
        Result.success(Unit)
    } catch (e: Exception) { Result.failure(e) }

    suspend fun alterarStatusAtivo(uid: String, ativo: Boolean): Result<Unit> = try {
        colecao.document(uid).update("ativo", ativo).await()
        Result.success(Unit)
    } catch (e: Exception) { Result.failure(e) }
}