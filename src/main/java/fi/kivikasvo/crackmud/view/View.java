
package fi.kivikasvo.crackmud.view;

import fi.kivikasvo.crackmud.network.Client;

public class View {
    private Client client;

    public View(Client client) {
        this.client = client;
    }
    
    void send(String message){
        client.send(message);
    }

}
