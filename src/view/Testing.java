package view;
import controller.Field;
import controller.Wavefunction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;


public class Testing {
    private final int N = 12, M = 10;
    Window wind = new Window();
    GridPanel p = new GridPanel(N, M);
    Wavefunction wf = new Wavefunction(N, M);
    Field f;
    File fi;
    int idx;
    @Before
    public void setup(){
        fi = new File(new File("src","imgs"), "1.png");
        idx = 0;
        wind.setTilesM(M);
        wind.setTilesN(N);
        try{
            f = new Field(ImageIO.read(fi), idx, 1, fi.getAbsolutePath());
        }catch(IOException ioX){
            ioX.printStackTrace();
        }
    }
    @Test
    public void testWindowN(){
        Assert.assertEquals("Window set N for width works properly.", wind.getTilesN(), N);
    }
    @Test
    public void testWindowM(){
        Assert.assertEquals("Window set M for width works properly.", wind.getTilesM(), M);
    }

    @Test
    public void testGridM(){
        Assert.assertEquals("Grid gets height properly calculated.", p.getHeight(), 50*M + 50);
    }

    @Test
    public void testGridN(){
        Assert.assertEquals("Grid gets width properly calculated.", p.getWidth(), 50*N);
    }

    @Test
    public void testWaveWidth(){ Assert.assertEquals("Check if wavelength algorithm has right amount of tiles horizontally", wf.getW(), N);}

    @Test
    public void testWaveHeight(){ Assert.assertEquals("Check if wavelength algorithm has right amount of tiles vertically", wf.getH(), M);}

    @Test
    public void testFieldPath(){ Assert.assertEquals("Checks if the images path is equal to the one stored in the object.", f.getAbsolutePath(), fi.getAbsolutePath());}

    @Test
    public void testFieldIdx(){ Assert.assertEquals("Checks if the given idx is what is actually stored in the object", f.getIdx(), idx);}

    @Test
    public void testPossibleNeighbours(){Assert.assertEquals("Checks if the neighbourmtx for a given direction is calculated properly.", f.getPossibleStates(idx), 0);}
    @Test
    public void testSingleNeighbourVal(){Assert.assertFalse("Checks if the a value of the neighbourmtx is calculated propely.", f.getNeighbourMtxVal(idx, 0));}
}
