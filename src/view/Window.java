package view;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;

import controller.Wavefunction;

public class Window extends JFrame{
    private Wavefunction func = new Wavefunction();
    public Window(){
        super("Wavefunction-collapse");
        setSize(1280, 720);
        JPanel panel = new JPanel();
        panel.setBackground(Color.pink);
        add(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args){
        Window w = new Window();
        w.setVisible(true);
    }
}