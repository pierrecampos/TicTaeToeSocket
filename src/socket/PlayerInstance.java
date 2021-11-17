package socket;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class PlayerInstance extends Thread {
    private static List<BufferedWriter> clientsWriter;

    private final Socket con;
    private InputStream iS;
    private InputStreamReader iSR;
    private BufferedReader bR;
    private String name;

    public PlayerInstance(Socket con, List<BufferedWriter> clientsWriter) {
        this.con = con;
        PlayerInstance.clientsWriter = clientsWriter;

        try {
            iS = con.getInputStream();
            iSR = new InputStreamReader(iS);
            bR = new BufferedReader(iSR);
        } catch (IOException e) {
            e.printStackTrace();
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
                bW.write(msg + "\r\n");
                bW.flush();
            }
        }
    }

}
