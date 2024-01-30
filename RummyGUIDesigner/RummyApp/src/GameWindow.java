import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.intellijthemes.FlatSolarizedDarkIJTheme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameWindow extends AbstractWindow{

    //Logic TMP interface between visualisation and data
    private Integer[][] logicPlayingBoard;
    private Integer[][] logicHand;
    private Object[][] logicPlayersList;

    //Components
    private JPanel panelMain;
    private JTable tableBoard;
    private JTable tableHand;
    private JTable tablePlayers;
    private JButton buttonRevert;
    private JButton buttonApply;
    private JButton buttonDraw;
    private JButton buttonNone1;
    private JButton buttonNone2;
    private JScrollPane scrollPanel;
    private JLabel labelTitle;
    private JLabel labelInfo;
    private JLabel labelClock;
    private JLabel labelTilesCounter;

    //Table models
    private DefaultTableModel modelBoard;
    private DefaultTableModel modelHand;
    private DefaultTableModel modelPlayers;

    public GameWindow() {
        //Invoking abstract window init
        super();
        //Setting contentPanel
        this.setContentPane(panelMain);
        //Set action listener
        this.initListeners();
    }



    private void createUIComponents() {

        //Tables:

        //Listed players Customization
        tablePlayers = new JTable(){
            @Override
            public boolean isRowSelected(int row) {
                return false;
            }

            @Override
            public boolean isColumnSelected(int column) {
                return false;
            }

//            @Override
//            public boolean isCellSelected(int row, int column) {
//                return false;
//            }
        };
        tablePlayers.setShowHorizontalLines(true);
        tablePlayers.setShowVerticalLines(true);

        //Specify table model is such a way that first column is interpreted as ImageIcons second as Strings and third as Integers
        //Which corresponds to the fact that first column contains icons representing players second their nicks and third the number of tiles they have left
        modelPlayers = new DefaultTableModel(){
            @Override
            public Class<?> getColumnClass(int columnIndex) {

                if (columnIndex == 0)
                    return ImageIcon.class;
                else if (columnIndex == 1)
                    return String.class;
                else
                    return Integer.class;
            }

            //Disable editing cells by user
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }


        };

        //Set up columns
        modelPlayers.setColumnIdentifiers(new String[]{"Icon","Nick","Counter"});
        //Finally set table model to the table
        tablePlayers.setModel(modelPlayers);

        //Set size of rows
        tablePlayers.setRowHeight(40);
        //Change width of column containing nicks
        tablePlayers.getColumnModel().getColumn(1).setPreferredWidth(120);
        tablePlayers.getColumnModel().getColumn(1).setResizable(true);

        //Playing board
        tableBoard = new JTable(){
            @Override
            public boolean isRowSelected(int row) {
                return false;
            }

            @Override
            public boolean isColumnSelected(int column) {
                return false;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            @Override
            public boolean getColumnSelectionAllowed() {
                return false;
            }
            @Override
            public boolean getRowSelectionAllowed() {
                return false;
            }

        };
        tableBoard.setShowVerticalLines(true);
        tableBoard.setShowHorizontalLines(true);
        tableBoard.setTableHeader(null);

        //Specify table model
        modelBoard = new DefaultTableModel(){

            //TO DO: this method has to be handled in a way that we want to handle placing tiles on playing board
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        //Set up columns
        modelBoard.setColumnIdentifiers(new String[]{"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19"});
        //Finally set table model to the table
        tableBoard.setModel(modelBoard);

        //Set size of rows
        tableBoard.setRowHeight(50);

        //Hand of player
        tableHand = new JTable(){
            @Override
            public boolean isRowSelected(int row) {
                return false;
            }

            @Override
            public boolean isColumnSelected(int column) {
                return false;
            }
        };
        tableHand.setShowHorizontalLines(true);
        tableHand.setShowVerticalLines(true);

        //Specify table model
        modelHand = new DefaultTableModel(){

            //TO DO: this method has to be handled in a way that we want to handle placing tiles on playing board
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        //Set up columns
        modelHand.setColumnIdentifiers(new String[]{"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19"});
        //Finally set table model to the table
        tableHand.setModel(modelHand);

        //Set size of rows
        tableHand.setRowHeight(50);

        //Set color
        tableHand.setBackground(new Color(67,107,123));

        //Initialize Exemplary Arrays
        initializeLogicArraysWithExemplaryData();

        //Supply tables with data
        supplyPlayersWithData();
        supplyBoardWithData();
        supplyHandWithData();
    }

    private void supplyPlayersWithData(){
        //Insert consecutive rows into table model from logical array
        for (Object[] row : logicPlayersList){
            modelPlayers.addRow(row);
        }
    }

    private void supplyBoardWithData(){
        //Insert rows from logic array into table model
        for (Integer[] row : logicPlayingBoard){
            modelBoard.addRow(row);
        }
    }

    private void supplyHandWithData(){
        //Insert rows from logic hand array into table model
        for (Integer[] row : logicHand){
            modelHand.addRow(row);
        }
    }

    private void initializeLogicArraysWithExemplaryData(){

        //Logic Playing Board Data
        logicPlayingBoard = new Integer[20][20];

        for (int i = 0; i < 20; i++){
            for (int j = 0; j < 20; j++){
                logicPlayingBoard[i][j] = i*10 + j;
            }
        }

        //Logic Player's Hand Data
        logicHand = new Integer[2][20];

        for (int i = 0; i < 20; i++){
            logicHand[0][i] = (int)( Math.random()*13);
            logicHand[1][i] = (int)( Math.random()*13);
        }

        //Players List Data
        logicPlayersList = new Object[4][3];

        ImageIcon userImage = new ImageIcon(new ImageIcon("C:\\Users\\braghgi\\Documents\\Informatyka\\java\\Rummy\\RummyGUIDesigner\\RummyApp\\graphics\\user.png").getImage().getScaledInstance(30,30, Image.SCALE_SMOOTH));

        logicPlayersList[0][0] = userImage;
        logicPlayersList[1][0] = userImage;
        logicPlayersList[2][0] = userImage;
        logicPlayersList[3][0] = userImage;

        logicPlayersList[0][1] = "Jagi";
        logicPlayersList[1][1] = "Gromcio";
        logicPlayersList[2][1] = "Hubert";
        logicPlayersList[3][1] = "Jeremi";

        logicPlayersList[0][2] = 3;
        logicPlayersList[1][2] = 7;
        logicPlayersList[2][2] = 1;
        logicPlayersList[3][2] = 2;
    }

    @Override
    protected void initListeners() {

        tableBoard.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                switch (keyCode) {
                    case KeyEvent.VK_UP:
                        scrollVertically(scrollPanel, -1);
                        System.out.println("up");
                        break;
                    case KeyEvent.VK_DOWN:
                        scrollVertically(scrollPanel, 1);
                        System.out.println("down");
                        break;
                    case KeyEvent.VK_LEFT:
                        scrollHorizontally(scrollPanel, -1);
                        break;
                    case KeyEvent.VK_RIGHT:
                        scrollHorizontally(scrollPanel, 1);
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

    }

    private static void scrollVertically(JScrollPane scrollPanel, int direction) {
        JScrollBar verticalScrollBar = scrollPanel.getVerticalScrollBar();
        verticalScrollBar.setValue(verticalScrollBar.getValue() + direction * verticalScrollBar.getBlockIncrement());
    }

    private static void scrollHorizontally(JScrollPane scrollPanel, int direction) {
        JScrollBar horizontalScrollBar = scrollPanel.getHorizontalScrollBar();
        horizontalScrollBar.setValue(horizontalScrollBar.getValue() + direction * horizontalScrollBar.getBlockIncrement());
    }

}
