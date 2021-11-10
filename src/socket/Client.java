package socket;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.Socket;

public class Client extends JFrame implements KeyListener {

    private Socket con;
    private OutputStream oS;
    private Writer oSW;
    private BufferedWriter bW;
    private static String name;


    public Client(String name) {

    }


    public static void main(String[] args) {
        Client client = new Client(args[0]);
        client.connectToServer();
        client.listenMessages();
    }

    private void connectToServer() {
        try {
            con = new Socket("localhost", 12345);
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
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
