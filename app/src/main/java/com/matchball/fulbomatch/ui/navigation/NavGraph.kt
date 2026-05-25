package com.matchball.fulbomatch.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.matchball.fulbomatch.ui.screens.CreateMatchScreen
import com.matchball.fulbomatch.ui.screens.EditMatchScreen
import com.matchball.fulbomatch.ui.screens.EditProfileScreen
import com.matchball.fulbomatch.ui.screens.HomeScreen
import com.matchball.fulbomatch.ui.screens.LoginScreen
import com.matchball.fulbomatch.ui.screens.MatchDetailScreen
import com.matchball.fulbomatch.ui.screens.MatchesScreen
import com.matchball.fulbomatch.ui.screens.OnboardingScreen
import com.matchball.fulbomatch.ui.screens.ProfileScreen
import com.matchball.fulbomatch.ui.screens.RecuperarContraseñaScreen
import com.matchball.fulbomatch.ui.screens.RegisterScreen
import com.matchball.fulbomatch.ui.screens.RequestsScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.Onboarding.route
    ) {
        composable(Routes.Onboarding.route) {
            OnboardingScreen(
                onStartClick = {
                    navController.navigate(Routes.Login.route) {
                        popUpTo(Routes.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.Home.route) {
                        popUpTo(Routes.Login.route) { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate(Routes.Register.route)
                },
                onRecuperarClick = {
                    navController.navigate(Routes.RecuperarContraseña.route)
                }
            )
        }

        composable(Routes.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Routes.Home.route) {
                        popUpTo(Routes.Register.route) { inclusive = true }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.RecuperarContraseña.route) {
            RecuperarContraseñaScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.Home.route) {
            HomeScreen(
                onMatchClick = { matchId ->
                    navController.navigate(
                        "${Routes.MatchDetail.createRoute(matchId)}?isUserJoined=false&isOrganizer=false"
                    )
                },
                onCreateMatchClick = {
                    navController.navigate(Routes.CreateMatch.route)
                },
                onProfileClick = {
                    navController.navigate(Routes.Profile.route)
                },
                onRequestsClick = {
                    navController.navigate(Routes.Requests.route)
                },
                onMatchesClick = {
                    navController.navigate(Routes.Matches.route)
                }
            )
        }

        composable(Routes.Matches.route) {
            MatchesScreen(
                onHomeClick = {
                    navController.navigate(Routes.Home.route) {
                        launchSingleTop = true
                    }
                },
                onCreateMatchClick = {
                    navController.navigate(Routes.CreateMatch.route)
                },
                onProfileClick = {
                    navController.navigate(Routes.Profile.route)
                },
                onMatchClick = { matchId ->
                    val isOrganizer = matchId == "2" || matchId == "past_3"

                    navController.navigate(
                        "${Routes.MatchDetail.createRoute(matchId)}?isUserJoined=true&isOrganizer=$isOrganizer"
                    )
                },
                onRequestsClick = {
                    navController.navigate(Routes.Requests.route)
                }
            )
        }

        composable(
            route = "${Routes.MatchDetail.route}?isUserJoined={isUserJoined}&isOrganizer={isOrganizer}",
            arguments = listOf(
                navArgument("matchId") {
                    type = NavType.StringType
                },
                navArgument("isUserJoined") {
                    type = NavType.BoolType
                    defaultValue = false
                },
                navArgument("isOrganizer") {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) { backStackEntry ->
            val matchId = backStackEntry.arguments?.getString("matchId") ?: ""
            val isUserJoined = backStackEntry.arguments?.getBoolean("isUserJoined") ?: false
            val isOrganizer = backStackEntry.arguments?.getBoolean("isOrganizer") ?: false

            MatchDetailScreen(
                matchId = matchId,
                isUserJoined = isUserJoined,
                isOrganizer = isOrganizer,
                onBackClick = {
                    navController.popBackStack()
                },
                onEditMatchClick = { id ->
                    navController.navigate(Routes.EditMatch.createRoute(id))
                },
                onJoinClick = {
                    navController.navigate(Routes.Matches.route) {
                        popUpTo(Routes.Home.route) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                },
                onLeaveClick = {
                    navController.navigate(Routes.Matches.route) {
                        popUpTo(Routes.Matches.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Routes.CreateMatch.route) {
            CreateMatchScreen(
                onMatchCreated = {
                    navController.popBackStack()
                },
                onBackClick = {
                    navController.navigate(Routes.Home.route)
                },
                onProfileClick = {
                    navController.navigate(Routes.Profile.route)
                },
                onMatchesClick = {
                    navController.navigate(Routes.Matches.route)
                }
            )
        }

        composable(
            route = Routes.EditMatch.route,
            arguments = listOf(
                navArgument("matchId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val matchId = backStackEntry.arguments?.getString("matchId") ?: ""

            EditMatchScreen(
                matchId = matchId,
                onBackClick = {
                    navController.popBackStack()
                },
                onSaveClick = {
                    navController.navigate(Routes.Matches.route) {
                        launchSingleTop = true
                    }
                },
                onCancelMatchClick = {
                    navController.navigate(Routes.Matches.route) {
                        launchSingleTop = true
                    }
                },
                onHomeClick = {
                    navController.navigate(Routes.Home.route) {
                        launchSingleTop = true
                    }
                },
                onMatchesClick = {
                    navController.navigate(Routes.Matches.route) {
                        launchSingleTop = true
                    }
                },
                onCreateMatchClick = {
                    navController.navigate(Routes.CreateMatch.route)
                },
                onProfileClick = {
                    navController.navigate(Routes.Profile.route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Routes.Profile.route) {
            ProfileScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onHomeClick = {
                    navController.navigate(Routes.Home.route)
                },
                onCreateMatchClick = {
                    navController.navigate(Routes.CreateMatch.route)
                },
                onMatchesClick = {
                    navController.navigate(Routes.Matches.route)
                },
                onEditProfileClick = {
                    navController.navigate(Routes.EditProfile.route)
                }
            )
        }

        composable(Routes.EditProfile.route) {
            EditProfileScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onSaveClick = {
                    navController.popBackStack()
                },
                onHomeClick = {
                    navController.navigate(Routes.Home.route)
                },
                onMatchesClick = {
                    navController.navigate(Routes.Matches.route)
                },
                onCreateMatchClick = {
                    navController.navigate(Routes.CreateMatch.route)
                }
            )
        }

        composable(Routes.Requests.route) {
            RequestsScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}