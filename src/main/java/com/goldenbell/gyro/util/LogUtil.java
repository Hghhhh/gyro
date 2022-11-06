package com.goldenbell.gyro.util;

import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;

public class LogUtil {

    public static void log(Project project, String content) {
        NotificationGroupManager.getInstance()
                .getNotificationGroup("Gyro debugger messages")
                .createNotification(content, NotificationType.INFORMATION)
                .notify(project);
    }
}
