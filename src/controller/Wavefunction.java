package controller;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.awt.Image;
import java.awt.Graphics2D;
import java.util.Random;
import java.util.Arrays;
import java.util.HashSet;

/**
 * A programjelentős része itt található.
 * Ez az osztály valósítja meg a wavefunction-collapse algoritmus.
 */
public class Wavefunction implements Serializable{
    @Serial
    private static final long serialVersionUID = 6529685098267757690L;

    /**
     * Belsőosztály, ami reprezentál egy mezőt a megoldásban.
     * entropy - hány értéket vehet még fel ez a mező(maximum annyit, amennyi kép van).
     * collapsedState - alapból -1, különben pedig annak a képnek az indexe szerepl itt, amire össze lett omlasztva ez a mező.
     */
    private class Tile implements Serializable{
        @Serial
        private static final long serialVersionUID = 6529685098267757691L;
        private int entropy;
        private int collapsedState;
        //Konstruktor
        public Tile(){collapsedState = -1;}
        //Setterek, getterek.
        public void setEntropy(int ent){entropy = ent;}
        public void setState(int st){collapsedState = st;}
        public int getEntropy(){return entropy;}
        public int getCollapsedState(){return collapsedState;}
        //A szerializáláshoz kellett
        @Override
        public String toString(){
            return entropy + "," + collapsedState;
        }
    }

    /**
     * A beolvasott képeket ebben tároljuk
     */
    private  ArrayList<Field> states;
    /**
     * A megoldás mátrix, ezt populáltatjuk tele képekkel.
     */
    private  Tile tiles[][];
    /**
     * Hosszanti db mező.
     */
    private int resW;
    /**
     * Magassági db mező.
     */
    private int resH;
    /**
     * Mennyi mezőt nem omlasztottunk még össze.
     */
    private int uncollapsedStates;
    private transient Random rand;

    /**
     * Létrehozza az egyes states-nek a szomszédsági mátrixát minden irányba.
     */
    private void generateNeighbourMtxs(){
        for(int i = 0; i < states.size(); ++i){
            for(int j = 0; j < states.size(); ++j){
                for(int k = 0; k < 4; ++k){
                    Field val = states.get(i);
                    boolean seting = compare(states.get(i).getImg(), states.get(j).getImg(), k);
                    val.setNeighbourVal(k, j, seting);
                    states.set(i, val);
                }
            }
        }
    }

    /**
     * Összehasonlítja img1-et és img2-őt dir alapján.
     * @param img1 első kép
     * @param img2 második kép
     * @param dir irány(0: fent, 1: jobb, 2: lent, 3: bal)
     * @return true, ha azonos pixeleik vannak, false különben
     */
    private boolean compare(BufferedImage img1, BufferedImage img2, int dir){
        int r1[] = new int[1], r2[] = new int[1];
        if(dir == 0 || dir == 2){
            switch(dir){
                case 0:
                    r1 = img1.getRGB(0,0, img1.getWidth(),1, null,0,img1.getWidth());
                    r2 = img2.getRGB(0, img2.getHeight() - 1, img2.getWidth(), 1, null, 0,img2.getWidth());
                    break;
                case 2:
                    r1 = img1.getRGB(0,img1.getHeight() - 1, img1.getWidth(), 1, null, 0, img1.getWidth());
                    r2 = img2.getRGB(0,0, img2.getWidth(), 1, null, 0,img2.getWidth());
                    break;
            }
        }else {
            switch(dir){
                case 1:
                    r1 = img1.getRGB(img1.getWidth() - 1, 0, 1, img1.getHeight(), null, 0, img1.getHeight());
                    r2 = img2.getRGB(0, 0, 1, img2.getHeight(), null, 0, img2.getHeight());
                    break;
                case 3:
                    r1 = img1.getRGB(0, 0, 1, img1.getHeight(), null, 0, img1.getHeight());
                    r2 = img2.getRGB(img2.getWidth() - 1, 0, 1, img2.getHeight(), null, 0, img2.getHeight());
                    break;
            }
        }

        return Arrays.equals(r1,r2);
    }

