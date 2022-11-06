package com.goldenbell.gyro.programrunner;

import com.goldenbell.gyro.settings.GyroSettingsState;
import com.goldenbell.gyro.starter.GyroTestStarter;
import com.goldenbell.gyro.util.FileUtil;
import com.goldenbell.gyro.util.KeepAliveUtil;
import com.goldenbell.gyro.util.LogUtil;
import com.intellij.debugger.impl.GenericDebuggerRunner;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.*;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.target.TargetEnvironmentAwareRunProfileState;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.ThrowableComputable;
import com.intellij.util.SlowOperations;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.concurrency.AsyncPromise;
import org.jetbrains.concurrency.Promise;

import java.io.File;
import java.io.IOException;

import static com.goldenbell.gyro.common.CommonConstants.*;

public class GyroDebuggerRunner extends GenericDebuggerRunner {

    @Override
    public @NotNull String getRunnerId() {
        return RUNNER_ID;
    }

    @Override
    public boolean canRun(@NotNull String executorId, @NotNull RunProfile profile) {
        //todo: now only support maven project, support gradle later
        return executorId.equals(RUNNER_ID) && profile instanceof ModuleRunProfile && !(profile instanceof RunConfigurationWithSuppressedDefaultDebugAction);
    }

    private boolean doPreExecute(@NotNull RunProfileState state, @NotNull ExecutionEnvironment env) throws Throwable {
        Project project = env.getProject();
        File tempFile = FileUtil.getTempFile(project);
        File keepAliveFile = FileUtil.getKeepAliveFile(project);
        boolean wasRun = tempFile.exists() && KeepAliveUtil.isAlive(keepAliveFile);
        RunConfigurationBase runConfigurationBase = (RunConfigurationBase) env.getRunProfile();
        runConfigurationBase.setAllowRunningInParallel(true);
        JavaParameters javaParameters =
            SlowOperations.allowSlowOperations(new ThrowableComputable<JavaParameters, Throwable>() {

                @Override
                public JavaParameters compute() throws Throwable {
                    return ((JavaCommandLine) state).getJavaParameters();
                }
            });
        javaParameters.getClassPath().addFirst(PathManager.getJarPathForClass(GyroTestStarter.class));
        FileUtil.writeToTempFile(project, javaParameters.getProgramParametersList().getArray());
        javaParameters.getProgramParametersList().add(tempFile.getAbsolutePath());
        javaParameters.getProgramParametersList().add(javaParameters.getMainClass());
        javaParameters.setMainClass(TEST_STARTER_CLASS_NAME);
        //set vm param to enable jrebel hotswap
        String jrebelAgentPath = GyroSettingsState.getInstance().getJrebelAgentPath();
        if (new File(jrebelAgentPath).exists()) {
            javaParameters.getVMParametersList().add("-agentpath:" + jrebelAgentPath);
        } else {
            LogUtil.warning(project, "Your Jrebel agentpath is not exist; Jrebel HotSwap is unsupported now; You should install the Jrebel Idea Plugin or set the Jrebel agentpath on the Gyro Settings Page[Preferences->Tools->Gyro Settings]");
        }
        return wasRun;
    }

    @Override
    public RunContentDescriptor doExecute(RunProfileState state, @NotNull ExecutionEnvironment env) {
        try {
            boolean wasRun = this.doPreExecute(state, env);
            if (wasRun) {
                LogUtil.log(env.getProject(), "GYRO reuse!");
                return null;
            } else {
                LogUtil.log(env.getProject(), "GYRO run!");
                return super.doExecute(state, env);
            }
        } catch (Throwable e) {
            LogUtil.error(env.getProject(), "some errors happened, please restart");
            throw new RuntimeException(e);
        }
    }

    @Override
    public Promise<RunContentDescriptor> doExecuteAsync(TargetEnvironmentAwareRunProfileState state, @NotNull ExecutionEnvironment env) {
        try {
            boolean wasRun = this.doPreExecute(state, env);
            if (wasRun) {
                LogUtil.log(env.getProject(), "GYRO reuse!");
                AsyncPromise<RunContentDescriptor> promise = new AsyncPromise<>();
                promise.setResult(null);
                return promise;
            } else {
                LogUtil.log(env.getProject(), "GYRO run!");
                return super.doExecuteAsync(state, env);
            }
        } catch (Throwable e) {
            LogUtil.error(env.getProject(), "some errors happened, please restart");
            throw new RuntimeException(e);
        }
    }

}
