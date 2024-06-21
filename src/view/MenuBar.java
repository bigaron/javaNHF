package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

/**
 * JMenuBarból származtatott osztály, itt dolgozzuk fel a felhasználó inputjainak nagy százalékát.
 */
public class MenuBar extends JMenuBar{
    /**
     * Konstruktor, ahol a JMenuBar-hoz rendelek minden interakciót.
     * A JMenuitemekhez hozzárendeltem lambdafüggvényekkel actionListenereket, melyek a respektív függvényeket
     * meghívják, amint rájuk kattintanak.
     * @param frm Window, aminek függvényeit meghívja az osztály
     */
    public MenuBar(Window frm){
        JMenuItem exit = new JMenuItem("Exit");
        //Actionlistener, amely kilép a programból, ha rányomnak.
        exit.addActionListener(e -> Window.getWindows()[0].dispatchEvent(new WindowEvent(Window.getWindows()[0], WindowEvent.WINDOW_CLOSING)));

        JMenuItem file = new JMenuItem("Choose files");
        JMenuItem save = new JMenuItem("Save");
        JMenuItem dim = new JMenuItem("Set Dimension");
        JMenuItem gen = new JMenuItem("Generate!");
        JTextField TxtDimW = new JTextField(20);
        JTextField TxtDimH = new JTextField(20);

        TxtDimH.setText(Integer.toString(frm.getTilesM()));
        TxtDimW.setText(Integer.toString(frm.getTilesN()));

        JPanel PnlDim = new JPanel();
        PnlDim.add(new JLabel("N: "));
        PnlDim.add(TxtDimH);
        PnlDim.add(new JLabel("M: "));
        PnlDim.add(TxtDimW);
        //Ha szerializálni szeretne a felhasználó, meghívja a JFrame save függvényét
        save.addActionListener(e->{
            frm.save();
        });
        //Bekéri a felhasználótól a dimenziókat és ezt tovább passzolja a Windownak
        dim.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, PnlDim);
            frm.setTilesM(Integer.parseInt(TxtDimH.getText()));
            frm.setTilesN(Integer.parseInt(TxtDimW.getText()));
        });
        //JFileChooser segítségével beadja a felhasználó a fileokat, amiket szeretne használni.
        file.addActionListener(e ->{
            JFileChooser jfc = new JFileChooser();
            jfc.setMultiSelectionEnabled(true);
            int retVal = jfc.showOpenDialog(null);
            //Csak, akkor mentjük el a fileokat, ha az OK gombbal zárta be a JFileChoosert a felhasználó.
            if(retVal == JFileChooser.APPROVE_OPTION){
                frm.giveFiles(jfc.getSelectedFiles());
            }
        });

        gen.addActionListener(e -> frm.generate());
        JMenu operations = new JMenu("operations");
        operations.add(file);
        operations.add(save);
        operations.add(dim);
        //Keret a JMenuItemeknek
        operations.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        exit.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        gen.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        //Jelenítse is meg a swing
        operations.setBorderPainted(true);
        exit.setBorderPainted(true);
        gen.setBorderPainted(true);
        add(exit);
        add(operations);
        add(gen);
        //Újrafestjük a komponenseket
        repaint();
    }
}
