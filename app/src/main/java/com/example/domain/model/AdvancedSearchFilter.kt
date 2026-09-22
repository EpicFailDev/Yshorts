package com.example.domain.model

enum class DateRangeFilter(val label: String) {
    ALL("Qualquer data"),
    TODAY("Últimas 24 horas"),
    LAST_7_DAYS("Últimos 7 dias"),
    LAST_30_DAYS("Últimos 30 dias")
}

enum class SortOrder(val label: String) {
    CREATED_DESC("Mais recentes (Criação)"),
    CREATED_ASC("Mais antigos (Criação)"),
    TITLE_ASC("Título (A-Z)"),
    WORDS_DESC("Mais palavras"),
    DURATION_DESC("Maior duração")
}

data class AdvancedSearchFilter(
    val query: String = "",
    val category: String = "Todas",
    val tag: String = "",
    val dateRange: DateRangeFilter = DateRangeFilter.ALL,
    val sortBy: SortOrder = SortOrder.CREATED_DESC
) {
    val isActive: Boolean
        get() = query.isNotBlank() ||
                (category != "Todas" && category.isNotBlank()) ||
                tag.isNotBlank() ||
                dateRange != DateRangeFilter.ALL ||
                sortBy != SortOrder.CREATED_DESC

    fun reset(): AdvancedSearchFilter = AdvancedSearchFilter()
}
