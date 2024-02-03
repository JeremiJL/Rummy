import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.intellijthemes.FlatSolarizedDarkIJTheme;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class OptionsWindow extends AbstractWindow{

    //Components
    private JPanel panelMain;
    private JLabel labelTitle;
    private JTextField textFieldSetNick;
    private JLabel labelNickInfo;
    private JLabel labelWindowSizeInfo;
    private JComboBox<String> comboBoxSetWindowSize;
    private JComboBox<String> comboBoxChooseTheme;
    private JLabel labelThemeInfo;
    private JButton buttonMenu;
    private JButton buttonDeafaults;
    private JButton buttonApply;

    //Themes Map
    private static final Map<String, Class<? extends LookAndFeel>>  themesMap = new HashMap<>();
    //Supply Map
    static {
        themesMap.put(FlatSolarizedDarkIJTheme.NAME, FlatSolarizedDarkIJTheme.class);
        themesMap.put(FlatDarkLaf.NAME, FlatDarkLaf.class);
        themesMap.put(FlatLightLaf.NAME, FlatLightLaf.class);
    }

    //Window Dimensions Map
    private static final int[][] possibleDimensions = new int[][]{{1600,1000},{1400,1000},{1200,800},{1000,600}};

    public OptionsWindow() {
        //Invoking abstract window init
        super();
        //Setting contentPanel
        this.setContentPane(panelMain);
        //Creating action listeners
        initListeners();
    }

    private void createUIComponents() {

        //Choose theme combo box
        comboBoxChooseTheme = new JComboBox<>();
        //Feed combobox with names of themes
        themesMap.keySet().forEach(s -> comboBoxChooseTheme.addItem(s));
        //Display chosen value
        comboBoxChooseTheme.setSelectedItem(UIManager.getLookAndFeel().getName());

        //Set window size / dimension combo box
        comboBoxSetWindowSize = new JComboBox<>();
        //Feed combobox with possible window size's represented as string
        Arrays.stream(possibleDimensions).forEach(arr -> comboBoxSetWindowSize.addItem(arr[0] + " x " + arr[1]));
        //Display chosen values
        comboBoxSetWindowSize.setSelectedItem(AbstractWindow.windowWidth + " x " + AbstractWindow.windowHeight);
    }

    @Override
    protected void initListeners() {

        //Back to menu - simply go back to menu screen
        buttonMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //Dispose window and open Menu window
                dispose();
                new MenuWindow();

            }
        });

        //Apply all options changes
        buttonApply.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                //Apply Window Size changes

                //Extract int values from string representation from combo box
                String chosenValue = comboBoxSetWindowSize.getSelectedItem().toString();
                int[] newDimensions = Arrays.stream(chosenValue.split(" x ")).mapToInt(s -> Integer.parseInt(s)).toArray();

                //Set new window width and height as AbstractWindow fields
                AbstractWindow.windowWidth = newDimensions[0];
                AbstractWindow.windowHeight = newDimensions[1];

                //Apply Themes changes
                Class<? extends LookAndFeel> laf = themesMap.get(comboBoxChooseTheme.getSelectedItem());

                //Set selected look and feel
                try {
                    UIManager.setLookAndFeel(laf.getConstructor().newInstance());
                } catch (UnsupportedLookAndFeelException | NoSuchMethodException | InvocationTargetException |
                         InstantiationException | IllegalAccessException exception) {
                    throw new RuntimeException(exception);
                }

                //Finally: reopen window
                dispose();
                new OptionsWindow();
            }
        });

    }
}
