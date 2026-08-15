package com.aura.studio.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.aura.studio.avatar.AvatarSpec

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AvatarList : Screen("avatar_list")
    object AvatarCreator : Screen("avatar_creator?id={id}") {
        fun create(id: String? = null) = if (id != null) "avatar_creator?id=$id" else "avatar_creator"
    }
    object Generate : Screen("generate/{id}") {
        fun create(id: String) = "generate/$id"
    }
}

@Composable
fun AuraNavHost(
    viewModel: AvatarViewModel = hiltViewModel()
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Home.route) {

        composable(Screen.Home.route) {
            MainScreen(
                onOpenAvatars = { navController.navigate(Screen.AvatarList.route) },
                onCreateAvatar = { navController.navigate(Screen.AvatarCreator.create()) }
            )
        }

        composable(Screen.AvatarList.route) {
            AvatarListScreen(
                viewModel = viewModel,
                onCreateNew = { navController.navigate(Screen.AvatarCreator.create()) },
                onOpen = { avatar ->
                    navController.navigate(Screen.AvatarCreator.create(avatar.id))
                }
            )
        }

        composable(
            route = "avatar_creator?id={id}",
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStack ->
            val id = backStack.arguments?.getString("id")
            val initial = if (id != null) {
                viewModel.avatars.value.find { it.id == id } ?: AvatarSpec()
            } else AvatarSpec()

            AvatarCreatorScreen(
                initial = initial,
                onSave = { spec ->
                    viewModel.save(spec)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Generate.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStack ->
            val id = backStack.arguments?.getString("id") ?: return@composable
            val avatar = viewModel.avatars.value.find { it.id == id } ?: AvatarSpec()

            GenerateScreen(
                avatar = avatar,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