    /**
     * Beállítja az alapértelmezett értékeket a mezőknek.
     * @param resultH db mező szélességben
     * @param resultW db mező magasságban
     */
    private void initializeEntropy(int resultH, int resultW){
        tiles = new Tile[resultH][resultW];
        for(int i = 0; i < resultH; ++i)
            for(int j = 0; j < resultW; ++j) {
                tiles[i][j] = new Tile();
                tiles[i][j].setEntropy(states.size());
            }
    }

    /**
     * Átállítja az im ArrayListben lévő idx-edik BufferedImage dimenzióját a newH magasságra, és newW szélességre, majd visszaadja ezt a transzformált képet.
     *
     * @param idx states belül hányadik képről van szó
     * @param newH új képmagasság
     * @param newW új képszélesség
     * @return az új BufferedImage-t adja vissza.
     */
    public BufferedImage reSize(int idx, int newH, int newW) {
        Image temp = states.get(idx).getImg().getScaledInstance(newW, newH, Image.SCALE_DEFAULT);
        BufferedImage bimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
        //Alapbol nem lehet valtoztani a BufferedImagen, igy a createGraphics fgv segitsegevel, egy Graphics2D objektumot hoz letre
        //amelyre lehet mar rajzolni es modositani.
        Graphics2D g2d = bimg.createGraphics();
        g2d.drawImage(temp, 0, 0, null);
        g2d.dispose();

        return bimg;
    }
    /**
     * Vissazaadja a BufferedImage ImageIcon verzióját. Erre azért lehet szükség, mert JPanelre, csak ImageIcont lehet felrakni és BufferedImaget nem.
     * @param x A kész képnek az adott sorban az x.dik eleme.
     * @param y A kész képnek az adott oszlopban az y.dik eleme.
     * @return Az (x,y) koordinátájú képet ImageIconként visszadja
     */
    public ImageIcon getImgAsIcon(int x, int y){
        return new ImageIcon(states.get(tiles[x][y].getCollapsedState()).getImg());
    }

    /**
     *    Visszaadja az im meretet.
     */
    public int size(){return states.size();}

    /**
     * Beállítja az idx-edik elemet image-re
     * @param idx Annak az idx-e, amelyiket ki szeretnénk cserélni.
     * @param image A kép, amire cserélünk.
     */
    public void set(int idx, BufferedImage image){
        Field ret = new Field(image, states.get(idx).getIdx(), states.size(), states.get(idx).getAbsolutePath());
        //Át kell másolni a szomszédsági vektorok értékeit is
        for(int i = 0; i < 4; ++i)
            for(int j = 0; j < states.size(); ++j)
                ret.setNeighbourVal(i, j, states.get(idx).getNeighbourMtxVal(j, i));

        states.set(idx,ret);
    }

