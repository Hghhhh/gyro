package com.goldenbell.gyro.starter;

import com.goldenbell.gyro.util.KeepAliveUtil;
import com.sun.nio.file.SensitivityWatchEventModifier;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.Permission;
import java.util.Arrays;
import java.util.Iterator;

public class GyroTestStarter {

    public static void main(String[] args) {
        log("########################[GYRO RUNNING]########################");
        String className = args[args.length - 1];
        String tempFilePath = args[args.length - 2];
        File file = new File(tempFilePath);
        Runtime runtime = Runtime.getRuntime();
        runtime.addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                file.deleteOnExit();
            }
        }));
        Path path = Paths.get(file.getParent());
        args = Arrays.copyOf(args, args.length -2);

        try {
            System.setSecurityManager(new NoExitSecurityManager());
        } catch (Throwable throwable) {
            log("trying alternative solution");
        }
        String projectHashCode = file.getParent().split("\\.gyro")[1];
        String tempDirPath = path.getParent().toString();
        KeepAliveUtil.keepAlive(tempDirPath, projectHashCode);

        start(className, args);

        WatchService watchService = null;
        try {
            watchService = FileSystems.getDefault().newWatchService();
            path.register(watchService, new WatchEvent.Kind[] {StandardWatchEventKinds.ENTRY_MODIFY}, SensitivityWatchEventModifier.HIGH);
            while (true) {
                WatchKey watchKey = watchService.take();
                Iterator events = watchKey.pollEvents().iterator();

                while(events.hasNext()) {
                    events.next();
                    String[] params = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8)
                            .stream().filter((line) -> line!=null && !line.isEmpty()).toArray(String[]::new);
                    start(className, params);
                }
                watchKey.reset();
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            if (watchService != null) {
                try {
                    watchService.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private static void start(String className, String[] args) {
        try {
            Method method = Class.forName(className).getMethod("main", String[].class);
            method.invoke(null, new Object[]{args});
        } catch (ExitException | InvocationTargetException e) {
            //ignore
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException e) {
            log("some errors happened, please restart");
            throw new RuntimeException(e);
        }
    }

    private static void log(String message) {
        System.out.println(message);
    }

    protected static class ExitException extends SecurityException {
        public final int status;

        public ExitException(int status) {
            super("not exit");
            this.status = status;
        }
    }

    private static class NoExitSecurityManager extends SecurityManager {
        public NoExitSecurityManager() {}

        public void checkPermission(Permission permission) {}

        public void checkPermission(Permission permission, Object context) {}

        public void checkExit(int status) {
            super.checkExit(status);
            //prevent exit
            throw new ExitException(status);
        }
    }
}
