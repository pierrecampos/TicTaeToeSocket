package model.services;

import socket.Server;

import java.io.IOException;
import java.net.Socket;

public class PlayerSocketService {
    private Socket socket;
    private Server server;
    private String ip;
    private int port;

    public PlayerSocketService() {
    }

    public Server startServer(int port) {
        this.port = port;
        server = new Server(port);
        return server;
    }

    public boolean startSocket(String ip, int port) throws IOException {
        this.ip = ip;
        this.port = port;
        socket = new Socket(ip, port);
        return socket != null && socket.isConnected();
    }

    public Server getServer() {
        return server;
    }

    public Socket getSocket() {
        return server != null ? server.getSocket() : socket;
    }


    public void restartService() throws IOException {
        if(server != null){
            closeServer();
            Server server = startServer(port);
            new Thread(server).start();
        }else {
            socket.close();
//            startSocket(ip, port);
            System.out.println("CONNECTADO " + startSocket(ip, port));
        }
    }

    public void closeServer() {
        if (server != null) {
            server.closeServer();
        }
    }


}
