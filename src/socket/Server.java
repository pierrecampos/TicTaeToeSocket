package socket;

import model.listeners.GameReadyListener;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread {
    private static List<BufferedWriter> clientsWriter;
    private static ServerSocket server;
    private final int port;
    private final List<GameReadyListener> gameReadyListener;
    private final boolean alive;

    public Server(int port) {
        alive = true;
        this.port = port;
        gameReadyListener = new ArrayList<>();
    }


    @Override
    public void run() {
        try {
            server = new ServerSocket(port);
            clientsWriter = new ArrayList<BufferedWriter>();

            //Aguarda a coneção
            while (alive) {
                System.out.println("Aguardando conexão...");
                Socket con = server.accept();
                System.out.println("Ip conectado: " + con.getInetAddress().getHostAddress());
                Thread playerInstance = new PlayerInstance(con, clientsWriter);
                playerInstance.start();

                if (clientsWriter.size() > 0) {
                    notifyGameReadyListeners();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void subscribeGameReadyListener(GameReadyListener listener) {
        gameReadyListener.add(listener);
    }

    //Fica analisando se foi feita a coneção
    public void notifyGameReadyListeners() {
        for (GameReadyListener listener : gameReadyListener) {
            listener.onGameReady();
        }
    }
}
