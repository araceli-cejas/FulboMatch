package com.matchball.fulbomatch.ui.navigation

sealed class Routes(val route: String) {
    object Onboarding : Routes("onboarding")
    object Login : Routes("login")
    object Register : Routes("register")

    object RecuperarContraseña : Routes("recuperar_contraseña")
    object Home : Routes("home")

    object MatchDetail : Routes("match_detail/{matchId}") {
        fun createRoute(matchId: String) = "match_detail/$matchId"
    }

    object CreateMatch : Routes("create_match")

    object EditMatch : Routes("edit_match/{matchId}") {
        fun createRoute(matchId: String) = "edit_match/$matchId"
    }
    object MatchStatistics : Routes("match_statistics/{matchId}") {
        fun createRoute(matchId: String): String {
            return "match_statistics/$matchId"
        }
    }
    object Matches : Routes("matches")
    object Notifications : Routes("notifications")
    object Profile : Routes("profile")
    object EditProfile : Routes("edit_profile")
}