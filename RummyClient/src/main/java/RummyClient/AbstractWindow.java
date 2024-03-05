package RummyClient;

import javax.swing.*;
import java.awt.*;

public abstract class AbstractWindow extends JFrame {

    //Current Window size
    public static int windowWidth = 1200;
    public static int windowHeight = 800;

    //Colors
    public static Color backgroundBlue = new Color(67,107,123);

    public AbstractWindow() {
        //Standard window init
        this.setTitle("Rummy");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(windowWidth,windowHeight);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    protected abstract void initListeners();

}
