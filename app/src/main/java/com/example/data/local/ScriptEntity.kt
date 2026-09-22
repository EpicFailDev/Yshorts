package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.model.ScriptStatus
import com.example.domain.model.ShortsScript

@Entity(tableName = "scripts")
data class ScriptEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val category: String,
    val hook: String,
    val bodyContent: String,
    val callToAction: String,
    val visualNotes: String,
    val targetDurationSeconds: Int,
    val status: String,
    val tags: String,
    val aiSummary: String,
    val createdAt: Long,
    val updatedAt: Long,
    val isFavorite: Boolean,
    val firestoreId: String? = null
) {
    fun toDomain(): ShortsScript {
        return ShortsScript(
            id = id,
            title = title,
            category = category,
            hook = hook,
            bodyContent = bodyContent,
            callToAction = callToAction,
            visualNotes = visualNotes,
            targetDurationSeconds = targetDurationSeconds,
            status = ScriptStatus.fromLabel(status),
            tags = tags,
            aiSummary = aiSummary,
            createdAt = createdAt,
            updatedAt = updatedAt,
            isFavorite = isFavorite,
            firestoreId = firestoreId
        )
    }

    companion object {
        fun fromDomain(script: ShortsScript): ScriptEntity {
            return ScriptEntity(
                id = script.id,
                title = script.title,
                category = script.category,
                hook = script.hook,
                bodyContent = script.bodyContent,
                callToAction = script.callToAction,
                visualNotes = script.visualNotes,
                targetDurationSeconds = script.targetDurationSeconds,
                status = script.status.label,
                tags = script.tags,
                aiSummary = script.aiSummary,
                createdAt = script.createdAt,
                updatedAt = script.updatedAt,
                isFavorite = script.isFavorite,
                firestoreId = script.firestoreId
            )
        }
    }
}
