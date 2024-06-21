package controller;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

/**
 * Egy Mezőt reprezentál a wavefunction-collapseben
 */
public class Field implements Serializable {
    @Serial
    private static final long serialVersionUID = 6529685098267757692L;

    /**
     * Belsőosztály, ami egy szomszédságivektort ad meg egy adott irányba
     */
    private class NeighbourMatrix implements Serializable {
        @Serial
        private static final long serialVersionUID = 6529685098267757693L;
        private  int len;
        private  boolean mtx[];
        public NeighbourMatrix(int len){
            this.len = len;
            mtx = new boolean[len];
        }

        public boolean get(int idx){ return mtx[idx]; }
        public void set(int idx, boolean val){ mtx[idx] = val; }


        /**
         * Ha egy érték igaz, akkor a 2 annyiadikat adjuk hozzá
         */
        public int getCorrectValues(){
            int ret = 0;
            for(int i = 0; i < len; ++i){
                if(mtx[i]) ret += Math.pow(2, i);
            }

            return ret;
        }
        //Szerializáláshoz kellett
        @Override
        public String toString(){
            int sum = 0;
            for(int i = 0; i < len; ++i) sum += mtx[i] ? Math.pow(2,i) : 0;
            return len + "," + sum;
        }
    }

    /**
     * mezőt reprezentáló kép
     */
    private transient BufferedImage img;
    /**
     * mezőt reprezentáló kép elérési útja
     */
    private  String path;
    private  int idx;
    /**
     * A szomszédsági mátrix.
     */
    private  NeighbourMatrix nMtx[];


    public Field(BufferedImage bImg, int idx, int size, String path){
        nMtx = new NeighbourMatrix[4];
        for(int i = 0; i < 4; ++i) nMtx[i] = new NeighbourMatrix(size);
        img = bImg;
        this.idx = idx;
        this.path = path;
    }
    public BufferedImage getImg(){ return img;}
    public int getIdx(){return idx;}
    public boolean getNeighbourMtxVal(int idx, int dir){ return nMtx[dir].get(idx); }
    public int getPossibleStates(int dir){ return nMtx[dir].getCorrectValues();}
    public void setNeighbourVal(int dir, int idx, boolean val){nMtx[dir].set(idx, val);}
    public String getAbsolutePath(){ return path;}

    @Override
    public String toString(){
        StringBuilder ret = new StringBuilder(path + "," + idx + ",");
        for (NeighbourMatrix mtx : nMtx) {
            ret.append(mtx.toString()).append(",");
        }

        return ret.toString();
    }
    @Serial
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException{
        ois.defaultReadObject();
        img = ImageIO.read(new File(path));
    }
}
