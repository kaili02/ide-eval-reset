package com.example.intellij

import com.intellij.openapi.components.Service

import java.text.SimpleDateFormat
import java.util.*
import java.util.prefs.Preferences
import kotlin.concurrent.schedule

@Service
class MainComponent {

    companion object {
        private const val RESET_PERIOD = 2160000000L // 25 days
    }

    private val prefs: Preferences = Preferences.userRoot().node(Constants.PLUGIN_NAME)

    init {
        val lastResetTime = prefs.getLong(Constants.PRODUCT_NAME + Constants.PRODUCT_HASH, 0L)

        // 延迟 3 秒显示上次重置时间
        Timer().schedule(3000) {
            if (lastResetTime > 0) {
                val date = Date(lastResetTime)
                val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                NotificationHelper.showInfo(null,"The last reset time: ${format.format(date)}")
            }

            scheduleResetTask(lastResetTime)
        }
    }

    private fun scheduleResetTask(lastResetTime: Long) {
        Timer().schedule(0, 3600000) { // 每小时检查一次
            if (System.currentTimeMillis() - lastResetTime > RESET_PERIOD) {
                val message = "It has been a long time since the last reset!\nWould you like to reset it again?"
                val notification = NotificationHelper.showInfo(null,message)
                notification.addAction(ResetAction())
            }
        }
    }
}
