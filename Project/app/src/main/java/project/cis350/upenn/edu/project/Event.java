package project.cis350.upenn.edu.project;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Event contains a start date & time and an end date & time. It can be marked as complete
 * or incomplete.
 */

public class Event implements Serializable, Comparable<Event> {
    private Calendar start;
    private Calendar end;
    private boolean completed;

    public Event(Calendar start, Calendar end) {
        if (start == null || end == null) throw new IllegalArgumentException("Cal is null.");
        this.start = start;
        this.end = end;
        completed = false;
    }

    public int getYearInt() {
        int year = start.get(Calendar.YEAR);
        return year;
    }

    public int getMonthInt() {
        int month = start.get(Calendar.MONTH);
        return month;
    }

    public int getDayInt() {
        int day = start.get(Calendar.DAY_OF_MONTH);
        return day;
    }

    public String getYear() {
        String year = "";
        int asInt = start.get(Calendar.YEAR);
        year = year + asInt;
        return year;
    }

    public String getMonth() {
        String month = "";
        int asInt = start.get(Calendar.MONTH);
        month = month + asInt;
        return month;
    }

    public String getDay() {
        String day = "";
        int asInt = start.get(Calendar.DAY_OF_MONTH);
        day = day + asInt;
        return day;
    }


    public Calendar getStart() { return start; }

    public Calendar getEnd() { return end; }

    public boolean isCompleted() { return completed; }

    public void markCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (!start.equals(event.start)) return false;
        return end.equals(event.end);

    }

    @Override
    public int hashCode() {
        int result = start.hashCode();
        result = 31 * result + end.hashCode();
        return result;
    }

    public int compareTo(Event other) {
        if (start.compareTo(other.start) == 0) {
            return end.compareTo(other.end);
        } else {
            return start.compareTo(other.start);
        }
    }

    /**
     * Displays the event's start and end times respectively in the format "hh:mm - hh:mm"
     */
    public String toStringHour() {
        String startMin = String.format("%02d", start.get(Calendar.MINUTE));
        String endMin = String.format("%02d", end.get(Calendar.MINUTE));
        return start.get(Calendar.HOUR_OF_DAY) + ":" + startMin + " - " +
                end.get(Calendar.HOUR_OF_DAY) + ":" + endMin;
    }

    @Override
    public String toString() {
        return  "Completed = " + completed + "\n" +
                "Start: " + start.get(Calendar.HOUR_OF_DAY) + ":" + start.get(Calendar.MINUTE) + " on " +
                start.get(Calendar.DATE) + "/" + start.get(Calendar.MONTH) + "/" + start.get(Calendar.YEAR) + "\n" +
                "End:  " + end.get(Calendar.HOUR_OF_DAY) + ":" + end.get(Calendar.MINUTE) + " on " +
                end.get(Calendar.DATE) + "/" + end.get(Calendar.MONTH) + "/" + end.get(Calendar.YEAR);
    }
}