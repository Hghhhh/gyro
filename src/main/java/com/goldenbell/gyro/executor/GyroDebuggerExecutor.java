package com.goldenbell.gyro.executor;

import com.intellij.execution.Executor;
import com.intellij.execution.ExecutorRegistry;
import com.intellij.execution.executors.DefaultDebugExecutor;
import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

import static com.goldenbell.gyro.common.CommonConstants.RUNNER_ID;

public class GyroDebuggerExecutor extends DefaultDebugExecutor {

    @NotNull
    @Override
    public String getToolWindowId() {
        return RUNNER_ID;
    }

    @NotNull
    @Override
    public Icon getToolWindowIcon() {
        return IconLoader.getIcon("icons/toolWindowGyroDebugger.svg", this.getClass());
    }

    @Override
    @NotNull
    public Icon getIcon() {
        return IconLoader.getIcon("icons/startGyroDebugger.svg", this.getClass());
    }

    @Override
    public Icon getDisabledIcon() {
        return IconLoader.getDisabledIcon(getIcon());
    }

    @Override
    @NotNull
    public String getActionName() {
        return "Gyro Debug";
    }

    @Override
    @NotNull
    public String getId() {
        return RUNNER_ID;
    }

    @Override
    public String getContextActionId() {
        return "GyroDebugClassAction" + "NotSupportNow";
    }

    @Override
    @NotNull
    public String getStartActionText() {
        return "Gyro Debug";
    }

    @Override
    public String getDescription() {
        return "Gyro Debug";
    }

    @Override
    public boolean isSupportedOnTarget() {
        return RUNNER_ID.equalsIgnoreCase(getId());
    }

    public static Executor getGyroDebugExecutorInstance() {
        return ExecutorRegistry.getInstance().getExecutorById(RUNNER_ID);
    }
}
