package view;

import javax.swing.*;
import java.awt.GridLayout;
import java.io.*;

import controller.GenerationFailed;
import controller.Wavefunction;

/**
 * JPanelből származtatott osztály, mely GridLayout-ot használ
 * Ebbe pakoljuk bele az algoritmus által generált értékeket, majd jelenítjük meg a Window segítségével.
 */
public class GridPanel extends JPanel{
    /**
     * A Panel szélessége és magassága
     */
    private int width, height;
    private final int qualif = 50;
    //Getterek
    public int getWidth(){return width;}
    public int getHeight(){return height;}
    //Az algoritmus egy példánya
    private Wavefunction samples;

    /**
     * létrehoz egy n*m méretű JPanelt
     *
     * @param n db mező széltében
     * @param m db mező magasságban
     */
    public GridPanel(int n, int m){
        samples = new Wavefunction(n,m);
        setLayout(new GridLayout(m, n, 0, 0));
        width = qualif*n;
        height = qualif*m + 50;
        for(int i = 0; i < samples.size(); ++i){
            samples.set(i % samples.size(), samples.reSize(i % samples.size(), qualif, qualif));
        }
    }

    private void reset(){
        setLayout(new GridLayout(samples.getH(), samples.getW(), 0, 0));
        width = qualif*samples.getW();
        height = qualif*samples.getH() + 50;
        for(int i = 0; i < samples.size(); ++i){
            samples.set(i % samples.size(), samples.reSize(i % samples.size(), qualif, qualif));
        }
        for(int i = 0; i < samples.getH(); ++i) {
            for(int j = 0; j < samples.getW(); ++j) {
                add(new JLabel(samples.getImgAsIcon(i, j)));
            }
        }
    }

    /**
     * Legenerálunk egy megoldást.
     * @param tN db, szélességben
     * @param tM db, magasságban
     */
    public void generate(int tN, int tM){
        removeAll();
        setLayout(new GridLayout(tM,tN, 0, 0));
        setSize(0,0);
        samples.reSize(tN, tM);
        width = tN*qualif;
        height = tM*qualif + 50;
        try {
            samples.generateResult();
            for(int i = 0; i < samples.size(); ++i){
                samples.set(i % samples.size(), samples.reSize(i % samples.size(), qualif, qualif));
            }
            for(int i = 0; i < tM; ++i) {
                for(int j = 0; j < tN; ++j) {
                    add(new JLabel(samples.getImgAsIcon(i, j)));
                }
            }

            repaint();
        }catch(GenerationFailed gfEx){
            System.out.println("nem lehetett létrehozni lol.");
        }
    }


    public void open() throws GenerationFailed{
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("imgDta.ser"));
            samples = (Wavefunction) ois.readObject();
            reset();
        }catch(FileNotFoundException fnfEX){
            throw new GenerationFailed();
        }catch(IOException ioEX){
            throw new GenerationFailed();
        }catch(ClassNotFoundException cnfEX){
            throw new GenerationFailed();
        }
    }
    public void close(){
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("imgDta.ser"));
            oos.writeObject(samples);
        }catch (IOException ioX){ioX.printStackTrace();}
    }

    /**
     *
     * @return db mező széltében.
     */
    public int getN(){return samples.getW();}

    /**
     * @return db mező magasságban.
     */
    public int getM(){return samples.getH();}

    /**
     * A files fileokat fogja használni sampleként az algoritmus.
     * @param files kiválasztott fileok
     * @throws IOException ha nem léteznek a fileok, hibát dobunk.
     */
    public void passFiles(File[] files) throws IOException { samples.getFiles(files);}
}
