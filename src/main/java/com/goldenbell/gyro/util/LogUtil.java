package com.goldenbell.gyro.util;

import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;

public class LogUtil {

    public static void log(Project project, String content) {
        common(project, content, NotificationType.INFORMATION);
    }

    public static void warning(Project project, String content) {
        common(project, content, NotificationType.WARNING);
    }

    public static void error(Project project, String content) {
        common(project, content, NotificationType.ERROR);
    }

    private static void common(Project project, String content, NotificationType notificationType) {
        NotificationGroupManager.getInstance()
                .getNotificationGroup("Gyro debugger messages")
                .createNotification(content, notificationType)
                .notify(project);
    }
}
