package com.aura.studio.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.aura.studio.avatar.AvatarSpec

object Routes {
    const val LIST = "list"
    const val CREATE = "create"
    const val EDIT = "edit/{id}"
    const val DETAIL = "detail/{id}"
    fun edit(id: String) = "edit/$id"
    fun detail(id: String) = "detail/$id"
}

@Composable
fun AuraNavGraph(
    navController: NavHostController,
    viewModel: AvatarViewModel = hiltViewModel()
) {
    NavHost(
        navController = navController,
        startDestination = Routes.LIST
    ) {
        composable(Routes.LIST) {
            AvatarListScreen(
                viewModel = viewModel,
                onCreateNew = { navController.navigate(Routes.CREATE) },
                onOpen = { avatar -> navController.navigate(Routes.detail(avatar.id)) }
            )
        }

        composable(Routes.CREATE) {
            AvatarCreatorScreen(
                initial = AvatarSpec(),
                onSave = { spec ->
                    viewModel.save(spec)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.EDIT,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: return@composable
            val existing = viewModel.avatars.value.find { it.id == id } ?: AvatarSpec(id = id)

            AvatarCreatorScreen(
                initial = existing,
                onSave = { spec ->
                    viewModel.save(spec)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.DETAIL,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: return@composable
            val avatar = viewModel.avatars.value.find { it.id == id } ?: return@composable

            AvatarDetailScreen(
                avatar = avatar,
                onEdit = { navController.navigate(Routes.edit(avatar.id)) },
                onBack = { navController.popBackStack() },
                onGenerate = {
                    // Placeholder for future real generation
                }
            )
        }
    }
}
