package fi.kivikasvo.crackmud.controller;

import fi.kivikasvo.crackmud.HibernateUtil;
import fi.kivikasvo.crackmud.context.GameContext;
import fi.kivikasvo.crackmud.core.NoAnswerSubmittedForCheckException;
import fi.kivikasvo.crackmud.db.UserAccount;
import fi.kivikasvo.crackmud.db.Model;
import fi.kivikasvo.crackmud.db.Task;
import fi.kivikasvo.crackmud.db.exception.UserHasNoTaskByUUIDException;
import fi.kivikasvo.crackmud.puzzle.PuzzleResource;
import fi.kivikasvo.crackmud.puzzle.exception.InvalidAnswerFormattingException;
import fi.kivikasvo.crackmud.puzzle.exception.puzzleNotInLibraryException;
import fi.kivikasvo.crackmud.view.GameView;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Controller {

    public static void listTasks(GameContext context, GameView view) {
        view.onListTasks(Model.getTasksForUser(context.getSession().getUserAccount()));
    }

    public static void requestTask(GameContext context, GameView view, String taskId) {
        try {
            UserAccount user = context.getSession().getUserAccount();
            Task task = PuzzleResource.createTaskForUser(taskId, user);
            Model.addTaskForUser(user, task);
            view.onNewTaskReceived(task);
        } catch (puzzleNotInLibraryException ex) {
            view.onInvalidTaskRequested(taskId);
        }
    }

    public static void answerTask(GameContext context, GameView view, String taskUUID, String answer) {
        try {
            UserAccount userAccount = context.getSession().getUserAccount();
            Task task = Model.getTaskForUserByUUID(userAccount, taskUUID);
            if (task.getRemainingTries() <= 0) {
                view.onOutOfAnswerTries(answer);
                return;
            }
            task.submitAnswer(answer);
            view.onAnswerSubmitted(task);
        } catch (UserHasNoTaskByUUIDException ex) {
            view.onTriedToAnswerInvalidTaskUUID(taskUUID);
        } catch (InvalidAnswerFormattingException ex) {
            view.onInvalidAnswerFormatting(answer);
        }
    }

    public static void listAvailable(GameContext context, GameView view) {
        ArrayList<String> available = new ArrayList<>(PuzzleResource.library.keySet());
        view.onAvailableTasksRequested(available);
    }

    public static void checkTaskCorrectness(GameContext context, GameView view, String taskUUID) {
        try {
            UserAccount userAccount = context.getSession().getUserAccount();
            Task task = Model.getTaskForUserByUUID(userAccount, taskUUID);
            TimerTask tt = new TimerTask() {
                public void run() {
                    try {
                        view.onCheckedCorrectness(task.getCorrectness());
                    } catch (NoAnswerSubmittedForCheckException ex) {
                        view.onInvalidAnswerFormattingOnCheck();
                    }
                }
            };
            Timer timer = new Timer("Timer");
            long delay = 1000L;
            timer.schedule(tt, delay);
        } catch (UserHasNoTaskByUUIDException ex) {
            view.onTriedToCheckInvalidTaskUUID(taskUUID);
        }
    }

    public static void collectRewardsForTasks(GameContext context, GameView view) {
        UserAccount userAccount = context.getSession().getUserAccount();
        List<Task> tasksForUser = Model.getTasksForUser(userAccount);
        
    }

}
