package com.goldenbell.gyro.util;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtilRt;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;

import static com.goldenbell.gyro.common.CommonConstants.*;

public class FileUtil {

    private static String getTempFileDir(Project project) {
        int hashOfProjectName = Math.abs(project.getName().hashCode());
        StringBuilder filePath = new StringBuilder(FileUtilRt.getTempDirectory());
        filePath.append(File.separator);
        filePath.append(PLUGIN_TEMP_DIR + hashOfProjectName);
        return filePath.toString();
    }

    public static File getTempFile(Project project) {
        return Paths.get(getTempFileDir(project) + File.separator + PLUGIN_FILE_NAME).toFile();
    }

    public static File getKeepAliveFile(Project project) {
        int hashOfProjectName = Math.abs(project.getName().hashCode());
        StringBuilder filePath = new StringBuilder(FileUtilRt.getTempDirectory());
        filePath.append(File.separator);
        filePath.append(KEEP_ALIVE_DIR + hashOfProjectName);
        filePath.append(File.separator);
        filePath.append(KEEP_ALIVE_FILE_NAME);
        return Paths.get( filePath.toString()).toFile();
    }

    public static void writeToTempFile(Project project, @NotNull String[] linesToWrite) throws FileNotFoundException {
        PureFileUtil.writeToFile(getTempFileDir(project), linesToWrite, PLUGIN_FILE_NAME);
    }
}
