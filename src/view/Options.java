package view;

import javax.swing.JPanel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class Options extends JPanel {
    private JMenuBar bar = new JMenuBar();

    public Options(){
        JMenu operations = new JMenu("operations");
        operations.add(new JMenuItem("Choose files"));
        operations.add(new JMenuItem("Save"));
        operations.add(new JMenuItem("Set Dimension"));
        bar.add(operations);
        bar.add(new JMenuItem("Exit"));
        add(bar);
    }
}
