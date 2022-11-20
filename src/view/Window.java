package view;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Color;

public class Window extends JFrame{
    public Window(){
        super("Wavefunction-collapse");
        setSize(1280, 720);
        JLabel label = new JLabel("LSD");
        JPanel panel = new JPanel();
        panel.setBackground(Color.pink);
        panel.add(label);
        add(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args){
        Window w = new Window();
        w.setVisible(true);
    }
}