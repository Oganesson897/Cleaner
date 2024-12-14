package me.oganesson.cleaner.dialog;

import me.oganesson.cleaner.CleanLauncher;
import me.oganesson.cleaner.net.CleanroomGetter;

import javax.swing.*;

public class StartDialog extends JDialog {

    public StartDialog() {
        setTitle("Cleaner - Close window to launch game / Clean instance");
        setBounds(100, 100, 450, 172);

        JPanel panel = new JPanel();
        JButton last = new JButton("Clean instance with Last Release");
        JButton action = new JButton("Clean instance with Last Build");
        last.addActionListener(e -> {
            CleanLauncher.cleanable = true;
            CleanLauncher.version = CleanroomGetter.getLastReleaseVersion();
            dispose();
        });
        action.addActionListener(e -> {
            CleanLauncher.cleanable = true;
            CleanLauncher.version = CleanroomGetter.getLastActionVersion();
            dispose();
        });
        panel.add(last);
        panel.add(action);
        add(panel);
    }

}
