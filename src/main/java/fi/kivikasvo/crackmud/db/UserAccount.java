package fi.kivikasvo.crackmud.db;

import fi.kivikasvo.crackmud.core.HibernateUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class UserAccount implements Serializable {

    @Id
    String name;
    String passhash;
    @OneToMany(mappedBy="owner", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    List<Task> tasks;

    int freeCreditLimit;
    int credits;

    protected UserAccount() {
        this("guest", "");
    }

    public UserAccount(String name, String password) {
        tasks = new ArrayList<>();
        this.name = name;
        this.passhash = password; // TODO: :)
        freeCreditLimit = 10;
        credits = 0;
    }

    public void addTask(Task task) {
        tasks.add(task);
        save();
    }

    public ArrayList<Task> getTaskListCopy() {
        return new ArrayList<Task>(tasks);
    }

    public Task getTaskByUUID(String taskUUID) {
        for (Task t : tasks) {
            if (t.getUUID().equals(taskUUID)) return t;
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasshash() {
        return passhash;
    }

    public void setPasshash(String passhash) {
        this.passhash = passhash;
    }

    public int getFreeCreditLimit() {
        return freeCreditLimit;
    }

    public void setFreeCreditLimit(int creditLimit) {
        this.freeCreditLimit = creditLimit;
        save();
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
        save();
    }

    public void save() {
        HibernateUtil.save(this);
    }

}
