package fi.kivikasvo.crackmud.context;

import fi.kivikasvo.crackmud.core.PlaySession;
import fi.kivikasvo.crackmud.view.View;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Context {

    final PlaySession session;
    public final View view;

    public Context(PlaySession session, View view) {
        this.session = session;
        this.view = view;
    }
    
    void send(String message) {
        session.client.send(message);
    }

    public PlaySession getSession() {
        return session;
    }

    public void onEnterContext() {
        session.client.send("Entered " + this.getClass().getSimpleName());
    }

    public void onExitContext() {
        session.client.send("Left " + this.getClass().getSimpleName());
    }

    public void handleCommand(EnteredCommand command) {
        try {
            Method m = this.getClass().getDeclaredMethod(command.getCommand(), EnteredCommand.class);
            m.setAccessible(true);
            m.invoke(this, command);
        } catch (NoSuchMethodException ex) {
            session.client.send("Invalid command. Use \"commands\" for a list of available commands.");
        } catch (SecurityException | IllegalAccessException | InvocationTargetException ex) {
            Logger.getLogger(Context.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }

}
