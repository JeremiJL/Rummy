package RummyClient;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuWindow extends AbstractWindow {

    //Components
    private JPanel panelMain;
    private JLabel labelTitle;
    private JButton buttonHost;
    private JButton buttonJoin;
    private JButton buttonOptions;
    private JButton buttonProfile;

    public MenuWindow() {
        //Invoking abstract window init
        super();
        //Setting contentPanel
        this.setContentPane(panelMain);
        //Creating action listeners
        initListeners();
    }


    @Override
    protected void initListeners() {

        //Button Join - sends you to gameplay screen
        buttonJoin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new GameWindow();
            }
        });

        //Button Host - sends you to gameplay screen
        buttonHost.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new HostWindow();
            }
        });

        //Button Options - sends you to options screen
        buttonOptions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new OptionsWindow();
            }
        });

    }

}