    /**
     * Visszaadja, hogy egy mezőnek hány értéke lehet
     * @param y Magassági koordináta
     * @param x Szélességi koordináta
     * @return Mező összes értéke, nem akkurátus.
     */
    private ArrayList<Integer> getNeighbourStates(int y, int x){
        ArrayList<Integer> possibleStates = new ArrayList<>();
        int st = 0;
        if(y == resH - 1){
            st = tiles[y - 1][x].getCollapsedState();
            //A cella fölötti cellának megnézzük, hogy alulról milyen értékek csatlakozhatnak hozzá
            if(st != -1)possibleStates.add(states.get(st).getPossibleStates(2));
            else possibleStates.add((int)Math.pow(2, states.size()) - 1);
        }else if(y == 0){
            st = tiles[y + 1][x].getCollapsedState();
            if(st != -1)possibleStates.add(states.get(st).getPossibleStates(0));
            else possibleStates.add((int)Math.pow(2, states.size()) - 1);
        }else{
            st = tiles[y + 1][x].getCollapsedState();
            if(st != -1)possibleStates.add(states.get(st).getPossibleStates(0));
            else possibleStates.add((int)Math.pow(2, states.size()) - 1);
            st = tiles[y - 1][x].getCollapsedState();
            if(st != -1)possibleStates.add(states.get(st).getPossibleStates(2));
            else possibleStates.add((int)Math.pow(2, states.size()) - 1);
        }

        if(x == 0){
            st = tiles[y][x + 1].getCollapsedState();
            //A cella jobb oldali cellának megnézzük, hogy balról milyen értékek csatlakozhatnak hozzá
            if(st != -1)possibleStates.add(states.get(st).getPossibleStates(3));
            else possibleStates.add((int)Math.pow(2, states.size()) - 1);
        }else if(x == resW - 1){
            st = tiles[y][x - 1].getCollapsedState();
            //A cella bal oldali cellának megnézzük, hogy jobbról milyen értékek csatlakozhatnak hozzá
            if(st != -1)possibleStates.add(states.get(st).getPossibleStates(1));
            else possibleStates.add((int)Math.pow(2, states.size()) - 1);
        }else{
            st = tiles[y][x - 1].getCollapsedState();
            if(st != -1)possibleStates.add(states.get(st).getPossibleStates(1));
            else possibleStates.add((int)Math.pow(2, states.size()) - 1);
            st = tiles[y][x + 1].getCollapsedState();
            if(st != -1)possibleStates.add(states.get(st).getPossibleStates(3));
            else possibleStates.add((int)Math.pow(2, states.size()) - 1);
        }

        return possibleStates;
    }

    /**
     * Megadja, hogy melyik elemek a statesből kerülhetnek a mezőbe.
     * @param st Ezzel nézi, meg, hogy melyikek igazak.
     * @return Melyik képek lehetnek itt.
     */
    private ArrayList<Integer> convertStatesToArr(int st){
        ArrayList<Integer> ret = new ArrayList<>();
        for(int i = 0; i < states.size(); ++i){
            int and = st & (int)Math.pow(2, i);
            if(and != 0) ret.add(i);
        }

        return ret;
    }

    /**
     * Kiszámolja az összes lehetséges érték metszetét.
     * Mivel négy irányban van szomszédsági vektor, ezért négy irány vektorának a metszetét kell venni.
     * @param sets A szomszédsági mátrix
     * @return A metszet
     */
    private HashSet<Integer> calculateIntersection(ArrayList<HashSet<Integer>> sets){
        HashSet<Integer> tmp = sets.get(0);
        for(int i = 0; i < sets.size(); ++i) if(sets.get(i).size() < tmp.size()) tmp = sets.get(i);
        for(int i = 0; i < sets.size(); ++i) tmp.retainAll(sets.get(i));

        return tmp;
    }

    /**
     * Megadja, hogy pontosan milyen értékeket vehet fel egy mező.
     * @param y Magassági koordináta.
     * @param x Szélességi koordináta.
     * @return Lehetséges állapotok.
     */
    private HashSet<Integer> getPossibleStates(int y, int x){
        ArrayList<Integer> possibleStates = getNeighbourStates(y,x);
        ArrayList<HashSet<Integer>> finalPossible = new ArrayList<>();
        for(int i = 0; i < possibleStates.size(); ++i) finalPossible.add(i, new HashSet<>(convertStatesToArr(possibleStates.get(i))));
        return calculateIntersection(finalPossible);
    }

    /**
     * Végig megy a tiles-on és updateli az összes mező entrópiáját.
     * @throws GenerationFailed Ha hibába ütközünk.
     */
    private void updateEntropies() throws GenerationFailed{
        for(int y = 0; y < resH; ++y){
            for(int x = 0; x < resW; ++x){
                if(tiles[y][x].getEntropy() != 1) {
                    HashSet<Integer> result = getPossibleStates(y, x);
                    if (result.isEmpty()) {
                        throw new GenerationFailed();
                    }else if(result.size() == 1){
                        tiles[y][x].setState((int)result.toArray()[0]);
                        uncollapsedStates--;
                        tiles[y][x].setEntropy(result.size());
                    }
                    tiles[y][x].setEntropy(result.size());
                }
            }
        }
    }

