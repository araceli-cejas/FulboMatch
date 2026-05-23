package com.matchball.fulbomatch.ui.navigation

sealed class Routes(val route: String) {
    object Onboarding : Routes("onboarding")
    object Login : Routes("login")
    object Register : Routes("register")
    object Home : Routes("home")
    object MatchDetail : Routes("match_detail/{matchId}") {
        fun createRoute(matchId: String) = "match_detail/$matchId"
    }
    object CreateMatch : Routes("create_match")
    object Profile : Routes("profile")
    object Requests : Routes("requests")
}