package socket;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.Socket;

public class Client_Tests extends JFrame implements KeyListener {

    private static final long serialVersionUID = 1L;
    private static String name;
    private Socket con;
    private OutputStream oS;
    private Writer oSW;
    private BufferedWriter bW;
    private JPanel pane;
    private JTextField textField;

    public Client_Tests(String name) {
        pane = new JPanel();
        textField = new JTextField(10);
        textField.addKeyListener(this);
        pane.add(textField);

        setContentPane(pane);
        setLocationRelativeTo(null);
        setResizable(false);
        setSize(250,300);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }


    public static void main(String[] args) {
        Client_Tests client = new Client_Tests(args[0]);
        client.connectToServer();
        client.listenMessages();
    }

    private void connectToServer() {
        try {
            con = new Socket("localhost", 12344);
            oS = con.getOutputStream();
            oSW = new OutputStreamWriter(oS);
            bW = new BufferedWriter(oSW);
            bW.write(name + "\r\n");
            bW.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listenMessages() {
        try {
            InputStream iS = con.getInputStream();
            InputStreamReader iSR = new InputStreamReader(iS);
            BufferedReader bR = new BufferedReader(iSR);
            String msg;
            while (true) {
                if (bR.ready()) {
                    msg = bR.readLine();
                    System.out.println("OUVINDO " + msg);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String message) {
        try {
            bW.write(message + "\r\n");
            bW.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            sendMessage("OK");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
