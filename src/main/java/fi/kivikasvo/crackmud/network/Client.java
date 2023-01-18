package fi.kivikasvo.crackmud.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {

    private final BufferedReader reader;
    private final BufferedWriter writer;
    private final Socket socket;
    private Thread readThread;
    private boolean alive;
    private final ClientEventListener clientEventListener;

    Client(Socket socket, ClientEventListener clientEventListener) throws IOException {
        this.socket = socket;
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "ISO-8859-1"));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "ISO-8859-1"));
        this.clientEventListener = clientEventListener;
    }

    void startReading() {
        alive = true;
        readThread = new Thread(() -> {
            while (alive) {
                try {
                    String readLine = reader.readLine();
                    if (readLine == null) {
                        alive = false;
                        clientEventListener.onClientReadStreamBroke(this);
                        break;
                    }
                    clientEventListener.onClientMessageRead(this, readLine);
                } catch (IOException ex) {
                    System.out.println("IOException when trying to read a line from client " + this);
                    alive = false;
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        readThread.start();
    }

    public void send(String message) {
        try {
            writer.write(message+"\n\n");
            writer.flush();
        } catch (IOException ex) {
            clientEventListener.onClientWriteStreamBroke(this);
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void destroy() {
        try {
            alive = false;
            reader.close();
            writer.close();
            socket.close();
        } catch (IOException ex) {
            System.out.println("IOException when trying to destroy client " + this);
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String toString() {
        return socket.toString();
    }
}
