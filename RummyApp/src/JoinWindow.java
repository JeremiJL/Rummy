import javax.swing.*;

public class JoinWindow extends AbstractWindow{

    //Components
    private JPanel panelMain;
    private JLabel labelTitle;
    private JLabel labelInfo;
    private JTextField textFieldIpPort;
    private JButton buttonPaste;
    private JLabel labelTimeCounter;
    private JButton buttonJoin;
    private JTable tableLobby;

    public JoinWindow() {
        //Invoking abstract window init
        super();
        //Setting contentPanel
        this.setContentPane(panelMain);
        //Set action listener
        this.initListeners();
    }

    @Override
    protected void initListeners() {

    }
}
