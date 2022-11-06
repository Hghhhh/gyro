package com.goldenbell.gyro.settings;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class GyroSettingComponent {

    private final JPanel myMainPanel;
    private final JBTextField jrebelAgentPath = new JBTextField();

    public GyroSettingComponent() {
        myMainPanel = FormBuilder.createFormBuilder()
                .addLabeledComponent(new JBLabel("Enter Jrebel agent path:"), jrebelAgentPath, 1, false)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
    }

    public JPanel getPanel() {
        return myMainPanel;
    }

    public JComponent getPreferredFocusedComponent() {
        return jrebelAgentPath;
    }

    public String getJrebelAgentPathText() {
        return jrebelAgentPath.getToolTipText();
    }

    public void setJrebelAgentPathText(@NotNull String newText) {
        jrebelAgentPath.setText(newText);
    }

}
