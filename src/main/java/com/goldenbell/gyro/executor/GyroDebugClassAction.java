package com.goldenbell.gyro.executor;

import com.intellij.execution.*;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class GyroDebugClassAction extends ExecutorRegistryImpl.ExecutorAction {
    protected GyroDebugClassAction() {
        super(GyroDebuggerExecutor.getGyroDebugExecutorInstance());
    }

    @NotNull
    protected RunnerAndConfigurationSettings getSelectedConfiguration(@NotNull AnActionEvent anActionEvent) {
        return RunManager.getInstance(anActionEvent.getProject()).getSelectedConfiguration();
    }
}
