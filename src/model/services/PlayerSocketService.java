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

    public void closeService() throws IOException {
        if (socket != null) {
            socket.close();
        }
        if (server != null) {
            server.closeServer();
        }

    }


}
