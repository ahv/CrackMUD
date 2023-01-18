package fi.kivikasvo.crackmud.view;

import fi.kivikasvo.crackmud.db.Task;
import fi.kivikasvo.crackmud.db.UserAccount;
import fi.kivikasvo.crackmud.network.Client;
import java.util.ArrayList;
import java.util.List;

public class GameView extends View {

    public GameView(Client client) {
        super(client);
    }

    public void onListTasks(List<Task> tasksForUser) {
        String list = "--- TASKS ---------------------------------------------------------------------------------------\n";
        list += "UUID,                                TYPE, VALUE1, VALUE2, ANSWER, STATUS,   TRIES, TIME\n";
        for (Task t : tasksForUser) {
            String line = String.format("%s %-5s %s  %s  %-6s  %-9s %-2s     %s",
                    t.getUUID(), t.getPuzzleId(), t.getHex1(), t.getHex2(),
                    t.getGivenAnswer(), t.getStatus(), t.getRemainingTries(), t.getRemainingTimeString());
            list += line + "\n";
        }
        list += "-------------------------------------------------------------------------------------------------";
        send(list);
    }

    public void onNewTaskReceived(Task t) {
        String m = String.format("New task received. UUID: %s Type: %s Values: %s, %s", t.getUUID(), t.getPuzzleId(), t.getHex1(), t.getHex2());
        send(m);
    }

    public void onInvalidTaskRequested(String taskId) {
        send("Invalid task type.");
    }

    public void onTriedToAnswerInvalidTaskUUID(String taskUUID) {
        send("Invalid task UUID, cannot submit answer.");
    }

    public void onAnswerSubmitted(Task task) {
        send("Answer submitted.");
    }

    public void onAvailableTasksRequested(ArrayList<String> available) {
        String print = "Available Tasks:";
        for (String s : available) {
            print += "\n" + s;
        }
        send(print);
    }

    public void onCheckedCorrectness(String correctness) {
        send(correctness);
    }

    public void onTriedToCheckInvalidTaskUUID(String taskUUID) {
        send("Invalid task UUID.");
    }

    public void onInvalidAnswerFormatting(String answer) {
        send("Invalid answer formatting. Needs to be 6 symbols long with each symbol in the range of 0-9 and A-F");
    }

    public void onOutOfAnswerTries(String answer) {
        send("Can't submit answer. Out of tries.");
    }

    public void onStats(UserAccount userAccount) {
        // TODO: stats
    }

    public void onInvalidAnswerFormattingOnCheck() {
        send("No answer submitted yet, cannot check correctness.");
    }
}
