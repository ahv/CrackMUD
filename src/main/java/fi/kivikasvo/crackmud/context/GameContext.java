package fi.kivikasvo.crackmud.context;

import fi.kivikasvo.crackmud.Command;
import fi.kivikasvo.crackmud.core.PlaySession;
import fi.kivikasvo.crackmud.controller.Controller;
import fi.kivikasvo.crackmud.view.GameView;
import java.lang.reflect.Method;

public class GameContext extends Context {

    public GameContext(PlaySession session) {
        super(session, new GameView(session.client));
    }

    @Override
    public void onEnterContext() {
        session.client.send("Welcome to CrackMUD");
    }

    @Command(desc = "list tasks assigned to you", usage = "list")
    private void list(EnteredCommand command) {
        Controller.listTasks(this, (GameView) view);
    }

    @Command(desc = "request an instance of a type of task", usage = "request <type>, use \"available\" to see the list of types")
    private void request(EnteredCommand command) {
        Controller.requestTask(this, (GameView) view, command.getParam(1));
    }

    @Command(desc = "submit an answer to a task", usage = "answer <UUID> <answer>, see \"list\" for UUIDs of your tasks, answer must be 8 symbols in range 0 to F")
    private void answer(EnteredCommand command) {
        Controller.answerTask(this, (GameView) view, command.getParam(1), command.getParam(2).toUpperCase());
    }

    @Command(desc = "list task types available to you", usage = "tasks")
    private void tasks(EnteredCommand command) {
        Controller.listAvailable(this, (GameView) view);
    }

    @Command(desc = "check the degree of correctness of your answer to a task", usage = "check <UUID>")
    private void check(EnteredCommand command) {
        Controller.checkTaskCorrectness(this, (GameView) view, command.getParam(1));
    }
    
    @Command(desc = "collect rewards for solved tasks", usage = "collect")
    private void collect(EnteredCommand command) {
        Controller.collectRewardsForTasks(this, (GameView) view);
    }

    //@Command(desc = "show stats", usage = "stats")
    private void stats(EnteredCommand command) {
        // TODO: Show stats
        ((GameView) view).onStats(session.getUserAccount());
    }

    // TODO: Move this to the base class
    @Command(desc = "show proper usage of a command", usage = "usage <command>")
    private void usage(EnteredCommand command) {
        try {
            Method m = this.getClass().getDeclaredMethod(command.getParam(1), EnteredCommand.class);
            session.client.send("Usage: " + m.getAnnotation(Command.class).usage());
        } catch (NoSuchMethodException | SecurityException ex) {
            session.client.send("Invalid command name.");
        }
    }

    // TODO: Move this to the base class
    @Command(desc = "list available commands", usage = "commands")
    private void commands(EnteredCommand command) {
        Method[] methods = this.getClass().getDeclaredMethods();
        String s = "Commands:";
        for (Method m : methods) {
            Command c = m.getAnnotation(Command.class);
            if (c == null) {
                continue;
            }
            s += "\n" + m.getName() + " - " + c.desc();
        }
        session.client.send(s);
    }
}
