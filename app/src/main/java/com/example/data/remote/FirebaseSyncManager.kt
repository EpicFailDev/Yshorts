package com.example.data.remote

import android.content.Context
import android.util.Log
import com.example.domain.model.ShortsScript
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirebaseSyncManager(private val context: Context) {

    private val auth: FirebaseAuth? by lazy {
        try {
            FirebaseAuth.getInstance()
        } catch (e: Throwable) {
            Log.w("FirebaseSync", "FirebaseAuth not initialized: ${e.message}")
            null
        }
    }

    private val firestore: FirebaseFirestore? by lazy {
        try {
            FirebaseFirestore.getInstance()
        } catch (e: Throwable) {
            Log.w("FirebaseSync", "FirebaseFirestore not initialized: ${e.message}")
            null
        }
    }

    val currentUser: FirebaseUser?
        get() = auth?.currentUser

    val authState: Flow<FirebaseUser?> = callbackFlow {
        val authInstance = auth
        if (authInstance == null) {
            trySend(null)
            close()
            return@callbackFlow
        }
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            trySend(firebaseAuth.currentUser)
        }
        authInstance.addAuthStateListener(listener)
        awaitClose {
            authInstance.removeAuthStateListener(listener)
        }
    }

    suspend fun signInAnonymously(): Result<FirebaseUser?> = withContext(Dispatchers.IO) {
        val authInstance = auth ?: return@withContext Result.failure(IllegalStateException("Firebase Auth indisponível"))
        try {
            val result = authInstance.signInAnonymously().await()
            Result.success(result.user)
        } catch (e: Exception) {
            Log.e("FirebaseSync", "Erro no login anônimo", e)
            Result.failure(e)
        }
    }

    suspend fun signInWithEmail(email: String, pass: String): Result<FirebaseUser?> = withContext(Dispatchers.IO) {
        val authInstance = auth ?: return@withContext Result.failure(IllegalStateException("Firebase Auth indisponível"))
        try {
            val result = try {
                authInstance.signInWithEmailAndPassword(email, pass).await()
            } catch (e: Exception) {
                authInstance.createUserWithEmailAndPassword(email, pass).await()
            }
            Result.success(result.user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun signOut() {
        auth?.signOut()
    }

    suspend fun syncScriptToFirestore(script: ShortsScript): Result<String> = withContext(Dispatchers.IO) {
        val firestoreInstance = firestore ?: return@withContext Result.failure(IllegalStateException("Firestore indisponível"))
        val user = currentUser ?: return@withContext Result.failure(IllegalStateException("Usuário não autenticado"))

        try {
            val docRef = if (!script.firestoreId.isNullOrBlank()) {
                firestoreInstance.collection("users")
                    .document(user.uid)
                    .collection("shorts_scripts")
                    .document(script.firestoreId)
            } else {
                firestoreInstance.collection("users")
                    .document(user.uid)
                    .collection("shorts_scripts")
                    .document()
            }

            val data = hashMapOf(
                "title" to script.title,
                "category" to script.category,
                "hook" to script.hook,
                "bodyContent" to script.bodyContent,
                "callToAction" to script.callToAction,
                "visualNotes" to script.visualNotes,
                "targetDurationSeconds" to script.targetDurationSeconds,
                "status" to script.status.label,
                "tags" to script.tags,
                "aiSummary" to script.aiSummary,
                "createdAt" to script.createdAt,
                "updatedAt" to script.updatedAt,
                "isFavorite" to script.isFavorite
            )

            docRef.set(data, SetOptions.merge()).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Log.e("FirebaseSync", "Erro ao sincronizar com Firestore", e)
            Result.failure(e)
        }
    }

    suspend fun fetchScriptsFromFirestore(): Result<List<ShortsScript>> = withContext(Dispatchers.IO) {
        val firestoreInstance = firestore ?: return@withContext Result.failure(IllegalStateException("Firestore indisponível"))
        val user = currentUser ?: return@withContext Result.failure(IllegalStateException("Usuário não autenticado"))

        try {
            val snapshot = firestoreInstance.collection("users")
                .document(user.uid)
                .collection("shorts_scripts")
                .get()
                .await()

            val list = snapshot.documents.mapNotNull { doc ->
                val data = doc.data ?: return@mapNotNull null
                ShortsScript(
                    id = 0, // Assigned by Room
                    title = data["title"] as? String ?: "",
                    category = data["category"] as? String ?: "Geral",
                    hook = data["hook"] as? String ?: "",
                    bodyContent = data["bodyContent"] as? String ?: "",
                    callToAction = data["callToAction"] as? String ?: "",
                    visualNotes = data["visualNotes"] as? String ?: "",
                    targetDurationSeconds = (data["targetDurationSeconds"] as? Number)?.toInt() ?: 45,
                    status = com.example.domain.model.ScriptStatus.fromLabel(data["status"] as? String ?: ""),
                    tags = data["tags"] as? String ?: "",
                    aiSummary = data["aiSummary"] as? String ?: "",
                    createdAt = (data["createdAt"] as? Number)?.toLong() ?: System.currentTimeMillis(),
                    updatedAt = (data["updatedAt"] as? Number)?.toLong() ?: System.currentTimeMillis(),
                    isFavorite = data["isFavorite"] as? Boolean ?: false,
                    firestoreId = doc.id
                )
            }
            Result.success(list)
        } catch (e: Exception) {
            Log.e("FirebaseSync", "Erro ao buscar scripts do Firestore", e)
            Result.failure(e)
        }
    }
}
