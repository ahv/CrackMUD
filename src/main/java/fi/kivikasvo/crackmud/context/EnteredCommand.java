
package fi.kivikasvo.crackmud.context;

public class EnteredCommand {
    
    private String[] parts;
    
    public EnteredCommand(String message) {
        parts = message.split(" ");
    }

    public String getCommand() {
        return parts[0];
    }

    // TODO: -1 and 0 should be errors I guess
    public String getParam(int i) {
        return parts.length > i ? parts[i] : "";
    }

}
