import com.formdev.flatlaf.intellijthemes.FlatSolarizedDarkIJTheme;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        //enable look and feel
        try {
            UIManager.setLookAndFeel( new FlatSolarizedDarkIJTheme() );
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }

        //run menu window
        try{
            SwingUtilities.invokeLater(() -> new MenuWindow());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
}