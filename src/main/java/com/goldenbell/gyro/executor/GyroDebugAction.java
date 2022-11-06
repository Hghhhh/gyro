package com.goldenbell.gyro.executor;

import com.intellij.execution.Executor;
import com.intellij.execution.dashboard.actions.ExecutorAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class GyroDebugAction extends ExecutorAction {

    @Override
    protected Executor getExecutor() {
        return GyroDebuggerExecutor.getGyroDebugExecutorInstance();
    }

    @Override
    protected  void update(@NotNull AnActionEvent anActionEvent, boolean b) {
        anActionEvent.getPresentation().setEnabledAndVisible(true);
    }
}
