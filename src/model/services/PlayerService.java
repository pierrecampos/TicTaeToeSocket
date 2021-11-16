package model.services;

import java.io.IOException;
import java.net.Socket;

public class PlayerService {
    private Socket con;


    public boolean connect(String ip, int port) {
        try {
            con = new Socket(ip, port);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return con.isConnected();
    }

    public void closeSocket() {
        if (con != null && con.isConnected()) {
            try {
                con.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Socket getCon() {
        return con;
    }

    public boolean isConnected() {
        return con.isConnected();
    }


}
