package com.example.todo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import org.w3c.dom.Text;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;

public class ReminderFragment extends Fragment implements View.OnClickListener {
    private FloatingActionButton floatingActionButton;
    private ListView listView;
    private Context context;
    private Dialog dialogAddReminder;
    private TextView textViewDate, textViewTime, warning;
    private Button buttonAdd, buttonCancel;
    private EditText editTextReminderName, editTextReminderDescription;
    private String reminderName, reminderDescription;
    private int month,day,year, min, hour;
    private Calendar currentDate;
    private boolean isDateSet, isTimeSet;
    private  View parentView;
    private ArrayList<Reminder> arrayListReminder = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.reminder_layout, container, false);
        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        floatingActionButton = view.findViewById(R.id.floatingActionButtonReminder);
        listView = view.findViewById(R.id.listViewReminder);
        context = getContext();
        dialogAddReminder = new Dialog(context);

        parentView = view;


        //onClickListener
        floatingActionButton.setOnClickListener(this);
        updateUI();

        //


    }

    @Override
    public void onClick(View v) {
        if(v == floatingActionButton){
            //date time init
            currentDate = Calendar.getInstance();
            day = currentDate.get(Calendar.DATE);
            month = currentDate.get(Calendar.MONTH);
            year = currentDate.get((Calendar.YEAR));
            hour = currentDate.get(Calendar.HOUR);
            min = currentDate.get(Calendar.MINUTE);
            isDateSet = false;
            isTimeSet = false;

            //COMPONENT INIT
            dialogAddReminder.setContentView(R.layout.alert_add_reminder);
            warning = dialogAddReminder.findViewById(R.id.textViewReminderWarning);
            editTextReminderDescription = dialogAddReminder.findViewById(R.id.editTextDescription);
            editTextReminderName = dialogAddReminder.findViewById(R.id.editTextReminder);
            textViewDate = dialogAddReminder.findViewById(R.id.textViewDatePicker);
            textViewTime = dialogAddReminder.findViewById(R.id.textViewTimePicker);
            buttonAdd = dialogAddReminder.findViewById(R.id.buttonAddReminder);
            buttonCancel = dialogAddReminder.findViewById(R.id.buttonCancelReminder);
            warning.setVisibility(View.INVISIBLE);
            dialogAddReminder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogAddReminder.show();
            buttonAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reminderName = editTextReminderName.getText().toString().trim();
                    reminderDescription = editTextReminderDescription.getText().toString().trim();
                    if(TextUtils.isEmpty(reminderName)) {
                        warning.setText("com.example.todo.Reminder name cannot be empty");
                        warning.setVisibility(View.VISIBLE);
                        return;
                    }
                    else if(TextUtils.isEmpty(reminderDescription)){
                        warning.setText("Description cannot be empty");
                        warning.setVisibility(View.VISIBLE);
                        return;
                    }
                    else if(! isDateSet) {
                        warning.setText("Date not set");
                        warning.setVisibility(View.VISIBLE);
                        return;
                    }
                    else if (! isTimeSet) {
                        warning.setText("Time not set");
                        warning.setVisibility(View.VISIBLE);
                        return;
                    }
                    addToDb(reminderName, reminderDescription, day, month, year, hour, min);
                }
            });
            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogAddReminder.dismiss();
                }
            });
            textViewDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int yearDialog, int monthOfTheYear, int dayOfMonth) {
                            monthOfTheYear += 1;
                            day = dayOfMonth;
                            month = monthOfTheYear;
                            year = yearDialog;
                            textViewDate.setText(day + "/" + month + "/" + year);
                            isDateSet = true;
                        }
                    }, year, month, day);
                    datePickerDialog.show();
                }
            });
            textViewTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour) {
                            hour = hourOfDay;
                            min = minuteOfHour;
                            String am_pm = "am";
                            if (hourOfDay > 12){
                                hourOfDay -= 12;
                                am_pm = "pm";
                            }
                            textViewTime.setText(hourOfDay + ":" + minuteOfHour + " "+ am_pm);
                            isTimeSet = true;
                        }
                    }, hour, min, false);
                    timePickerDialog.show();
                }
            });
        }
    }
    private void addToDb(String reminderName, String reminderDescription, int day, int month, int year, int hour, int min){

    }
    private void updateUI () {
        arrayListReminder = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        Time time = new Time(11,2,22);
        arrayListReminder.add(new Reminder("Reminder 1", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu.", calendar, time));
        arrayListReminder.add(new Reminder("Reminder 2", "Description 2", calendar, time));

        arrayListReminder.add(new Reminder("Reminder 3", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. 3", calendar, time));
        arrayListReminder.add(new Reminder("Reminder 4", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulsunt in culpa qui officia deserunt mollit anim id est laborum. 4", calendar, time));
        arrayListReminder.add(new Reminder("Reminder 5", "Description 5", calendar, time));
        arrayListReminder.add(new Reminder("Reminder 6", "Description 6", calendar, time));


      ArrayAdapter<Reminder> arrayAdapter = new customAdapter();
      listView.setAdapter(arrayAdapter);
    }


    public class customAdapter extends ArrayAdapter<Reminder> {
            public customAdapter() {
                super(context,R.layout.item_reminder, arrayListReminder );
            }


        @Override
        public View getView(int position,View convertView,ViewGroup parent) {
                View itemView = convertView;
                if(convertView == null)
                    itemView = getLayoutInflater().inflate(R.layout.item_reminder, parent, false);
                Reminder reminder = arrayListReminder.get(position);
            TextView textViewTitle = itemView.findViewById(R.id.reminderTitle);
            TextView textViewDescription = itemView.findViewById(R.id.textViewDescription);
            TextView textViewDate = itemView.findViewById(R.id.textViewDate);
            TextView textViewTime = itemView.findViewById(R.id.textViewTime);
            textViewTitle.setText(reminder.getReminderTitle());
            textViewDescription.setText(reminder.getReminderDescription());
            Calendar calendar = reminder.getDate();
            String date = calendar.get(Calendar.DATE) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR);
            textViewDate.setText(date);
            textViewTime.setText(reminder.getTime().toString());


                return itemView;
        }
        //        @Override
//        public int getCount() {
//            int length = arrayListReminder.size();
//            return length;
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return null;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return 0;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            convertView = getLayoutInflater().inflate(R.layout.item_reminder, null);
//            TextView textViewTitle = convertView.findViewById(R.id.reminderTitle);
//            TextView textViewDescription = convertView.findViewById(R.id.textViewDescription);
//            TextView textViewDate = convertView.findViewById(R.id.textViewDate);
//            TextView textViewTime = convertView.findViewById(R.id.textViewTime);
//
//            return null;
//        }
    }
}

