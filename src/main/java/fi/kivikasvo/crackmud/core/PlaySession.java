package fi.kivikasvo.crackmud.core;

import fi.kivikasvo.crackmud.db.UserAccount;
import fi.kivikasvo.crackmud.context.Context;
import fi.kivikasvo.crackmud.context.LoginContext;
import fi.kivikasvo.crackmud.network.Client;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlaySession {

    public final Client client;
    private UserAccount user;
    private Context context;

    public PlaySession(Client client) {
        this.client = client;
        changeContext(LoginContext.class);
    }

    public UserAccount getUserAccount() {
        // TODO: Load from database
        return user;
    }
    
    // TODO: Make sure that this can be only set once
    public void setUserAccount(UserAccount user) {
        this.user = user;
    }

    public Context getContext() {
        return context;
    }

    public void changeContext(Class<? extends Context> newContextClass) {
        try {
            Constructor<?> constructor = newContextClass.getConstructor(PlaySession.class);
            Context newContext = (Context) constructor.newInstance(this);
            if (context != null) context.onExitContext();
            context = newContext;
            context.onEnterContext();
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            System.out.println("Context change failed!");
            Logger.getLogger(PlaySession.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
