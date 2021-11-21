package socket;

import model.listeners.GameReadyListener;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread {
    private static List<BufferedWriter> clientsWriter;
    private static ServerSocket server;
    private final int port;
    private final List<GameReadyListener> gameReadyListener;
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
            clientsWriter = new ArrayList<BufferedWriter>();

            //Aguardando conexão dos Players
            while (continueServer) {
                System.out.println("Aguardando conexão...");
                Socket con = server.accept();
                System.out.println("Ip conectado: " + con.getInetAddress().getHostAddress());
                Thread playerInstance = new PlayerInstance(con, clientsWriter);
                playerInstance.start();

                if (clientsWriter.size() > 0 && !server.isClosed()) {
                    continueServer = false;
                    notifyGameReadyListeners();
                }
            }

            while (!server.isClosed()) {

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

    public void closeServer() {
        try {
            if (server != null) {
                server.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
