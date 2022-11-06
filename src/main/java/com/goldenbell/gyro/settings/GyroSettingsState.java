package com.goldenbell.gyro.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class GyroSettingsState implements PersistentStateComponent<GyroSettingsState> {
    private String jrebelAgentPath;

    public GyroSettingsState() {
        super();
        StringBuilder jrebelAgentPathBuilder = new StringBuilder(PathManager.getPluginsPath());
        jrebelAgentPathBuilder.append(File.separator);
        jrebelAgentPathBuilder.append("jr-ide-idea");
        jrebelAgentPathBuilder.append(File.separator);
        jrebelAgentPathBuilder.append("lib");
        jrebelAgentPathBuilder.append(File.separator);
        jrebelAgentPathBuilder.append("jrebel6");
        jrebelAgentPathBuilder.append(File.separator);
        jrebelAgentPathBuilder.append("lib");
        jrebelAgentPathBuilder.append(File.separator);
        jrebelAgentPathBuilder.append("libjrebel64.dylib");
        jrebelAgentPath = jrebelAgentPathBuilder.toString();
    }

    public static GyroSettingsState getInstance() {
        return ApplicationManager.getApplication().getService(GyroSettingsState.class);
    }

    public String getJrebelAgentPath() {
        return jrebelAgentPath;
    }

    @Override
    public @Nullable GyroSettingsState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull GyroSettingsState state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}
