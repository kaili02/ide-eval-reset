package com.example.intellij

import com.intellij.icons.AllIcons
import com.intellij.notification.Notification
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

object NotificationHelper {

    private val NOTIFICATION_GROUP: NotificationGroup by lazy {
        NotificationGroupManager.getInstance().getNotificationGroup("my.plugin.notification.group")
            ?: error("Notification group 'my.plugin.notification.group' not found. Make sure it is registered in plugin.xml")
    }


    private fun createNotification(
        project: Project?,
        title: String? = Constants.PLUGIN_NAME,
        subtitle: String? = null,
        content: String,
        type: NotificationType,
        icon: javax.swing.Icon? = AllIcons.General.Reset
    ): Notification {
        // 新方式只传 content 和 type
        val notification = NOTIFICATION_GROUP.createNotification(content, type)

        // 设置标题、副标题和图标
        if (title != null) {
            notification.setTitle(title);
        }
        if (subtitle != null) {
            notification.subtitle = subtitle
        }
        notification.icon = icon

        notification.notify(project)
        return notification
    }


    // Error
    fun showError(project: Project?, content: String): Notification =
        createNotification(project, content = content, type = NotificationType.ERROR)

    fun showError(project: Project?, title: String?, content: String): Notification =
        createNotification(project, title = title, content = content, type = NotificationType.ERROR)

    fun showError(project: Project?, title: String?, subtitle: String?, content: String): Notification =
        createNotification(project, title = title, subtitle = subtitle, content = content, type = NotificationType.ERROR)

    // Warning
    fun showWarn(project: Project?, content: String): Notification =
        createNotification(project, content = content, type = NotificationType.WARNING)

    fun showWarn(project: Project?, title: String?, content: String): Notification =
        createNotification(project, title = title, content = content, type = NotificationType.WARNING)

    fun showWarn(project: Project?, title: String?, subtitle: String?, content: String): Notification =
        createNotification(project, title = title, subtitle = subtitle, content = content, type = NotificationType.WARNING)

    // Info
    fun showInfo(project: Project?, content: String): Notification =
        createNotification(project, content = content, type = NotificationType.INFORMATION)

    fun showInfo(project: Project?, title: String?, content: String): Notification =
        createNotification(project, title = title, content = content, type = NotificationType.INFORMATION)

    fun showInfo(project: Project?, title: String?, subtitle: String?, content: String): Notification =
        createNotification(project, title = title, subtitle = subtitle, content = content, type = NotificationType.INFORMATION)
}

