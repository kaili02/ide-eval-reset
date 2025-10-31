package com.example.intellij

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.PathManager
import com.intellij.openapi.application.ex.ApplicationInfoEx
import com.intellij.openapi.application.impl.ApplicationInfoImpl
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.util.text.StringUtil
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import java.util.prefs.Preferences

class ResetAction : AnAction(
    "Reset ${Constants.PRODUCT_NAME}'s Eval",
    "Reset my IDE eval information",
    AllIcons.General.Reset
) {

    companion object {
        private const val OLD_MACHINE_ID_KEY = "JetBrains.UserIdOnMachine"
        private const val NEW_MACHINE_ID_KEY = "user_id_on_machine"
        private const val DEVICE_ID_KEY = "device_id"
        private const val DEFAULT_COMPANY_NAME = "jetbrains"
    }

    override fun actionPerformed(anActionEvent: AnActionEvent) {
        val project: Project? = anActionEvent.project
        val evalFile = getEvalFile()
        if (evalFile.exists()) {
            if (!FileUtil.delete(evalFile)) {
                NotificationHelper.showError(project, "Remove eval folder failed!")
                return
            }
        }

        val optionsFile = getOptionsFile()
        if (optionsFile.exists()) {
            try {
                val sbContent = StringBuilder()
                Scanner(optionsFile).use { scanner ->
                    while (scanner.hasNextLine()) {
                        val line = scanner.nextLine()
                        if (!line.contains("name=\"evlsprt")) {
                            sbContent.append(line).append("\n")
                        }
                    }
                }
                Files.write(Paths.get(optionsFile.toURI()), sbContent.toString().toByteArray())
            } catch (e: IOException) {
                NotificationHelper.showError(project, e.message ?: "Unknown IO Error")
                return
            }
        }

        val appInfo: ApplicationInfoEx = ApplicationInfoImpl.getShadowInstance()
        val companyName = appInfo.shortCompanyName
        val node = if (StringUtil.isEmptyOrSpaces(companyName)) DEFAULT_COMPANY_NAME else companyName.lowercase()

        val prefsRoot: Preferences = Preferences.userRoot()
        val prefs: Preferences = prefsRoot.node(node)

        prefsRoot.remove(OLD_MACHINE_ID_KEY)
        prefs.remove(NEW_MACHINE_ID_KEY)
        prefs.remove(DEVICE_ID_KEY)

        Preferences.userRoot().node(Constants.PLUGIN_NAME)
            .put("${Constants.PRODUCT_NAME}${Constants.PRODUCT_HASH}", System.currentTimeMillis().toString())

        if (appInfo.isVendorJetBrains && SystemInfo.isWindows) {
            val names = arrayOf("PermanentUserId", "PermanentDeviceId")
            for (name in names) {
                if (deleteSharedFile(name)) continue
                NotificationHelper.showError(project, "Remove $name file failed!")
                return
            }
        }

        NotificationHelper.showInfo(project, "Reset successfully!\nPlease restart your IDE and enjoy it!")
        ApplicationManager.getApplication().invokeLater { ApplicationManager.getApplication().restart() }
    }

    override fun isDumbAware(): Boolean = false

    protected fun deleteSharedFile(fileName: String): Boolean {
        val appData = System.getenv("APPDATA") ?: return false
        val dir = Paths.get(appData, "JetBrains", fileName).toFile()
        return !dir.exists() || dir.delete()
    }

    protected fun getEvalFile(): File {
        val configPath = PathManager.getConfigPath()
        return File(configPath, "eval")
    }

    protected fun getOptionsFile(): File {
        val configPath = PathManager.getConfigPath()
        return File(File(configPath, "options"), "other.xml")
    }
}
