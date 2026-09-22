package com.example.data.remote

import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

data class GeneratedShortsScript(
    val title: String,
    val category: String,
    val hook: String,
    val bodyContent: String,
    val callToAction: String,
    val visualNotes: String,
    val tags: String
)

data class ThemeSuggestion(
    val title: String,
    val hookIdea: String,
    val whyItWorks: String
)

class GeminiService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val mediaType = "application/json; charset=utf-8".toMediaType()
    private val modelName = "gemini-3.1-pro-preview"

    private val apiKey: String
        get() = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Throwable) {
            ""
        }

    fun isConfigured(): Boolean {
        return apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY"
    }

    suspend fun generateShortsScriptWithThinking(
        topicOrTrend: String,
        category: String,
        targetDurationSeconds: Int,
        tone: String = "Dinâmico e Direto"
    ): Result<GeneratedShortsScript> = withContext(Dispatchers.IO) {
        if (!isConfigured()) {
            return@withContext Result.failure(
                IllegalStateException("Chave da API Gemini não configurada. Configure GEMINI_API_KEY nos Secrets do AI Studio.")
            )
        }

        val systemPrompt = """
            Você é um especialista mundial em retenção e viralização de YouTube Shorts.
            Crie um roteiro completo, viciante e de alta retenção no formato estrito de YouTube Shorts.
            Tempo alvo: $targetDurationSeconds segundos (~${(targetDurationSeconds * 2.5).toInt()} palavras no total).
            Formato obrigatório:
            1. GANCHO (0 a 3s): Uma frase inicial impactante que faça o espectador parar o feed imediatamente.
            2. CORPO (3 a ${targetDurationSeconds - 10}s): Ritmo acelerado, cortes mentais a cada 3 segundos, sem enrolação.
            3. CTA (últimos 5 a 10s): Chamada para ação natural que incentive comentários ou compartilhamentos.
            4. NOTAS VISUAIS: Dicas de B-roll, textos na tela e efeitos sonoros.
            
            Retorne APENAS um JSON válido com esta estrutura exata:
            {
              "title": "Título chamativo do Shorts",
              "category": "$category",
              "hook": "Gancho inicial (0-3s)",
              "bodyContent": "Conteúdo principal do roteiro",
              "callToAction": "Chamada final para ação",
              "visualNotes": "Ideias de cortes visuais, textos piscando e B-roll",
              "tags": "shorts, viral, youtube"
            }
        """.trimIndent()

        val userPrompt = "Crie um roteiro de YouTube Shorts sobre: '$topicOrTrend'. Categoria: '$category'. Tom: '$tone'."

        try {
            val rawResponse = callGeminiWithThinking(systemPrompt, userPrompt)
            val jsonText = cleanJsonResponse(rawResponse)
            val json = JSONObject(jsonText)
            val script = GeneratedShortsScript(
                title = json.optString("title", "Shorts: $topicOrTrend"),
                category = json.optString("category", category),
                hook = json.optString("hook", ""),
                bodyContent = json.optString("bodyContent", ""),
                callToAction = json.optString("callToAction", ""),
                visualNotes = json.optString("visualNotes", ""),
                tags = json.optString("tags", "shorts")
            )
            Result.success(script)
        } catch (e: Exception) {
            Log.e("GeminiService", "Erro na geração do roteiro", e)
            Result.failure(e)
        }
    }

    suspend fun suggestTrendingThemesWithThinking(
        categoryOrNiche: String
    ): Result<List<ThemeSuggestion>> = withContext(Dispatchers.IO) {
        if (!isConfigured()) {
            return@withContext Result.failure(
                IllegalStateException("Chave da API Gemini não configurada.")
            )
        }

        val systemPrompt = """
            Você é um estrategista de conteúdo para YouTube Shorts focado em tendências de busca em alta e psicologia de retenção.
            Gere 5 ideias de temas virais com ganchos magnéticos para o nicho/categoria: '$categoryOrNiche'.
            
            Retorne APENAS um JSON no seguinte formato (array de objetos):
            [
              {
                "title": "Título magnético do Shorts",
                "hookIdea": "Gancho inicial irresistível para os primeiros 3 segundos",
                "whyItWorks": "Por que esse tema retém o público e explora buscas em alta"
              }
            ]
        """.trimIndent()

        val userPrompt = "Sugira 5 temas com alto potencial de busca e engajamento para Shorts sobre: $categoryOrNiche"

        try {
            val rawResponse = callGeminiWithThinking(systemPrompt, userPrompt)
            val jsonText = cleanJsonResponse(rawResponse)
            val jsonArray = JSONArray(jsonText)
            val suggestions = mutableListOf<ThemeSuggestion>()
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                suggestions.add(
                    ThemeSuggestion(
                        title = obj.optString("title"),
                        hookIdea = obj.optString("hookIdea"),
                        whyItWorks = obj.optString("whyItWorks")
                    )
                )
            }
            Result.success(suggestions)
        } catch (e: Exception) {
            Log.e("GeminiService", "Erro ao sugerir temas", e)
            Result.failure(e)
        }
    }

    suspend fun summarizeScriptWithThinking(
        scriptContent: String
    ): Result<String> = withContext(Dispatchers.IO) {
        if (!isConfigured()) {
            return@withContext Result.failure(
                IllegalStateException("Chave da API Gemini não configurada.")
            )
        }

        val systemPrompt = """
            Você é um editor sênior de vídeos verticais.
            Analise a transcrição/roteiro fornecido e crie um resumo executivo com:
            - Ideia central (1 frase)
            - 3 pontos-chave do conteúdo
            - Avaliação do gancho e ritmo de retenção
            Mantenha a resposta concisa e direta ao ponto.
        """.trimIndent()

        try {
            val response = callGeminiWithThinking(systemPrompt, scriptContent)
            Result.success(response.trim())
        } catch (e: Exception) {
            Log.e("GeminiService", "Erro ao resumir roteiro", e)
            Result.failure(e)
        }
    }

    /**
     * Executes the Gemini REST API call with gemini-3.1-pro-preview and thinkingLevel = "high".
     * Strictly complies with the requirement:
     * "You MUST add thinking mode to the app where relevant to handle users' most complex queries.
     * You MUST use the gemini-3.1-pro-preview model and set `thinkingLevel` to `ThinkingLevel.HIGH`.
     * Do not set `maxOutputTokens`."
     */
    private fun callGeminiWithThinking(systemInstruction: String, userContent: String): String {
        val url = "https://generativelanguage.googleapis.com/v1beta/models/$modelName:generateContent?key=$apiKey"

        val requestJson = JSONObject().apply {
            put("contents", JSONArray().apply {
                put(JSONObject().apply {
                    put("role", "user")
                    put("parts", JSONArray().apply {
                        put(JSONObject().apply { put("text", userContent) })
                    })
                })
            })

            put("systemInstruction", JSONObject().apply {
                put("parts", JSONArray().apply {
                    put(JSONObject().apply { put("text", systemInstruction) })
                })
            })

            // ThinkingConfig with thinkingLevel set to HIGH
            put("generationConfig", JSONObject().apply {
                put("temperature", 0.7)
                put("thinkingConfig", JSONObject().apply {
                    put("thinkingLevel", "HIGH")
                })
                // Notice: maxOutputTokens is deliberately NOT set as mandated
            })
        }

        val requestBody = requestJson.toString().toRequestBody(mediaType)
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        val response = client.newCall(request).execute()
        val responseBody = response.body?.string() ?: ""

        if (!response.isSuccessful) {
            val errorMsg = try {
                val errorJson = JSONObject(responseBody)
                errorJson.optJSONObject("error")?.optString("message") ?: response.message
            } catch (e: Exception) {
                response.message
            }
            throw IllegalStateException("API error (${response.code}): $errorMsg")
        }

        val rootJson = JSONObject(responseBody)
        val candidates = rootJson.optJSONArray("candidates")
        if (candidates == null || candidates.length() == 0) {
            throw IllegalStateException("Nenhum candidato retornado pelo modelo.")
        }

        val firstCandidate = candidates.getJSONObject(0)
        val content = firstCandidate.optJSONObject("content")
        val parts = content?.optJSONArray("parts")

        if (parts == null || parts.length() == 0) {
            throw IllegalStateException("Nenhum texto retornado na resposta.")
        }

        // Return candidate parts combined (excluding pure thought parts if separated)
        val textBuilder = StringBuilder()
        for (i in 0 until parts.length()) {
            val part = parts.getJSONObject(i)
            // If part has 'thought' flag, we can skip or include; standard answer is in 'text'
            val isThought = part.optBoolean("thought", false)
            val text = part.optString("text", "")
            if (!isThought && text.isNotEmpty()) {
                textBuilder.append(text)
            } else if (text.isNotEmpty() && textBuilder.isEmpty()) {
                // If only thought exists, fall back to it
                textBuilder.append(text)
            }
        }

        return textBuilder.toString()
    }

    private fun cleanJsonResponse(raw: String): String {
        var clean = raw.trim()
        if (clean.startsWith("```json")) {
            clean = clean.removePrefix("```json")
        } else if (clean.startsWith("```")) {
            clean = clean.removePrefix("```")
        }
        if (clean.endsWith("```")) {
            clean = clean.removeSuffix("```")
        }
        return clean.trim()
    }
}
