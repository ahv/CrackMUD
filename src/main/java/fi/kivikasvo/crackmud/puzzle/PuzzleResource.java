package fi.kivikasvo.crackmud.puzzle;

import fi.kivikasvo.crackmud.Main;
import fi.kivikasvo.crackmud.db.Task;
import fi.kivikasvo.crackmud.exception.puzzleNotInLibraryException;
import fi.kivikasvo.crackmud.db.UserAccount;
import java.io.File;
import java.util.HashMap;
import org.apache.commons.io.FilenameUtils;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.jse.JsePlatform;

public class PuzzleResource {

    // TODO: Don't expose, but provide methods to query instead
    public static HashMap<String, PuzzleScript> library = buildLibrary();

    private static HashMap<String, PuzzleScript> buildLibrary() {
        System.out.println("Building script library...");
        System.out.println("Path to scripts folder: " + Main.PATH + "lua/");
        HashMap<String, PuzzleScript> scriptLibrary = new HashMap<>();
        File[] fileList = new File(Main.PATH + "lua/").listFiles();
        System.out.println("Found " + fileList.length + " scripts.");
        for (File f : fileList) {
            String scriptName = FilenameUtils.removeExtension(f.getName());
            PuzzleScript scriptItem = new PuzzleScript(scriptName, loadScriptFile(scriptName));
            System.out.println("Processing script: " + scriptName);
            scriptLibrary.put(scriptName, scriptItem);
        }
        return scriptLibrary;
    }

    public static void rebuildLibrary() {
        library = buildLibrary();
    }

    // TODO: Trys and catches, validation
    private static LuaValue loadScriptFile(String name) {
        LuaValue globals = JsePlatform.standardGlobals();
        globals.get("dofile").call(LuaValue.valueOf(Main.PATH + "lua/" + name + ".lua"));
        return globals;
    }

    public static Task createTaskForUser(String taskId, UserAccount user) throws puzzleNotInLibraryException {
        if (!library.containsKey(taskId)) {
            throw new puzzleNotInLibraryException(taskId);
        }
        Task task = new Task(user, library.get(taskId));
        return task;
    }

    // TODO: don't expose, implement real tests
    public static void test(String scriptId, String hash1, String hash2) {
        System.out.println("Script ID: " + scriptId);
        LuaValue script = loadScriptFile(scriptId);
        System.out.println("Desc:        " + script.get("desc"));
        System.out.println("Values:      " + hash1 + ", " + hash2);
        LuaValue re = script.get("solve").call(LuaValue.valueOf(hash1), LuaValue.valueOf(hash2));
        System.out.println("solution:    " + re.tojstring(1).toUpperCase());
        System.out.println("");
    }

}
