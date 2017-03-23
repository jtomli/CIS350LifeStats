package project.cis350.upenn.edu.project;

import java.io.Serializable;

/**
 * Created by nkeen_000 on 2/22/2017.
 */

public class Event implements Serializable, Comparable<Event> {
    private int year;
    private int month;
    private int day;
    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;
    private boolean allDay;
    private boolean completed;

    public Event(int year, int month, int day, int startHour, int startMinute, int endHour,
                 int endMinute, boolean allDay) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
        this.allDay = allDay;
        completed = false;
    }

    public int getYear() { return year; }

    public int getMonth() { return month; }

    public int getDay() { return day; }

    public int getStartHour() { return startHour; }

    public int getStartMinute() { return startMinute; }

    public int getEndHour() { return endHour; }

    public int getEndMinute() { return endMinute; }

    public boolean isAllDay() { return allDay; }

    public boolean isCompleted() { return completed; }

    public void markCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (year != event.year) return false;
        if (month != event.month) return false;
        if (day != event.day) return false;
        if (allDay && event.allDay) return true;
        if (startHour != event.startHour) return false;
        if (startMinute != event.startMinute) return false;
        if (endHour != event.endHour) return false;
        if (endMinute != event.endMinute) return false;
        return allDay == event.allDay;

    }

    @Override
    public int hashCode() {
        int result = year;
        result = 31 * result + month;
        result = 31 * result + day;
        result = 31 * result + startHour;
        result = 31 * result + startMinute;
        result = 31 * result + endHour;
        result = 31 * result + endMinute;
        result = 31 * result + (allDay ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        // Returns in the following format:
        // Event on mm/dd/yyyy from hh:mm to hh:mm is (not) completed
        // Event on mm/dd/yyyy all day is (not) completed

        StringBuilder s = new StringBuilder();

        if (completed) s.append("Complete goal:      ");
        else           s.append("Incomplete goal:   ");

        // pad months, days, hours, and minutes with 0's if necessary
        if (month < 10) s.append("0" + month);
        else            s.append(month);

        s.append("/");

        if (day < 10) s.append("0" + day);
        else          s.append(day);

        s.append("/");

        if (year < 100) s.append("20" + year);
        else            s.append(year);

        if (allDay) s.append(" all day");
        else {
            s.append(" from ");
            if (startHour < 10) s.append("0" + startHour + ":");
            else                s.append(startHour + ":");

            if (startMinute < 10) s.append("0" + startMinute);
            else                  s.append(startMinute);

            s.append(" to ");

            if (endHour < 10) s.append("0" + endHour + ":");
            else                s.append(endHour + ":");

            if (endMinute < 10) s.append("0" + endMinute);
            else                  s.append(endMinute);
        }

        return s.toString();
    }

    @Override
    // if this.compareTo(e) returns 1, then this is earlier
    public int compareTo(Event e) {
        if (year == e.year) {
            if (month == e.month) {
                if (day == e.day) {
                    if (allDay && e.allDay) return 0;
                    else if (allDay && !e.allDay) return 1;
                    else if (!allDay && e.allDay) return -1;
                    else {
                        if (startHour == e.startHour) {
                            if (startMinute == e.startMinute) {
                                if (endHour == e.endHour) {
                                    if (endMinute == e.endMinute) {
                                        return 0;
                                    } else if (endMinute < e.endMinute) {
                                        return -1;
                                    } else {
                                        return 1;
                                    }
                                } else if (endHour < e.endHour) {
                                    return -1;
                                } else {
                                    return 1;
                                }
                            } else if (startMinute < e.startMinute) {
                                return -1;
                            } else {
                                return 1;
                            }
                        } else if (startHour < e.startHour) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                } else if (day < e.day) {
                    return -1;
                } else {
                    return 1;
                }
            } else if (month < e.month) {
                return -1;
            } else {
                return 1;
            }
        } else if (year < e.year) {
            return -1;
        } else {
            return 1;
        }
    }
}
