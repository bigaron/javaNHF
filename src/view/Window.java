package view;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;

public class Window extends JFrame{
    public Window(){
        super("Wavefunction-collapse");
        setSize(1280, 720);
        JPanel panel = new ImagePanel();
        JPanel menuBar = new Options();
        JPanel grid = new GridPanel();
        panel.setBackground(Color.darkGray);
        //panel.repaint();
        add(menuBar, BorderLayout.NORTH);
        add(grid, BorderLayout.CENTER);
        add(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args){
        Window w = new Window();
        w.setVisible(true);
    }
}