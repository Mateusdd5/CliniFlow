package com.cliniflow.app.repository

import com.cliniflow.app.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    suspend fun cadastrar(usuario: Usuario, senha: String): Result<String> {
        return try {
            val result = auth.createUserWithEmailAndPassword(usuario.email, senha).await()
            val uid = result.user?.uid ?: throw Exception("Erro ao criar usuário")

            val usuarioComId = usuario.copy(uid = uid)
            db.collection("usuarios").document(uid).set(usuarioComId).await()

            Result.success(uid)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, senha: String): Result<String> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, senha).await()
            Result.success(result.user?.uid ?: throw Exception("Erro ao fazer login"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() = auth.signOut()

    fun usuarioAtualId(): String? = auth.currentUser?.uid
}