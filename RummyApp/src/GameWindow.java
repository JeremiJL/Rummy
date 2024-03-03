
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;


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

    //Interaction
    private LinkedList<Integer> tilesHeld = new LinkedList<>();
    private boolean selectMultiple = false;

    //Cursors
    private static final Image grabCursorImage = Toolkit.getDefaultToolkit().getImage("RummyApp/graphics/icon_grab_white.png");
    private static final Cursor cursorGrab = Toolkit.getDefaultToolkit().createCustomCursor(grabCursorImage, new Point(3, 0), "Grab Cursor");

    private static final Image cardsCursorImage = Toolkit.getDefaultToolkit().getImage("RummyApp/graphics/icon_card_white.png");
    private static final Cursor cursorCards = Toolkit.getDefaultToolkit().createCustomCursor(cardsCursorImage, new Point(3, 0), "Cards Cursor");


    public GameWindow() {
        //Invoking abstract window init
        super();
        //Setting contentPanel
        this.setContentPane(panelMain);
        //Set action listener
        this.initListeners();
        //Set key binding
        setKeyBindings();
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

            @Override
            public boolean isCellSelected(int row, int column) {
                return false;
            }
        };
        tablePlayers.setShowHorizontalLines(true);
        tablePlayers.setShowVerticalLines(true);

        //Set colorfull border for table
        Border lineBorder = BorderFactory.createLineBorder(backgroundBlue, 2);
        tablePlayers.setBorder(lineBorder);

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
            public boolean getRowSelectionAllowed() {
                return true;
            }

            @Override
            public boolean getColumnSelectionAllowed() {
                return true;
            }

            @Override
            public boolean isFocusable() {
                return true;
            }
        };

        tableBoard.setShowVerticalLines(true);
        tableBoard.setShowHorizontalLines(true);
        tableBoard.setTableHeader(null);
        tableBoard.setFocusable(true);

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

        //Set height of rows
        tableBoard.setRowHeight(50);

        //Set width of columns - not working!
        TableColumnModel columnModel = tableBoard.getColumnModel();
        for (int i = 0; i < columnModel.getColumnCount(); i++){
            columnModel.getColumn(i).setPreferredWidth(50);
        }

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
        tableHand.setBackground(backgroundBlue);

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
            logicPlayingBoard[i][0] = i*10;
            for (int j = 0; j < 20; j++){
                //logicPlayingBoard[i][j] = i*10 + j;
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

        ImageIcon userImage = new ImageIcon(new ImageIcon("RummyApp/graphics/user.png").getImage().getScaledInstance(25,25, Image.SCALE_SMOOTH));

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


    private void setKeyBindings(){

        //Key Bindings for main panel
        InputMap mainInputs = panelMain.getInputMap();
        ActionMap mainActions = panelMain.getActionMap();

        //Turning on and off multiple selection mode
        mainInputs.put(KeyStroke.getKeyStroke("SPACE"),"pressed space");
        mainInputs.put(KeyStroke.getKeyStroke("released SPACE"),"released space");

        mainActions.put("pressed space", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectMultiple = true;
            }
        });

        mainActions.put("released space", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectMultiple = false;
            }
        });

        //Moving the board using arrow keys
        mainInputs.put(KeyStroke.getKeyStroke("UP"),"up");
        mainInputs.put(KeyStroke.getKeyStroke("DOWN"),"down");
        mainInputs.put(KeyStroke.getKeyStroke("RIGHT"),"right");
        mainInputs.put(KeyStroke.getKeyStroke("LEFT"),"left");

        mainActions.put("up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scrollVertically(scrollPanel, -1);
            }
        });
        mainActions.put("down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scrollVertically(scrollPanel, 1);
            }
        });
        mainActions.put("right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scrollHorizontally(scrollPanel, 1);
            }
        });
        mainActions.put("left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scrollHorizontally(scrollPanel, -1);
            }
        });

    }

    @Override
    protected void initListeners() {

        tableBoard.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {

                //Extract selected cell index
                int sRow = tableBoard.getSelectedRow();
                int sCol = tableBoard.getSelectedColumn();

                System.out.println("selected row : " + sRow + " selected col : " + sCol);

                //If some cards are held in hand, then lay them out on board
                if (!tilesHeld.isEmpty()){

                    int startingColumn = sCol;
                    while (!tilesHeld.isEmpty()){

                        placeTile(sRow,sCol);

                        //Lay them out in consecutive columns, if border of board is reached, then move to next row
                        if (sCol < tableBoard.getColumnCount() -1){
                            sCol++;
                        } else{
                            sRow++;
                            sCol = startingColumn;
                        }
                    }

                }
                //If no card is held and selectMultiple option is diabled, then grab tile
                else if (! selectMultiple) {
                    grabTile(sRow,sCol);
                }

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

                //If no cards are held in hand and selectMultiple option is enabled grab all cards from selected tiles
                if (tilesHeld.isEmpty() && selectMultiple){

                    //grab selected tiles
                    for (int sRow : tableBoard.getSelectedRows()){
                        for (int sCol : tableBoard.getSelectedColumns()){
                            grabTile(sRow,sCol);
                        }
                    }
                }

            }

            @Override
            public void mouseEntered(MouseEvent e) {

                if (tilesHeld.isEmpty())
                    tableBoard.setCursor(cursorGrab);
                else
                    tableBoard.setCursor(cursorCards);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                tableBoard.setCursor(Cursor.getDefaultCursor());
            }
        });


    }

    private boolean grabTile(int row, int col){
        if (tableBoard.getValueAt(row,col) != null){
            tilesHeld.add((int) tableBoard.getValueAt(row,col));
            tableBoard.setValueAt(null,row,col);
            tableBoard.setCursor(cursorCards);
            return true;
        }
        return false;
    }

    private boolean placeTile(int row, int col){

        //If chosen place on board is free and If some tiles are held, place first one
        if (!tilesHeld.isEmpty() && tableBoard.getValueAt(row,col) == null){
            tableBoard.setValueAt(tilesHeld.pop(),row,col);

            //If no tile is held, change cursor image
            if (tilesHeld.isEmpty()){
                //Set cursor indicating that hand is free
                tableBoard.setCursor(cursorGrab);
            }
            return true;
        }
        return false;
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
