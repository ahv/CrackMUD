package fi.kivikasvo.crackmud;

import fi.kivikasvo.crackmud.core.PlaySession;
import fi.kivikasvo.crackmud.context.EnteredCommand;
import fi.kivikasvo.crackmud.network.Client;
import fi.kivikasvo.crackmud.network.ConnectionManager;
import fi.kivikasvo.crackmud.network.ClientEventListener;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.HashMap;

public class Main implements ClientEventListener {

    public static final String PATH = Paths.get("").toAbsolutePath().toString() + "/";

    public static void main(String[] args) throws URISyntaxException {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 55055;
        Main m = new Main(port);
        m.start();
    }

    private final int serverPort;
    private final HashMap<Client, PlaySession> clientSessions;
    private final ConnectionManager connectionManager;

    public Main(int port) {
        serverPort = port;
        clientSessions = new HashMap<>();
        connectionManager = new ConnectionManager(port, this);
    }

    void start() {
        System.out.println("Server started on port: " + serverPort);
        connectionManager.start();
    }

    @Override
    public void onNewClientCreated(Client client) {
        PlaySession session = new PlaySession(client);
        clientSessions.put(client, session);
    }

    @Override
    public void onClientMessageRead(Client client, String message) {
        System.out.println(client + " sent: " + message);
        clientSessions.get(client).getContext().handleCommand(new EnteredCommand(message));
    }

    @Override
    public void onClientReadStreamBroke(Client client) {
        System.out.println("Client read stream broke for " + client);
        clientSessions.remove(client);
        connectionManager.destroy(client);
    }

    @Override
    public void onClientWriteStreamBroke(Client client) {
        System.out.println("Client read stream broke for " + client);
        clientSessions.remove(client);
        connectionManager.destroy(client);
    }
}
