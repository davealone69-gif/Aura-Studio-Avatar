package com.aura.studio.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore("aura_prefs")

data class GenDefaults(
    val steps: Int = 20,
    val cfg: Float = 7f,
    val width: Int = 512,
    val height: Int = 768,
    val defaultNude: Boolean = true,
    val autoEnhance: Boolean = false,
    val defaultLlmPath: String = "",
    val defaultImageModelPath: String = ""
)

@Singleton
class UserPrefs @Inject constructor(@ApplicationContext private val context: Context) {
    private object Keys {
        val STEPS = intPreferencesKey("gen_steps")
        val CFG = floatPreferencesKey("gen_cfg")
        val WIDTH = intPreferencesKey("gen_width")
        val HEIGHT = intPreferencesKey("gen_height")
        val DEFAULT_NUDE = booleanPreferencesKey("default_nude")
        val AUTO_ENHANCE = booleanPreferencesKey("auto_enhance")
        val LLM_PATH = stringPreferencesKey("llm_path")
        val IMAGE_PATH = stringPreferencesKey("image_model_path")
    }

    val genDefaults: Flow<GenDefaults> = context.dataStore.data.map { p ->
        GenDefaults(
            steps = p[Keys.STEPS] ?: 20,
            cfg = p[Keys.CFG] ?: 7f,
            width = p[Keys.WIDTH] ?: 512,
            height = p[Keys.HEIGHT] ?: 768,
            defaultNude = p[Keys.DEFAULT_NUDE] ?: true,
            autoEnhance = p[Keys.AUTO_ENHANCE] ?: false,
            defaultLlmPath = p[Keys.LLM_PATH] ?: "",
            defaultImageModelPath = p[Keys.IMAGE_PATH] ?: ""
        )
    }

    suspend fun setSteps(v: Int) = context.dataStore.edit { it[Keys.STEPS] = v.coerceIn(4, 50) }
    suspend fun setCfg(v: Float) = context.dataStore.edit { it[Keys.CFG] = v.coerceIn(1f, 20f) }
    suspend fun setSize(w: Int, h: Int) = context.dataStore.edit { it[Keys.WIDTH] = w; it[Keys.HEIGHT] = h }
    suspend fun setDefaultNude(v: Boolean) = context.dataStore.edit { it[Keys.DEFAULT_NUDE] = v }
    suspend fun setAutoEnhance(v: Boolean) = context.dataStore.edit { it[Keys.AUTO_ENHANCE] = v }
    suspend fun setLlmPath(v: String) = context.dataStore.edit { it[Keys.LLM_PATH] = v }
    suspend fun setImageModelPath(v: String) = context.dataStore.edit { it[Keys.IMAGE_PATH] = v }
}
