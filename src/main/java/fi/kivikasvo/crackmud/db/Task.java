package fi.kivikasvo.crackmud.db;

import fi.kivikasvo.crackmud.core.AnsiColor;
import fi.kivikasvo.crackmud.core.Hex;
import fi.kivikasvo.crackmud.exception.NoAnswerSubmittedForCheckException;
import fi.kivikasvo.crackmud.exception.InvalidAnswerFormattingException;
import fi.kivikasvo.crackmud.puzzle.PuzzleScript;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import org.apache.commons.lang3.time.DurationFormatUtils;

@Entity
public class Task implements Serializable {

    @Id
    String taskUUID;
    @ManyToOne
    UserAccount owner;
    String puzzleId;
    String hex1;
    String hex2;
    String solution;
    String answer;
    int remaining_tries;
    Task.Status status;
    LocalTime created_time;
    Duration time_limit;

    public enum Status {
        NEW,
        CORRECT,
        INCORRECT,
        EXPIRED
    }

    public Task(UserAccount owner, PuzzleScript puzzle) {
        this.taskUUID = UUID.randomUUID().toString();
        this.owner = owner;
        this.puzzleId = puzzle.getId();
        this.hex1 = Hex.gen();
        this.hex2 = Hex.gen();
        this.solution = puzzle.solve(hex1, hex2);
        remaining_tries = 16;
        status = Status.NEW;
        this.created_time = LocalTime.now();
        time_limit = Duration.ofHours(1);
    }

    public String getUUID() {
        return taskUUID;
    }

    public UserAccount getOwner() {
        return owner;
    }

    public LocalTime getCreated_timestamp() {
        return created_time;
    }

    public String getHex1() {
        return hex1;
    }

    public String getHex2() {
        return hex2;
    }

    public String getSolution() {
        return solution;
    }

    public String getPuzzleId() {
        return puzzleId;
    }

    public void submitAnswer(String answer) throws InvalidAnswerFormattingException {
        if (!validateAnswer(answer)) {
            throw new InvalidAnswerFormattingException(answer);
        }
        this.answer = answer;
        status = isAnswerCorrect() ? Status.CORRECT : Status.INCORRECT;
        remaining_tries--;
        owner.save();
    }

    // TODO: Crude solution - try turning chars to values and see if they're in a proper range instead
    private boolean validateAnswer(String answer) {
        if (answer.length() != 6) {
            return false;
        }
        String validSymbols = "0123456789ABCDEF";
        for (char c : answer.toCharArray()) {
            if (validSymbols.indexOf(c) == -1) {
                return false;
            }
        }
        return true;
    }

    public String getGivenAnswer() {
        return answer;
    }

    public boolean isAnswerCorrect() {
        return answer.equals(solution);
    }

    public int getRemainingTries() {
        return remaining_tries;
    }

    public Status getStatus() {
        getRemainingTimeMillis(); // Lazy hax to set status to EXPIRED
        return status;
    }

    public long getRemainingTimeMillis() {
        long millisToExpiration = LocalTime.now().until(created_time.plus(time_limit), ChronoUnit.MILLIS);
        if (millisToExpiration <= 0) {
            status = Status.EXPIRED;
        }
        return millisToExpiration <= 0 ? 0 : millisToExpiration;
    }

    public String getRemainingTimeString() {
        return DurationFormatUtils.formatDuration(getRemainingTimeMillis(), "H:mm:ss", true);
    }

    public String getCorrectness() throws NoAnswerSubmittedForCheckException {
        if (answer == null) {
            throw new NoAnswerSubmittedForCheckException();
        }
        String re = "Your answer:        " + answer + "\n";
        re += "Correctness result: ";
        for (int i = 0; i < 6; i++) {
            if (solution.charAt(i) == answer.charAt(i)) {
                re += AnsiColor.ANSI_GREEN + "*";
            } else if (solution.indexOf(answer.charAt(i)) != -1) {
                re += AnsiColor.ANSI_YELLOW + "+";
            } else {
                re += AnsiColor.ANSI_RED + "-";
            }
        }
        re += AnsiColor.ANSI_RESET + "\n* = correct symbols in correct spot\n+ = correct symbol in incorrect spot\n- = symbol not part of solution";
        return re;
    }

    // Required by Hibernate
    protected Task() {
        
    }

    int getRemaining_tries() {
        return remaining_tries;
    }

    void setRemaining_tries(int remaining_tries) {
        this.remaining_tries = remaining_tries;
    }

    String getAnswer() {
        return answer;
    }

    void setAnswer(String answer) {
        this.answer = answer;
    }

    String getTaskUUID() {
        return taskUUID;
    }

    void setTaskUUID(String taskUUID) {
        this.taskUUID = taskUUID;
    }

    LocalTime getCreated_time() {
        return created_time;
    }

    void setCreated_time(LocalTime created_time) {
        this.created_time = created_time;
    }

    Duration getTime_limit() {
        return time_limit;
    }

    void setTime_limit(Duration time_limit) {
        this.time_limit = time_limit;
    }

    public void setOwner(UserAccount owner) {
        this.owner = owner;
    }

    void setPuzzleId(String puzzleId) {
        this.puzzleId = puzzleId;
    }

    void setHex1(String hex1) {
        this.hex1 = hex1;
    }

    void setHex2(String hex2) {
        this.hex2 = hex2;
    }

    void setSolution(String solution) {
        this.solution = solution;
    }

    void setStatus(Status status) {
        this.status = status;
    }
    
}
