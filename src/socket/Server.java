package socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread {
    private static List<BufferedWriter> clientsWriter;
    private static ServerSocket server;
    private final Socket con;
    private InputStream iS;
    private InputStreamReader iSR;
    private BufferedReader bR;
    private String name;

    public Server(Socket con) {
        this.con = con;

        try {
            iS = con.getInputStream();
            iSR = new InputStreamReader(iS);
            bR = new BufferedReader(iSR);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        server = new ServerSocket(12345);
        clientsWriter = new ArrayList<BufferedWriter>();

        while (true) {
            System.out.println("Aguardando conexão...");
            Socket con = server.accept();
            System.out.println("Ip conectado: " + con.getInetAddress().getHostAddress());
            Thread serverClient = new Server(con);
            serverClient.start();
        }

    }

    @Override
    public void run() {
        try {
            OutputStream ouS = this.con.getOutputStream();
            Writer ouW = new OutputStreamWriter(ouS);
            BufferedWriter bW = new BufferedWriter(ouW);
            clientsWriter.add(bW);
            String msg;
            msg = name = bR.readLine();
            while (msg != null) {
                msg = bR.readLine();
                sendMessages(bW, msg);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessages(BufferedWriter bWS, String msg) throws IOException {
        for (BufferedWriter bW : clientsWriter) {
            if (bW != bWS) {
                bW.write(name + ": " + msg + "\r\n");
                bW.flush();
                System.out.println(msg);
            }
        }

    }


}
