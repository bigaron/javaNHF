package view;

import javax.swing.JPanel;
import java.awt.GridLayout;

public class GridPanel extends JPanel{
    //TODO: nem jol displayeli a stateket.
    public GridPanel(){
        setLayout(new GridLayout(3, 3));
        add(new ImagePanel(0));
        add(new ImagePanel(0));
        add(new ImagePanel(0));
        add(new ImagePanel(0));
        add(new ImagePanel(0));
        add(new ImagePanel(0));

    }
}
