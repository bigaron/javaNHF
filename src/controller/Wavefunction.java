package controller;

import javax.imageio.ImageReader;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;

public class Wavefunction {
    private ArrayList<BufferedImage> im;
        
    private void getImgs(){
        try {
            im.add(ImageIO.read(new File("1.png")));
            im.add(ImageIO.read(new File("2.png")));
            im.add(ImageIO.read(new File("3.png")));
            im.add(ImageIO.read(new File("4.png")));

        }catch(IOException ioX) {
            ioX.printStackTrace();
        }
    }
    public Wavefunction(){
        im = new ArrayList<>();
    }
}
