package fi.kivikasvo.crackmud.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionAccepter {

    private final int port;
    private final ConnectionAccepterEventListener listener;
    private Thread accepterThread;
    private ServerSocket serverSocket;
    private boolean active;

    public ConnectionAccepter(int port, ConnectionAccepterEventListener listener) {
        this.port = port;
        this.listener = listener;
    }

    public void startAccepting() {
        active = true;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException ex) {
            listener.OnServerSocketFailedToInitialize(ex);
        }
        accepterThread = new Thread(() -> {
            while (active) {
                try {
                    Socket s = serverSocket.accept();
                    s.setKeepAlive(true);
                    listener.OnNewConnection(s);
                } catch (IOException ex) {
                    listener.OnFailedToAcceptConnection(ex);
                }
            }
            listener.OnConnectionAccepterStopped();
        });
        accepterThread.start();
    }

    public void stopAccepting() {
        try {
            serverSocket.close();
            active = false;
        } catch (IOException ex) {

        }
    }
}
