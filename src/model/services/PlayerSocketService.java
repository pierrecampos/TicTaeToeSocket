package model.services;

import socket.Server;

import java.io.IOException;
import java.net.Socket;

public class PlayerSocketService {
    private Socket socket;
    private Server server;

    public PlayerSocketService() {
    }

    public Server startServer(int port) {
        server = new Server(port);
        return server;
    }

    public boolean startSocket(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        return socket != null && socket.isConnected();
    }

    public Server getServer() {
        return server;
    }

    public Socket getSocket() {
        return server != null ? server.getSocket() : socket;
    }

    public void closeServer() {
        if (server != null) {
            server.closeServer();
        }
    }


}
