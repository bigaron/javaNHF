package controller;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;
import java.nio.Buffer;
import java.util.ArrayList;
import java.awt.Image;
import java.awt.Graphics2D;

public class Wavefunction {
    //A kep egy pixelenek a szinet reprezentalja, rgb szineket tarol
    private class Pixel {
        public int red, green, blue;
        //Azert, kell, hogy lehessen ertek nelkul is letrehozni ilyen peldanyt
        public Pixel(){}
        //Parameteres konstruktor, hogy inline lehessen megadni az osztalyt
        public Pixel(int r, int g, int b){
            red = r;
            green = g;
            blue = b;
        }
        //Utolag beallitani az erteket
        public void setVal(int r, int g, int b){
            red = r;
            green = g;
            blue = b;
        }
    }
    private ArrayList<BufferedImage> im;

    //Visszaadja ixm kepnek az (X,Y) helye levo RGB erteket.
    private Pixel getPixel(BufferedImage ixm, int X, int Y){
        int rgb = ixm.getRGB(X, Y);
        //ARGB sorrendben tarolja egy intben az ertekeket a java, igy ay Redhez 16 bitet, Bluehoz 8 bitet, Greenhez pedig csak siman 0xFF-el kell maszkolni.
        //0xFF binarisan a 1111_1111-nek felel meg.
        return new Pixel(rgb >> 16 & 0xFF, rgb >> 8 & 0xFF, rgb & 0xFF);
    }
    //DEBUGhoz kell, majd
    //TODO: torolni leadas elott
    /*private void printPix(BufferedImage imag){
        Pixel currPi = new Pixel();
        for(int i = 0; i < imag.getHeight(); ++i){
            System.out.print(i + ". ");
            for(int j = 0; j < imag.getWidth(); ++j){
                currPi = getPixel(imag, j, i);
                System.out.print(" r: " + currPi.red + ",g: " + currPi.blue + ",b: " + currPi.green + " | ");
            }
            System.out.println("");
        }
    }*/

    //Atallitja az im ArrayListben levo idx-edik BufferedImage dimenziojat az newH magassagra es newW szelessegre
    //majd visszaadja ezt a transzformalt kepet.
    public BufferedImage reSize(int idx, int newH, int newW){
        Image temp = im.get(idx).getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage bimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
        //Alapbol nem lehet valtoztani a BufferedImagen, igy a createGraphics fgv segitsegevel, egy Graphics2D objektumot hoz letre
        //amelyre lehet mar rajzolni es modositani.
        Graphics2D g2d = bimg.createGraphics();
        g2d.drawImage(temp, 0, 0,null);
        g2d.dispose();

        return bimg;
    }
    //Beolvassa a kepeket
    private void getImgs(){
        try {
            im.add(ImageIO.read(new File(new File("src","imgs"), "1.png")));
            im.add(ImageIO.read(new File(new File("src","imgs"), "2.png")));
            im.add(ImageIO.read(new File(new File("src","imgs"), "3.png")));
            im.add(ImageIO.read(new File(new File("src","imgs"), "4.png")));

            //for(BufferedImage itm : im) printPix(itm);
           // for(int i = 0; i < im.size(); ++i) im.set(i, reSize(i, 100, 100));
        }catch(IOException ioX) {
            ioX.printStackTrace();
        }
    }

    //Visszadja az idx-edik BufferedImage-t az im-bol.
    public BufferedImage getImg(int idx){
        return idx > im.size() ? null : idx < 0 ? null : im.get(idx);
    }

    //Visszaadja az im meretet.
    public int size(){return im.size();}
    public void set(int idx, BufferedImage image){im.set(idx, image);}
    public Wavefunction(){
        im = new ArrayList<>();
        getImgs();
    }
}