    /**
     * Megkeresi a legkisebb entrópiájú mezőt és annak random választ egy képet.
     * @throws GenerationFailed Ha az entrópia 0 lenne egy mezőnél.
     */
    private void collapseLowestEnt() throws GenerationFailed{
        int x = 0, y = 0, lowestEnt = states.size() + 1;
        for(int i = 0; i < resH; ++i){
            for(int j = 0; j < resW; ++j){
                if((tiles[i][j].getEntropy() < lowestEnt) && (tiles[i][j].getEntropy() != 1)){
                    lowestEnt = tiles[i][j].getEntropy();
                    y = i;
                    x = j;
                }
            }
        }
        //Csak akkor lesz egy cellának az entrópiája 0, ha nem lehet összeomlasztani, ekkor hibát dobunk.
        if(tiles[y][x].getEntropy() == 0) throw new GenerationFailed();
        //Ha minden cella még szabad, akkor bármelyik állapottal összeomlaszthatjuk.
        if(tiles[y][x].getEntropy() == states.size()) {
            tiles[y][x].setState(rand.nextInt(tiles[y][x].getEntropy()));
            tiles[y][x].setEntropy(1);
        }else{
            HashSet<Integer> result = getPossibleStates(y,x);
            if(result.isEmpty()) throw new GenerationFailed();
            else{
                Object[] tmp = result.toArray();
                int finalState = (int)tmp[rand.nextInt(result.size())];
                tiles[y][x].setState(finalState);
                tiles[y][x].setEntropy(1);
            }
        }
        if(tiles[y][x].getCollapsedState() == -1 )
            System.out.println("Yikes");
        updateEntropies();
    }

    /**
     * Legenerálja az eredményt, százszor próbálkozik, ha ennyiszer nem jár sikerrel, akkor azt mondja, hogy a megadott képekkel nem lehet generálni.
     * @throws GenerationFailed Ha nem jár sikerrel az algoritmus.
     */
    public void generateResult() throws GenerationFailed{
        if(states.isEmpty()) return;
        generateNeighbourMtxs();
        int tries = 0;
        initializeEntropy(resH, resW);
        uncollapsedStates = resH * resW;

        //Addig megy a ciklus, ameddig van össze nem omlasztott cella
        while(uncollapsedStates > 0){
            try {
                collapseLowestEnt();
            }catch(GenerationFailed gfEx){
                tries++;
                if(tries >= 100) throw new GenerationFailed();
                initializeEntropy(resH, resW);
                uncollapsedStates = resH * resW;
            }
            uncollapsedStates--;
        }
    }
    public Wavefunction(int resultW, int resultH){
        resW = resultW;
        resH = resultH;
        states = new ArrayList<>();
        rand = new Random();
        //getImgs();
    }

    /**
     * Ha változtatni szeretnénk a méreteken
     * @param resW Szélességi db mezők
     * @param resH Magassági db mezők
     */
    public void reSize(int resW, int resH){
        this.resW = resW;
        this.resH = resH;
    }

    /**
     * States a megadott files-al lesz azonos.
     * @param files A képek, amiket a felhasználó választott.
     * @throws IOException Ha nem létezik egy file a filesból
     */
    public void getFiles(File[] files) throws IOException{
        states = new ArrayList<>();
        int i = 0;
        for(File file: files) {
            states.add(new Field(ImageIO.read(file), i, files.length, file.getAbsolutePath()));
            ++i;
        }
    }

    //Getter
    public int getW(){return resW;}
    public int getH(){return resH;}

    //A szerializációt felülírja, így helyesen működik a szerializáció
    @Serial
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException{
        ois.defaultReadObject();
        rand = new Random();
    }
    //A szerializációt felülírja, így helyesen működik a szerializáció
    @Serial
    private void writeObject(ObjectOutputStream oos) throws IOException{
        oos.defaultWriteObject();
    }
}
