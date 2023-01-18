package fi.kivikasvo.crackmud.puzzle;

import org.luaj.vm2.LuaValue;

public class PuzzleScript {

    private final String id;
    private final String description;
    private final LuaValue solver;
    private int cost;

    public PuzzleScript(String id, LuaValue script) {
        this.id = id;
        description = script.get("desc").toString();
        solver = script.get("solve");
        cost = 0;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public int getCost() {
        return cost;
    }

    public String solve(String hash1, String hash2) {
        LuaValue re = solver.call(LuaValue.valueOf(hash1), LuaValue.valueOf(hash2));
        return re.toString().toUpperCase();
    }

}
