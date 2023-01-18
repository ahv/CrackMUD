package fi.kivikasvo.crackmud.context;

import fi.kivikasvo.crackmud.Command;
import fi.kivikasvo.crackmud.HibernateUtil;
import fi.kivikasvo.crackmud.view.LoginView;
import fi.kivikasvo.crackmud.core.PlaySession;
import fi.kivikasvo.crackmud.core.TextBlock;
import fi.kivikasvo.crackmud.db.UserAccount;

public class LoginContext extends Context {

    public LoginContext(PlaySession session) {
        super(session, new LoginView(session.client));
    }

    @Override
    public void onEnterContext() {
        session.client.send(TextBlock.title);
        session.client.send("Use: create <username> <password>, or login <username> <password>");
    }

    @Override
    public void onExitContext() {
    }

    @Command(desc="Used to login into the game in the login context", usage="Login <username> <password>")
    public void login(EnteredCommand command) {
        UserAccount user = HibernateUtil.loadUser(command.getParam(1));
        if (user == null) {
            send("Invalid credentials.");
            return;
        }
        if (user.getPasshash().equals(command.getParam(2))) {
            // TODO: Check if there's a session with the account already - boot off old session
            send("Credentials ok.");
            session.setUserAccount(user);
            session.changeContext(GameContext.class);
            return;
        } else {
            send("Invalid credentials.");
            return;
        }
    }
    
    @Command(desc="Used to login into the game in the login context", usage="Login <username> <password>")
    public void create(EnteredCommand command) {
        UserAccount user = HibernateUtil.loadUser(command.getParam(1));
        if (user != null) {
            send("Username taken, sorry.");
        } else {
            user = new UserAccount(command.getParam(1), command.getParam(2));
            user.save();
            session.setUserAccount(user);
            send("User account created.");
            session.changeContext(GameContext.class);
        }
    }
}
