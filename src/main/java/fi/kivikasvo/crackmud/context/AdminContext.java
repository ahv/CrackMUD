
package fi.kivikasvo.crackmud.context;

import fi.kivikasvo.crackmud.Command;
import fi.kivikasvo.crackmud.view.AdminView;
import fi.kivikasvo.crackmud.core.PlaySession;
import fi.kivikasvo.crackmud.puzzle.PuzzleResource;

public class AdminContext extends Context {

    public AdminContext(PlaySession session) {
        super(session, new AdminView(session.client));
    }

    @Command(desc="", usage="")
    private void reload() {
        session.client.send("Reloading script library.");
        PuzzleResource.rebuildLibrary();
    }

}
