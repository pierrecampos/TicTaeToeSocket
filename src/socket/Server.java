package socket;

import model.listeners.GameReadyListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread {
    private static ServerSocket server;
    private final int port;
    private final List<GameReadyListener> gameReadyListener;
    private Socket socket;
    private boolean continueServer;

    public Server(int port) {
        continueServer = true;
        this.port = port;
        gameReadyListener = new ArrayList<>();
    }

    @Override
    public void run() {
        try {
            server = new ServerSocket(port);
            System.out.println("Aguardando Conexão");
            socket = server.accept();
            System.out.println("Cliente conectado " + socket.getInetAddress().getHostAddress());
            notifyGameReadyListeners();

            while (continueServer) {
            }
            if (socket != null && socket.isConnected()) {
                socket.close();
            }

        } catch (SocketException ignored) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void subscribeGameReadyListener(GameReadyListener listener) {
        gameReadyListener.add(listener);
    }

    //Notifica o host quando há um adversário
    public void notifyGameReadyListeners() {
        for (GameReadyListener listener : gameReadyListener) {
            listener.onGameReady();
        }
    }

    public Socket getSocket(){
        return socket;
    }

    public void closeServer() {
        if (server != null) {
            continueServer = false;
            try {
                server.close();
                System.out.println("Server Fechado");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
