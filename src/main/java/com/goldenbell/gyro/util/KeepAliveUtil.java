package com.goldenbell.gyro.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.goldenbell.gyro.common.CommonConstants.*;

public class KeepAliveUtil {

    public static void keepAlive(String tempDirPath, String projectHashCode) {
        ScheduledExecutorService keepAliveTimer = Executors.newScheduledThreadPool(1);
        Runnable keepAliveRunnable = new Runnable() {
            @Override
            public void run() {
                String[] aliveTime = new String[] {String.valueOf(System.currentTimeMillis())};
                StringBuilder keepAliveFileDir = new StringBuilder(tempDirPath);
                keepAliveFileDir.append(File.separator);
                keepAliveFileDir.append(KEEP_ALIVE_DIR);
                keepAliveFileDir.append(projectHashCode);
                try {
                    PureFileUtil.writeToFile(keepAliveFileDir.toString(), aliveTime, KEEP_ALIVE_FILE_NAME);
                } catch (Throwable throwable) {
                    System.out.println("some errors happened, please restart");
                }
            }
        };
        keepAliveTimer.scheduleAtFixedRate(keepAliveRunnable, 0, 3000, TimeUnit.MICROSECONDS);
    }

    public static boolean isAlive(File keepAliveFile) throws IOException {
        if (!keepAliveFile.exists()) {
            return false;
        }
        List<String> content = Files.readAllLines(keepAliveFile.toPath(), StandardCharsets.UTF_8);
        String aliveTime = content.size() > 0 ? content.get(0) : "";
        if (aliveTime == null || aliveTime.isEmpty()) {
            return false;
        }
        try {
            return System.currentTimeMillis() - Long.parseLong(aliveTime) <= KEEP_ALIVE_ALLOW_TIME;
        } catch (NumberFormatException numberFormatException) {
            return false;
        }
    }
}
