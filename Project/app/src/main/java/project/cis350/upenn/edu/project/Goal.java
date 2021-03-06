package project.cis350.upenn.edu.project;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Goal contains a name, a reason for that goal, and a set of events for which
 * that goal takes place
 */

public class Goal implements Serializable, Comparable<Goal> {
    private String name;
    protected Set<Event> events;
    private String reason = "";

    public Goal(String name) {
        this.name = name;
        events = new TreeSet<>();
    }

    public String getName() {
        return name;
    }

    public void addEvent(Event e) {
        events.add(e);
    }

    public void removeEvent(Event e) {
        events.remove(e);
    }

    /**
     * Calculates percentage of goal events that have been completed.
     * @return ratio of completed events to total events
     */
    public double getTotalCompletion() {
        int total = 0;
        int completed = 0;
        for (Event e : events) {
            total++;
            if (e.isCompleted()) completed++;
        }
        if (total == 0) return 1.0;
        else return (double) completed / (double) total;
    }

    /**
     * Calculates percentage of goal events that have been completed and converts result into a
     * String percentage with at most 4 digits.
     * @return percentage of completed events with respect to total events
     */
    public String getTotalCompletionPercent() {
        String s = "" + getTotalCompletion()*100;
        if (s.length() > 5) { s = s.substring(0, 5); }
        return s + "%";
    }

    /**
     * Calculates percentage of goal events that have been completed during the current month.
     * @param month is the current month as an int
     * @return ratio of completed events this month to total events this month
     */
    public double getMonthlyCompletion(int month) {
        int total = 0;
        int completed = 0;
        for (Event e : events) {
            if (e.getStart().get(Calendar.MONTH) == month) {
                total++;
                if (e.isCompleted()) { completed++; }
            }
        }
        if (total == 0) return 1.0;
        else return (double) completed / (double) total;
    }

    /**
     * Calculates percentage of goal events that have been completed during the current month and
     * converts the result into a String percentage with at most 4 digits.
     * @param month is the current month as an int
     * @return percentage of completed events with respect to total events
     */
    public String getMonthlyCompletionPercent(int month) {
        String s = "" + getMonthlyCompletion(month)*100;
        if (s.length() > 5) { s = s.substring(0, 5); }
        return s + "%";
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void removeReason(String reason) {
        this.reason = "";
    }

    public String getReason() {
        return reason;
    }

    public Set<Event> getEvents() {
        return Collections.unmodifiableSet(events);
    }

    @Override
    public String toString() {
        return name + "\n \t Progress: " + getTotalCompletionPercent();
    }

    @Override
    public int compareTo(Goal o) {
        return name.compareTo(o.getName());
    }
}