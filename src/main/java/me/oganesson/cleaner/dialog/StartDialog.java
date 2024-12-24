package me.oganesson.cleaner.dialog;

import me.oganesson.cleaner.cleanroom.CleanroomGetter;

import javax.swing.*;
import java.awt.*;

public class StartDialog extends JDialog {

    public static String of() {
        return new StartDialog().version;
    }

    private String version;

    public StartDialog() {
        super((Frame) null, "Cleaner - Close window to launch game / Clean instance", true);
        setBounds(100, 100, 450, 172);

        JPanel panel = new JPanel();
        JButton release = new JButton("Clean instance with Last Release");
        JButton action = new JButton("Clean instance with Last Build");
        release.addActionListener(e -> {
            version = CleanroomGetter.getLastReleaseVersion();
            dispose();
        });
        action.addActionListener(e -> {
            version = CleanroomGetter.getLastActionVersion();
            dispose();
        });
        panel.add(release);
        panel.add(action);
        add(panel);

        pack();
        setAutoRequestFocus(true);
        setVisible(true);
    }

}
