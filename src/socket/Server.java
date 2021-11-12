package socket;

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

    public Server(int port) {
        this.port = port;
    }


    @Override
    public void run() {
        try {
            server = new ServerSocket(port);
            clientsWriter = new ArrayList<BufferedWriter>();

            while (true) {
                System.out.println("Aguardando conexão...");
                Socket con = server.accept();
                System.out.println("Ip conectado: " + con.getInetAddress().getHostAddress());
                Thread playerInstance = new PlayerInstance(con, clientsWriter);
                playerInstance.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
