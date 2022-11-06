package com.goldenbell.gyro.util;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class PureFileUtil {

    public static void writeToFile(String gyroDirectoryPath, String[] linesToWrite, String fileName) throws FileNotFoundException {
        File parent = new File(gyroDirectoryPath);
        if (!parent.exists()) {
            parent.mkdirs();
        }

        File file = new File(parent, fileName);

        file.deleteOnExit();

        PrintWriter writer = null;

        try {
            writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
            int lineCount = linesToWrite.length;
            for (int i=0; i < lineCount; i++) {
                writer.println(linesToWrite[i]);
            }

            writer.println("");
        } catch (FileNotFoundException fileNotFoundException) {
            throw fileNotFoundException;
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
