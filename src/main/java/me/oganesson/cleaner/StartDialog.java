package me.oganesson.cleaner;

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
            dispose();
        });
        action.addActionListener(e -> {
            CleanLauncher.cleanable = true;
            dispose();
        });
        panel.add(last);
        add(panel);
    }

}
