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
    const val DESIGNER = "designer"
    const val EDIT = "edit/{id}"
    const val DETAIL = "detail/{id}"
    const val GENERATE = "generate/{id}"
    const val MODELS = "models"
    const val SETTINGS = "settings"
    const val GALLERY = "gallery"
    fun edit(id: String) = "edit/$id"
    fun detail(id: String) = "detail/$id"
    fun generate(id: String) = "generate/$id"
}

@Composable
fun AuraNavGraph(navController: NavHostController, viewModel: AvatarViewModel = hiltViewModel()) {
    NavHost(navController = navController, startDestination = Routes.LIST) {
        composable(Routes.LIST) {
            AvatarListScreen(
                viewModel = viewModel,
                onCreateNew = { navController.navigate(Routes.DESIGNER) },
                onOpen = { a -> navController.navigate(Routes.detail(a.id)) },
                onOpenModels = { navController.navigate(Routes.MODELS) },
                onOpenSettings = { navController.navigate(Routes.SETTINGS) },
                onOpenGallery = { navController.navigate(Routes.GALLERY) }
            )
        }
        composable(Routes.DESIGNER) {
            AvatarDesignerScreen(
                initial = AvatarSpec(),
                onSave = { s -> viewModel.save(s); navController.popBackStack() },
                onBack = { navController.popBackStack() },
                onGenerate = { s -> viewModel.save(s); navController.navigate(Routes.generate(s.id)) }
            )
        }
        composable(Routes.EDIT, arguments = listOf(navArgument("id") { type = NavType.StringType })) { e ->
            val id = e.arguments?.getString("id") ?: return@composable
            val existing = viewModel.avatars.value.find { it.id == id } ?: AvatarSpec(id = id)
            AvatarDesignerScreen(
                initial = existing,
                onSave = { s -> viewModel.save(s); navController.popBackStack() },
                onBack = { navController.popBackStack() },
                onGenerate = { s -> viewModel.save(s); navController.navigate(Routes.generate(s.id)) }
            )
        }
        composable(Routes.DETAIL, arguments = listOf(navArgument("id") { type = NavType.StringType })) { e ->
            val id = e.arguments?.getString("id") ?: return@composable
            val avatar = viewModel.avatars.value.find { it.id == id } ?: return@composable
            AvatarDetailScreen(avatar, onEdit = { navController.navigate(Routes.edit(avatar.id)) }, onBack = { navController.popBackStack() }, onGenerate = { navController.navigate(Routes.generate(avatar.id)) })
        }
        composable(Routes.GENERATE, arguments = listOf(navArgument("id") { type = NavType.StringType })) { e ->
            val id = e.arguments?.getString("id") ?: return@composable
            val avatar = viewModel.avatars.value.find { it.id == id } ?: return@composable
            GenerateScreen(avatar, onBack = { navController.popBackStack() })
        }
        composable(Routes.MODELS) { ModelManagerScreen(onBack = { navController.popBackStack() }) }
        composable(Routes.SETTINGS) { SettingsScreen(onBack = { navController.popBackStack() }) }
        composable(Routes.GALLERY) { GalleryScreen(onBack = { navController.popBackStack() }) }
    }
}
