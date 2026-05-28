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
import com.matchball.fulbomatch.ui.screens.NotificationsScreen
import com.matchball.fulbomatch.ui.screens.OnboardingScreen
import com.matchball.fulbomatch.ui.screens.ProfileScreen
import com.matchball.fulbomatch.ui.screens.RecuperarContraseñaScreen
import com.matchball.fulbomatch.ui.screens.RegisterScreen
import com.matchball.fulbomatch.ui.screens.StatisticsScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Routes.Onboarding.route
    ) {
        composable(Routes.Onboarding.route) {
            OnboardingScreen(
                onStartClick = {
                    navController.navigate(Routes.Login.route) {
                        popUpTo(Routes.Onboarding.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Routes.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.Home.route) {
                        popUpTo(Routes.Login.route) {
                            inclusive = true
                        }
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
                        popUpTo(Routes.Register.route) {
                            inclusive = true
                        }
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
                isDarkMode = isDarkMode,
                onToggleDarkMode = onToggleDarkMode,
                onMatchClick = { matchId ->
                    navController.navigate(
                        "${Routes.MatchDetail.createRoute(matchId)}?isUserJoined=false&isOrganizer=false&isFinished=false"
                    )
                },
                onCreateMatchClick = {
                    navController.navigate(Routes.CreateMatch.route)
                },
                onProfileClick = {
                    navController.navigate(Routes.Profile.route)
                },
                onRequestsClick = {
                    navController.navigate(Routes.Notifications.route) {
                        launchSingleTop = true
                    }
                },
                onMatchesClick = {
                    navController.navigate(Routes.Matches.route)
                }
            )
        }

        composable(Routes.Matches.route) {
            MatchesScreen(
                isDarkMode = isDarkMode,
                onToggleDarkMode = onToggleDarkMode,
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
                    val isFinished = matchId.startsWith("past_")
                    val isOrganizer = matchId == "2" || matchId == "past_3"

                    navController.navigate(
                        "${Routes.MatchDetail.createRoute(matchId)}?isUserJoined=true&isOrganizer=$isOrganizer&isFinished=$isFinished"
                    )
                },
                onRequestsClick = {
                    navController.navigate(Routes.Notifications.route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = "${Routes.MatchDetail.route}?isUserJoined={isUserJoined}&isOrganizer={isOrganizer}&isFinished={isFinished}",
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
                },
                navArgument("isFinished") {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) { backStackEntry ->
            val matchId = backStackEntry.arguments?.getString("matchId") ?: ""
            val isUserJoined = backStackEntry.arguments?.getBoolean("isUserJoined") ?: false
            val isOrganizer = backStackEntry.arguments?.getBoolean("isOrganizer") ?: false
            val isFinished = backStackEntry.arguments?.getBoolean("isFinished") ?: false

            MatchDetailScreen(
                matchId = matchId,
                isUserJoined = isUserJoined,
                isOrganizer = isOrganizer,
                isFinished = isFinished,
                isDarkMode = isDarkMode,
                onToggleDarkMode = onToggleDarkMode,
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
                },
                onStatisticsClick = { id ->
                    navController.navigate(Routes.MatchStatistics.createRoute(id))
                },
                onNotificationsClick = {
                    navController.navigate(Routes.Notifications.route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = Routes.MatchStatistics.route,
            arguments = listOf(
                navArgument("matchId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val matchId = backStackEntry.arguments?.getString("matchId") ?: ""

            StatisticsScreen(
                matchId = matchId,
                onBackClick = {
                    navController.popBackStack()
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
                onCreateClick = {
                    navController.navigate(Routes.CreateMatch.route)
                },
                onProfileClick = {
                    navController.navigate(Routes.Profile.route) {
                        launchSingleTop = true
                    }
                },
                onNotificationsClick = {
                    navController.navigate(Routes.Notifications.route) {
                        launchSingleTop = true
                    }
                },
                isDarkMode = isDarkMode,
                onToggleDarkMode = onToggleDarkMode
            )
        }

        composable(Routes.CreateMatch.route) {
            CreateMatchScreen(
                isDarkMode = isDarkMode,
                onToggleDarkMode = onToggleDarkMode,
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
                },
                onNotificationsClick = {
                    navController.navigate(Routes.Notifications.route) {
                        launchSingleTop = true
                    }
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
                isDarkMode = isDarkMode,
                onToggleDarkMode = onToggleDarkMode,
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
                },
                onNotificationsClick = {
                    navController.navigate(Routes.Notifications.route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Routes.Profile.route) {
            ProfileScreen(
                isDarkMode = isDarkMode,
                onToggleDarkMode = onToggleDarkMode,
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
                },
                onNotificationsClick = {
                    navController.navigate(Routes.Notifications.route) {
                        launchSingleTop = true
                    }
                },
                onLogoutClick = {
                    navController.navigate(Routes.Login.route) {
                        popUpTo(Routes.Home.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onDeleteAccountClick = {
                    navController.navigate(Routes.Login.route) {
                        popUpTo(Routes.Home.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Routes.EditProfile.route) {
            EditProfileScreen(
                isDarkMode = isDarkMode,
                onToggleDarkMode = onToggleDarkMode,
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
                },
                onNotificationsClick = {
                    navController.navigate(Routes.Notifications.route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Routes.Notifications.route) {
            NotificationsScreen(
                isDarkMode = isDarkMode,
                onToggleDarkMode = onToggleDarkMode,
                onBackClick = {
                    navController.popBackStack()
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
                onCreateClick = {
                    navController.navigate(Routes.CreateMatch.route)
                },
                onProfileClick = {
                    navController.navigate(Routes.Profile.route) {
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}