package view;
import controller.GenerationFailed;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Saját osztály, ami a JFrame leszármazottja.
 * Ezzel interaktál a felhasználó.
 */
public class Window extends JFrame{
    /**
     * hány mező széltében
     */
    private int tilesN;
    /**
     *  hány mező hosszában
     */
    private int tilesM;
    /**
     * Ezzel jelenítjük meg az eredményt.
     */
    private final GridPanel grid;
    /**
     * Engedélyezi a generálást. Ha nincs kiválasztva kép, akkor ki false, különben true.
     */
    private boolean generatable;

    /**
     * Konstruktor
     */
    public Window(){
        //Meghívja a JFrame konstruktorát és beállítja a nevet
        super("Wavefunction-collapse");
        generatable = true;
        //Alapértéket állítunk a dimenziúknak, hogy egy ablakot létre tudjunk hozni.
        tilesN = tilesM = 10;
        //Beállítja a JFrame hátterét feketére.
        getContentPane().setBackground(Color.black);
        //Létrehoz egy tilesN * tilesM-es GridPanelt.
        grid = new GridPanel(tilesN, tilesM);
        //Megpróbálkuk beolvasni a legutolsó mentést, ha van ilyen.
        try{
            grid.open();
        }catch(GenerationFailed gF){
            //Ha nincs ilyen, akkor kiírjuk, hogy nem volt legutolsó mentés, és a generálás lehetőségét kikapcsoljuk.
            JOptionPane.showMessageDialog(null, "There was no previous save.");
            generatable = false;
        }
        //Átállítjuk az akkurátus értékekre a tilesN-t és M-et.
        tilesN = grid.getN();
        tilesM = grid.getM();
        //Beállítjuk az ablak méretét.
        setSize(grid.getWidth(), grid.getHeight());
        //Beállítjuk a grid-nek a háttérszínét.
        grid.setBackground(Color.darkGray);
        //Hozzáadjuk a JFrameünkhöz a grid-et, és a layoutban középre helyezzzük.
        add(grid, BorderLayout.CENTER);
        setJMenuBar(new MenuBar(this));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }

    /**
     * Legenerál egy képet, ha a generálhat
     */
    public void generate(){
        if(generatable) {
            remove(grid);
            grid.generate(tilesN, tilesM);
            setSize(grid.getWidth(), grid.getHeight());
            grid.setBackground(Color.darkGray);
            add(grid, BorderLayout.CENTER);
            repaint();
            revalidate();
        }
    }

    /**
     * továbbítja a JFileChooser-ből kapott fileokat a GridPanelnek
     * @param files a választott fájlok
     */
    public void giveFiles(File[] files){
        try{
            grid.passFiles(files);
            generatable = true;
        }catch(IOException ioX){
            JOptionPane.showMessageDialog(null, new JLabel("Valami hiba történt."), "HIBA!",JOptionPane.WARNING_MESSAGE);
        }
    }

    //Setterek
    public void setTilesN(int n){tilesN = n;}
    public void setTilesM(int m){tilesM = m;}

    //Getterek
    public int getTilesN(){return tilesN;}
    public int getTilesM(){return tilesM;}

    /**
     * Szerializálja a GridPanelt.
     */
    public void save(){if(generatable)grid.close();}
    public static void main(String[] args){
        Window w = new Window();
        w.setVisible(true);
    }
}