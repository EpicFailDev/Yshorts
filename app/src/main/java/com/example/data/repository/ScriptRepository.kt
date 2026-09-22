package com.example.data.repository

import com.example.data.local.ScriptDao
import com.example.data.local.ScriptEntity
import com.example.data.remote.FirebaseSyncManager
import com.example.domain.model.AdvancedSearchFilter
import com.example.domain.model.DateRangeFilter
import com.example.domain.model.ScriptStatus
import com.example.domain.model.ShortsScript
import com.example.domain.model.SortOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ScriptRepository(
    private val scriptDao: ScriptDao,
    private val firebaseSync: FirebaseSyncManager
) {

    fun getAllScripts(): Flow<List<ShortsScript>> {
        return scriptDao.getAllScripts().map { list -> list.map { it.toDomain() } }
    }

    fun searchScriptsAdvanced(filter: AdvancedSearchFilter): Flow<List<ShortsScript>> {
        return scriptDao.getAllScripts().map { list ->
            val domainList = list.map { it.toDomain() }
            val now = System.currentTimeMillis()

            domainList.filter { script ->
                // 1. Text Query Filter
                val matchesQuery = if (filter.query.isBlank()) {
                    true
                } else {
                    val q = filter.query.trim().lowercase()
                    script.title.lowercase().contains(q) ||
                    script.hook.lowercase().contains(q) ||
                    script.bodyContent.lowercase().contains(q) ||
                    script.visualNotes.lowercase().contains(q) ||
                    script.tags.lowercase().contains(q) ||
                    script.callToAction.lowercase().contains(q)
                }

                // 2. Category Filter
                val matchesCategory = if (filter.category.isBlank() || filter.category == "Todas" || filter.category == "Todos") {
                    true
                } else {
                    script.category.equals(filter.category, ignoreCase = true)
                }

                // 3. Tag Filter
                val matchesTag = if (filter.tag.isBlank()) {
                    true
                } else {
                    val scriptTags = script.tags.split(",").map { it.trim().lowercase() }
                    scriptTags.any { it.contains(filter.tag.trim().lowercase()) }
                }

                // 4. Creation Date Filter
                val matchesDate = when (filter.dateRange) {
                    DateRangeFilter.ALL -> true
                    DateRangeFilter.TODAY -> script.createdAt >= (now - 24L * 60 * 60 * 1000)
                    DateRangeFilter.LAST_7_DAYS -> script.createdAt >= (now - 7L * 24 * 60 * 60 * 1000)
                    DateRangeFilter.LAST_30_DAYS -> script.createdAt >= (now - 30L * 24 * 60 * 60 * 1000)
                }

                matchesQuery && matchesCategory && matchesTag && matchesDate
            }.let { filtered ->
                // 5. Ordered Results
                when (filter.sortBy) {
                    SortOrder.CREATED_DESC -> filtered.sortedByDescending { it.createdAt }
                    SortOrder.CREATED_ASC -> filtered.sortedBy { it.createdAt }
                    SortOrder.TITLE_ASC -> filtered.sortedBy { it.title.lowercase() }
                    SortOrder.WORDS_DESC -> filtered.sortedByDescending { it.totalWords }
                    SortOrder.DURATION_DESC -> filtered.sortedByDescending { it.targetDurationSeconds }
                }
            }
        }
    }

    fun getAllTags(): Flow<List<String>> {
        return scriptDao.getAllScripts().map { list ->
            list.flatMap { entity ->
                entity.tags.split(",").map { it.trim() }
            }.filter { it.isNotBlank() }
            .distinct()
            .sorted()
        }
    }

    fun searchScripts(query: String, category: String?): Flow<List<ShortsScript>> {
        val flow = if (category.isNullOrBlank() || category == "Todos" || category == "Todas") {
            if (query.isBlank()) scriptDao.getAllScripts() else scriptDao.searchScripts(query)
        } else {
            if (query.isBlank()) scriptDao.getScriptsByCategory(category) else scriptDao.searchScriptsWithCategory(query, category)
        }
        return flow.map { list -> list.map { it.toDomain() } }
    }

    fun getAllCategories(): Flow<List<String>> {
        return scriptDao.getAllCategories()
    }

    suspend fun getScriptById(id: Long): ShortsScript? {
        return scriptDao.getScriptById(id)?.toDomain()
    }

    suspend fun insertScript(script: ShortsScript): Long {
        val entity = ScriptEntity.fromDomain(script)
        val id = scriptDao.insertScript(entity)

        // Asynchronously sync to Firestore if user is logged in
        if (firebaseSync.currentUser != null) {
            try {
                val firestoreId = firebaseSync.syncScriptToFirestore(script.copy(id = id)).getOrNull()
                if (firestoreId != null) {
                    scriptDao.updateScript(entity.copy(id = id, firestoreId = firestoreId))
                }
            } catch (_: Exception) {}
        }
        return id
    }

    suspend fun updateScript(script: ShortsScript) {
        val entity = ScriptEntity.fromDomain(script.copy(updatedAt = System.currentTimeMillis()))
        scriptDao.updateScript(entity)

        if (firebaseSync.currentUser != null) {
            try {
                firebaseSync.syncScriptToFirestore(script)
            } catch (_: Exception) {}
        }
    }

    suspend fun deleteScript(script: ShortsScript) {
        scriptDao.deleteById(script.id)
    }

    suspend fun syncWithCloud(): Result<Int> {
        val cloudScriptsResult = firebaseSync.fetchScriptsFromFirestore()
        if (cloudScriptsResult.isFailure) {
            return Result.failure(cloudScriptsResult.exceptionOrNull() ?: Exception("Falha ao sincronizar"))
        }

        val cloudScripts = cloudScriptsResult.getOrNull() ?: emptyList()
        var importedCount = 0
        cloudScripts.forEach { script ->
            val entity = ScriptEntity.fromDomain(script)
            scriptDao.insertScript(entity)
            importedCount++
        }
        return Result.success(importedCount)
    }

    suspend fun seedSampleScriptsIfEmpty() {
        // Pre-fill with top quality shorts scripts for YouTube creators
        val samples = listOf(
            ShortsScript(
                title = "O Truque Psicológico que te Faz Procrastinar",
                category = "Produtividade",
                hook = "Você não é preguiçoso. O seu cérebro só está caindo no maior golpe da dopamina barata.",
                bodyContent = "Toda vez que você precisa sentar para trabalhar e pega o celular, o seu cérebro não quer fugir do esforço, ele quer fugir da incerteza.\n\nExiste uma regra chamada Regra dos 2 Minutos: se você apenas abrir o documento e escrever uma linha, a barreira do atrito cai em 80%.\n\nFaça o teste hoje: marque no cronômetro 120 segundos.",
                callToAction = "Comente 'FOCO' se você vai testar isso hoje e já salva esse vídeo pra não esquecer!",
                visualNotes = "Corte rápido nos 0:02 com efeito sonoro 'whoosh'. Zoom in lento no rosto do apresentador. Texto na tela: 'REGRA DOS 2 MINUTOS' em vermelho.",
                targetDurationSeconds = 30,
                status = ScriptStatus.READY,
                tags = "produtividade, foco, shorts viral, psicologia",
                aiSummary = "Aborda a raiz neurológica da procrastinação e ensina a regra dos 2 minutos para quebrar o atrito inicial.",
                isFavorite = true
            ),
            ShortsScript(
                title = "3 Hacks Secretos para Dobrar a Bateria do Celular",
                category = "Tecnologia",
                hook = "Se o seu celular descarrega antes do fim do dia, pare de cometer esses 3 erros agora mesmo!",
                bodyContent = "Erro número 1: Deixar a busca por redes Wi-Fi e Bluetooth ativada em segundo plano. Desative isso em Ajustes de Localização.\n\nErro número 2: Usar papel de parede claro em telas OLED. Cores pretas puras desligam os pixels e economizam até 30% de carga.\n\nErro número 3: Fechar todos os aplicativos toda hora. Reabrir consome mais CPU do que mantê-los congelados na memória RAM.",
                callToAction = "Qual é a porcentagem da sua bateria agora? Deixe nos comentários e se inscreva para mais dicas!",
                visualNotes = "Close no smartphone mostrando a bateria em vermelho. Efeito de raio cortando a tela. Gráfico minimalista de economia.",
                targetDurationSeconds = 45,
                status = ScriptStatus.READY,
                tags = "bateria, dicas tech, celulares, hacks",
                aiSummary = "Lista três erros comuns de consumo de energia com foco em displays OLED e processos em segundo plano."
            ),
            ShortsScript(
                title = "Como Investir seus Primeiros R$ 100",
                category = "Finanças",
                hook = "A maioria das pessoas guarda dinheiro na poupança e perde poder de compra sem saber.",
                bodyContent = "Se você tem apenas 100 reais, a poupança rende menos que a inflação real.\n\nCom os mesmos 100 reais no Tesouro Selic ou em um CDB de liquidez diária a 100% do CDI, seu dinheiro rende todos os dias úteis com a máxima segurança do país.\n\nNão espere ter muito dinheiro para começar; comece para ter muito.",
                callToAction = "Você já investe ou ainda deixa na poupança? Comente aqui embaixo!",
                visualNotes = "Texto grande: 'POUPANÇA vs TESOURO'. Som de moeda caindo. Letreiros em amarelo e branco.",
                targetDurationSeconds = 35,
                status = ScriptStatus.DRAFT,
                tags = "investimentos, dinheiro, tesouro selic, educacao financeira",
                aiSummary = "Comparação direta entre poupança e Tesouro Selic com R$ 100 iniciais."
            )
        )

        samples.forEach { script ->
            scriptDao.insertScript(ScriptEntity.fromDomain(script))
        }
    }
}
