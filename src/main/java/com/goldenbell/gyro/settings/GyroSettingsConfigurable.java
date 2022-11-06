package com.goldenbell.gyro.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class GyroSettingsConfigurable implements Configurable {

    private GyroSettingComponent gyroSettingComponent;

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "Gyro Settings";
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return gyroSettingComponent.getPreferredFocusedComponent();
    }

    @Override
    public @Nullable JComponent createComponent() {
        gyroSettingComponent = new GyroSettingComponent();
        return gyroSettingComponent.getPanel();
    }

    @Override
    public boolean isModified() {
        GyroSettingsState settingState = GyroSettingsState.getInstance();
        return !gyroSettingComponent.getJrebelAgentPathText().equals(settingState.getJrebelAgentPath());
    }

    @Override
    public void apply() throws ConfigurationException {
        GyroSettingsState settingState = GyroSettingsState.getInstance();
        gyroSettingComponent.setJrebelAgentPathText(settingState.getJrebelAgentPath());
    }

    @Override
    public void reset() {
        GyroSettingsState settingState = GyroSettingsState.getInstance();
        gyroSettingComponent.setJrebelAgentPathText(settingState.getJrebelAgentPath());
    }

    @Override
    public void disposeUIResources() {
        gyroSettingComponent = null;
    }
}
