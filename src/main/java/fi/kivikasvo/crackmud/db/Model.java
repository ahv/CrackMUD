
package fi.kivikasvo.crackmud.db;

import fi.kivikasvo.crackmud.core.HibernateUtil;
import fi.kivikasvo.crackmud.exception.UserHasNoTaskByUUIDException;
import java.util.List;

public class Model {

    public static List<Task> getTasksForUser(UserAccount user) {
        return user.getTaskListCopy();
    }

    public static void addTaskForUser(UserAccount user, Task task) {
        user.addTask(task);
    }

    public static Task getTaskForUserByUUID(UserAccount user, String taskUUID) throws UserHasNoTaskByUUIDException {
        if (user.getTaskByUUID(taskUUID) == null) throw new UserHasNoTaskByUUIDException("No task with UUID: " + taskUUID);
        return user.getTaskByUUID(taskUUID);
    }
}
