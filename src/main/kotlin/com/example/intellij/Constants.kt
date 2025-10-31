package com.example.intellij

import com.intellij.ide.plugins.PluginManager
import com.intellij.ide.plugins.cl.PluginClassLoader
import com.intellij.openapi.application.ApplicationNamesInfo
import com.intellij.openapi.application.PathManager
import com.intellij.openapi.extensions.PluginId

object Constants {
    val CLASS_LOADER: PluginClassLoader = Constants::class.java.classLoader as PluginClassLoader
    val PLUGIN_ID: PluginId = CLASS_LOADER.pluginId
    val PLUGIN_NAME: String = PluginManager.getPlugin(PLUGIN_ID)?.name ?: ""
    val PRODUCT_NAME: String = ApplicationNamesInfo.getInstance().fullProductName
    val PRODUCT_HASH: String = PathManager.getConfigPath().hashCode().toString()
}
