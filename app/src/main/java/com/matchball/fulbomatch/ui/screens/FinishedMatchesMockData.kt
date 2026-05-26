package com.matchball.fulbomatch.ui.screens

data class FinishedMatchDetail(
    val id: String,
    val title: String,
    val location: String,
    val date: String,
    val time: String,
    val resultHome: String,
    val resultAway: String,
    val organizerName: String,
    val rating: String,
    val ratingDescription: String,
    val surface: String,
    val level: String,
    val players: String,
    val statistics: FinishedMatchStatistics
) {
    val totalGoals: Int
        get() = (resultHome.toIntOrNull() ?: 0) + (resultAway.toIntOrNull() ?: 0)
}

data class FinishedMatchStatistics(
    val dateTimeAndPlace: String,
    val teamAName: String,
    val teamBName: String,
    val duration: Int,
    val yellowCards: Int,
    val redCards: Int,
    val scorers: List<FinishedMatchScorer>
)

data class FinishedMatchScorer(
    val name: String,
    val teamName: String,
    val goals: Int
)

private val finishedMatchesMock = listOf(
    FinishedMatchDetail(
        id = "past_1",
        title = "Cancha El Clásico",
        location = "Palermo, CABA",
        date = "12 OCT",
        time = "19:00 hs",
        resultHome = "5",
        resultAway = "3",
        organizerName = "Juan P.",
        rating = "4.9",
        ratingDescription = "(25 partidos)",
        surface = "Césped\nSintético",
        level = "Amateur",
        players = "10/10",
        statistics = FinishedMatchStatistics(
            dateTimeAndPlace = "Sábado 12 Oct, 19:00 - Palermo, CABA",
            teamAName = "Equipo A",
            teamBName = "Equipo B",
            duration = 60,
            yellowCards = 2,
            redCards = 0,
            scorers = listOf(
                FinishedMatchScorer("Carlos P.", "Equipo A", 3),
                FinishedMatchScorer("David G.", "Equipo A", 2),
                FinishedMatchScorer("Bruno S.", "Equipo B", 2),
                FinishedMatchScorer("Enzo M.", "Equipo B", 1)
            )
        )
    ),
    FinishedMatchDetail(
        id = "past_2",
        title = "La Canchita F5",
        location = "Belgrano, CABA",
        date = "05 OCT",
        time = "20:30 hs",
        resultHome = "2",
        resultAway = "2",
        organizerName = "Matías G.",
        rating = "4.7",
        ratingDescription = "(18 partidos)",
        surface = "Césped\nSintético",
        level = "Medio",
        players = "10/10",
        statistics = FinishedMatchStatistics(
            dateTimeAndPlace = "Sábado 05 Oct, 20:30 - Belgrano, CABA",
            teamAName = "Equipo A",
            teamBName = "Equipo B",
            duration = 60,
            yellowCards = 3,
            redCards = 1,
            scorers = listOf(
                FinishedMatchScorer("Lucas R.", "Equipo A", 1),
                FinishedMatchScorer("Nico M.", "Equipo A", 1),
                FinishedMatchScorer("Fede P.", "Equipo B", 2)
            )
        )
    ),
    FinishedMatchDetail(
        id = "past_3",
        title = "Fútbol City",
        location = "Vicente López, GBA",
        date = "28 SEP",
        time = "18:00 hs",
        resultHome = "1",
        resultAway = "4",
        organizerName = "Martín G.",
        rating = "4.8",
        ratingDescription = "(12 partidos)",
        surface = "Sintético",
        level = "Avanzado",
        players = "12/14",
        statistics = FinishedMatchStatistics(
            dateTimeAndPlace = "Sábado 28 Sep, 18:00 - Vicente López, GBA",
            teamAName = "Equipo A",
            teamBName = "Equipo B",
            duration = 60,
            yellowCards = 1,
            redCards = 0,
            scorers = listOf(
                FinishedMatchScorer("Araceli C.", "Equipo A", 1),
                FinishedMatchScorer("David G.", "Equipo B", 2),
                FinishedMatchScorer("Carlos P.", "Equipo B", 2)
            )
        )
    )
)

fun getFinishedMatchDetail(matchId: String): FinishedMatchDetail? {
    return finishedMatchesMock.find { it.id == matchId }
}