import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HostWindow extends AbstractWindow {


    //Logic TMP interface between visualisation and data
    private Object[][] logicPlayersList;

    //Components
    private JPanel panelMain;
    private JLabel labelTitle;
    private JTextField textFieldIpPort;
    private JButton buttonCopy;
    private JLabel labelInfo;
    private JTable tableLobby;
    private JButton buttonStart;
    private JLabel labelTimeCounter;
    private JSpinner spinnerRoundCounter;
    private JButton buttonCreateGame;
    private JComboBox comboBoxChooseJoker;
    private JButton buttonChooseCustomJoker;

    //Table model
    private DefaultTableModel modelLobby;

    public HostWindow() {
        //Invoking abstract window init
        super();
        //Setting contentPanel
        this.setContentPane(panelMain);
        //Set action listener
        this.initListeners();
    }


    private void createUIComponents() {

        //lobby table
        tableLobby = new JTable(){
            @Override
            public boolean isRowSelected(int row) {
                return false;
            }

            @Override
            public boolean isColumnSelected(int column) {
                return false;
            }

            @Override
            public boolean isCellSelected(int row, int column) {
                return false;
            }
        };

        //Set colorfull border for table
        Border lineBorder = BorderFactory.createLineBorder(backgroundBlue, 2);
        tableLobby.setBorder(lineBorder);

        //custom table model, first column displays icon, second player nick
        modelLobby = new DefaultTableModel(){
            @Override
            public Class<?> getColumnClass(int columnIndex) {

                if (columnIndex == 0)
                    return ImageIcon.class;
                else
                    return String.class;
            }

            //Disable editing cells by user
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };

        //Set up columns
        modelLobby.setColumnIdentifiers(new String[]{"Icon","Nick","Ip"});
        //Finally set table model to the table
        tableLobby.setModel(modelLobby);

        //Set size of rows
        tableLobby.setRowHeight(40);
        //Change width of columns
        tableLobby.getColumnModel().getColumn(0).setPreferredWidth(40);
        tableLobby.getColumnModel().getColumn(1).setPreferredWidth(120);
        tableLobby.getColumnModel().getColumn(2).setPreferredWidth(100);


        //Initialize Exemplary Arrays
        initializeLogicArraysWithExemplaryData();

        //Supply table with data
        supplyLobbyWithData();;

    }

    private void supplyLobbyWithData(){
        //Insert consecutive rows into table model from logical array
        for (Object[] row : logicPlayersList){
            modelLobby.addRow(row);
        }
    }

    private void initializeLogicArraysWithExemplaryData(){

        //Players List Data
        logicPlayersList = new Object[4][3];

        ImageIcon userImage = new ImageIcon(new ImageIcon("RummyApp/graphics/user.png").getImage().getScaledInstance(25,25, Image.SCALE_SMOOTH));

        logicPlayersList[0][0] = userImage;
        logicPlayersList[1][0] = userImage;
        logicPlayersList[2][0] = userImage;
        logicPlayersList[3][0] = userImage;

        logicPlayersList[0][1] = "Jagi";
        logicPlayersList[1][1] = "Gromcio";
        logicPlayersList[2][1] = "Hubert";
        logicPlayersList[3][1] = "Jeremi";

        logicPlayersList[0][2] = "127.0.0.1:701";
        logicPlayersList[1][2] = "127.0.0.2:701";
        logicPlayersList[2][2] = "127.0.0.3:701";
        logicPlayersList[3][2] = "127.0.0.4:701";
    }

    @Override
    protected void initListeners() {

        //Button Create/Start - creates new lobby or starts the game depending on state
        buttonStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new GameWindow();
            }
        });

        //Copies to clipboard value of ip + port text field
        buttonCopy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //Copy text field value to clipboard
                String textValue = textFieldIpPort.getText();
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(textValue), null);

                //Set button text to 'Copied'
                buttonCopy.setText("Copied");
            }
        });

        //Triggers when value of text field changes, changes the copy text
        textFieldIpPort.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                buttonCopy.setText("Copy");
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                buttonCopy.setText("Copy");
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                buttonCopy.setText("Copy");
            }
        });

    }

}
