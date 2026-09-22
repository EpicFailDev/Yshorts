package com.example

import com.example.domain.model.AdvancedSearchFilter
import com.example.domain.model.DateRangeFilter
import com.example.domain.model.ScriptStatus
import com.example.domain.model.ShortsScript
import com.example.domain.model.SortOrder
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun testShortsScriptCalculations() {
    val script = ShortsScript(
      title = "Dica Rápida",
      hook = "Você sabia disso?",
      bodyContent = "Este é um teste com dez palavras para verificar o cálculo.",
      callToAction = "Inscreva-se!",
      targetDurationSeconds = 15
    )

    assertTrue(script.totalWords > 0)
    assertTrue(script.estimatedSeconds >= 1)
    assertTrue(script.toFormattedPlainText().contains("ROTEIRO YOUTUBE SHORTS"))
    assertTrue(script.toFormattedPlainText().contains("GANCHO INICIAL"))
  }

  @Test
  fun testAdvancedSearchFiltering() {
    val now = System.currentTimeMillis()
    val script1 = ShortsScript(
      id = 1,
      title = "Como Investir Dinheiro",
      category = "Finanças",
      hook = "Pare de perder dinheiro hoje!",
      bodyContent = "Guarde no tesouro selic.",
      tags = "investimento, finanças, tesouro",
      createdAt = now - 1000 * 60 * 60 // 1 hour ago
    )
    val script2 = ShortsScript(
      id = 2,
      title = "3 Dicas Tech",
      category = "Tecnologia",
      hook = "Seu celular trava muito?",
      bodyContent = "Limpe o cache agora.",
      tags = "dicas, celular, android",
      createdAt = now - 1000 * 60 * 60 * 24 * 10 // 10 days ago
    )
    val list = listOf(script1, script2)

    // 1. Text Query
    val queryFilter = AdvancedSearchFilter(query = "investir")
    val queryMatch = list.filter { it.title.contains(queryFilter.query, ignoreCase = true) }
    assertEquals(1, queryMatch.size)
    assertEquals("Como Investir Dinheiro", queryMatch[0].title)

    // 2. Category
    val catFilter = AdvancedSearchFilter(category = "Tecnologia")
    val catMatch = list.filter { it.category == catFilter.category }
    assertEquals(1, catMatch.size)
    assertEquals(2L, catMatch[0].id)

    // 3. Tag
    val tagFilter = AdvancedSearchFilter(tag = "android")
    val tagMatch = list.filter { it.tags.contains(tagFilter.tag, ignoreCase = true) }
    assertEquals(1, tagMatch.size)
    assertEquals(2L, tagMatch[0].id)

    // 4. Date Range
    val dateFilter = AdvancedSearchFilter(dateRange = DateRangeFilter.TODAY)
    val dateMatch = list.filter { it.createdAt >= (now - 24L * 60 * 60 * 1000) }
    assertEquals(1, dateMatch.size)
    assertEquals(1L, dateMatch[0].id)

    // 5. Sort Order
    val sortDesc = list.sortedByDescending { it.createdAt }
    assertEquals(1L, sortDesc[0].id)
    val sortAsc = list.sortedBy { it.createdAt }
    assertEquals(2L, sortAsc[0].id)
  }
}
