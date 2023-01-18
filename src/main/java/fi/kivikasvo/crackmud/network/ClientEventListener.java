package fi.kivikasvo.crackmud.network;

public interface ClientEventListener {

    public void onNewClientCreated(Client c);

    public void onClientMessageRead(Client c, String message);

    public void onClientReadStreamBroke(Client c);

    public void onClientWriteStreamBroke(Client c);
}
