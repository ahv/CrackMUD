package fi.kivikasvo.crackmud.network;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionManager implements ConnectionAccepterEventListener {

    private ConnectionAccepter connectionAccepter;
    private ArrayList<Client> clients; // TODO: Needs thread safety
    private final ClientEventListener clientEventListener;

    public ConnectionManager(int port, ClientEventListener clientEventListener) {
        clients = new ArrayList<>();
        connectionAccepter = new ConnectionAccepter(port, this);
        this.clientEventListener = clientEventListener;
    }

    public void start() {
        connectionAccepter.startAccepting();
    }

    @Override
    public void OnNewConnection(Socket socket) {
        System.out.println("New connection: " + socket);
        try {
            Client c = new Client(socket, clientEventListener);
            clients.add(c);
            clientEventListener.onNewClientCreated(c);
            c.startReading();
        } catch (IOException ex) {
            System.out.println("IO Exception when creating Client");
            Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void OnServerSocketFailedToInitialize(Exception ex) {
        System.out.println("Server socket failed to initialize");
    }

    @Override
    public void OnConnectionAccepterStopped() {
        System.out.println("Connection accepter stopped");
    }

    @Override
    public void OnFailedToAcceptConnection(IOException ex) {
        System.out.println("Failed to accept connection");
    }

    public void destroy(Client client) {
        client.destroy();
        clients.remove(client);
    }

}
