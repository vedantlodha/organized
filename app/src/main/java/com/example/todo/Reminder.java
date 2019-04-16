package com.example.todo;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

public class Reminder {
    private String reminderTitle, reminderDescription;
    private Calendar dateTime;


    public void setReminderTitle(String reminderTitle) {
        this.reminderTitle = reminderTitle;
    }

    public void setReminderDescription(String reminderDescription) {
        this.reminderDescription = reminderDescription;
    }

    public void setDate(Calendar date) {
        this.dateTime = date;
    }



    public String getReminderTitle() {
        return reminderTitle;
    }

    public String getReminderDescription() {
        return reminderDescription;
    }

    public Calendar getDate() {
        return dateTime;
    }



    public Reminder(String reminderTitle, String reminderDescription, Calendar date) {
        this.reminderTitle = reminderTitle;
        this.reminderDescription = reminderDescription;
        this.dateTime = date;

    }
    public static void main(String args[]) {
//        Calendar cal = new Calendar(
//        Time time = new Time(13, 30, 00);
//        com.example.todo.Reminder rem = new com.example.todo.Reminder("aaa", "FDafas", date, time);
//        System.out.print(time.get);

    }
}
