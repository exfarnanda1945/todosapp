package com.example.todosapp.data.local

import com.example.todosapp.common.Constants.TABLE_NAME

fun searchAndFilerQueryBuilder(filter: FilterBy, sort: SortBy, search: String? = null): String {
    var query = "SELECT * FROM $TABLE_NAME WHERE isArchive = 0 AND isDone = 0"

    if (search != null) {
        query += " AND title LIKE '%${search}%' "
    }

    query += when (filter) {
        FilterBy.HIGH_PRIORITY -> "AND priority = ${TodosPriority.HIGH.ordinal} "
        FilterBy.MEDIUM_PRIORITY -> "AND priority = ${TodosPriority.MEDIUM.ordinal} "
        FilterBy.LOW_PRIORITY -> "AND priority = ${TodosPriority.LOW.ordinal} "
        FilterBy.ALL -> ""
    }

    query += when (sort) {
        SortBy.DATE_DESC -> "ORDER BY date DESC"
        SortBy.DATE_ASC -> "ORDER BY date ASC"

    }

    return query

}

enum class SortBy {
    DATE_ASC, DATE_DESC
}

enum class FilterBy {
    HIGH_PRIORITY, MEDIUM_PRIORITY, LOW_PRIORITY, ALL
}