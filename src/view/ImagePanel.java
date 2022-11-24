package view;
import javax.swing.JPanel;
import java.awt.Graphics;

import controller.Wavefunction;


public class ImagePanel extends JPanel {
    private Wavefunction wve;
    private int width = 30, height = 30;
    private int state;

    public ImagePanel(){
        wve = new Wavefunction();
    }
    public ImagePanel(int st){
        wve = new Wavefunction();
        state = st;
    }
    public void setDimension(int w, int h){
        width = w; height = h;
    }
    public void setHeight(int h){ height = h; }
    public void setWidth(int w){ width = w; }
    public void setState(int st){ state = st;}
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        wve.set(state, wve.reSize(state, height, width));
        g.drawImage(wve.getImg(state), 0, 0, null);
    }
}
