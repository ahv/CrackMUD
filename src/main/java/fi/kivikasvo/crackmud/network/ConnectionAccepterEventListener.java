package fi.kivikasvo.crackmud.network;

import java.io.IOException;
import java.net.Socket;

public interface ConnectionAccepterEventListener {
    public void OnConnectionAccepterStopped();
    public void OnServerSocketFailedToInitialize(Exception ex);
    public void OnFailedToAcceptConnection(IOException ex);
    public void OnNewConnection(Socket s);
}
