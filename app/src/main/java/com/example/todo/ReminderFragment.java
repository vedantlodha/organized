package com.example.todo;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import java.util.Date;
import java.text.ParseException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.todo.db.TaskContract;
import com.example.todo.db.TaskDbHelper;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
    private TaskDbHelper mHelper;

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
        mHelper = new TaskDbHelper(getContext());
        parentView = view;


        //onClickListener
        floatingActionButton.setOnClickListener(this);


        //db


        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE_REMINDERS,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_REMINDER_TITLE, TaskContract.TaskEntry.COL_REMINDER_DESCRIPTION},
                null, null, null, null, null);

        while(cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_REMINDER_TITLE);

            try {
                updateUI();
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }
        cursor.close();
        db.close();

        try {
            updateUI();
        } catch (ParseException e) {
            e.printStackTrace();
        }



        registerForContextMenu(listView);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(Menu.NONE, R.id.delete_item, menu.NONE, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int pos = info.position;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Do you wish to delete the reminder?");
        builder.setTitle("Confirm delete");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    deleteReminder(pos);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        })
                .setNegativeButton("No", null);
        AlertDialog dialog = builder.create();
        dialog.show();
        return super.onContextItemSelected(item);
    }

    private void deleteReminder(int pos) throws ParseException {
            String title = arrayListReminder.get(pos).getReminderTitle();
            SQLiteDatabase db = mHelper.getWritableDatabase();
            db.delete(TaskContract.TaskEntry.TABLE_REMINDERS,
                    TaskContract.TaskEntry.COL_REMINDER_TITLE + " = ?",
                    new String[]{title});
            db.close();
            updateUI();
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
                        warning.setText("Reminder name cannot be empty");
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
                    try {
                        updateUI();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    dialogAddReminder.dismiss();
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
        String dateTime = year +  "-" + month + "-" + day + "-" + hour + ":" + min + ":00";
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COL_REMINDER_TITLE, reminderName);
        values.put(TaskContract.TaskEntry.COL_REMINDER_DESCRIPTION, reminderDescription);
        values.put(TaskContract.TaskEntry.COL_REMINDER_DATETIME, dateTime);
        db.insertWithOnConflict(TaskContract.TaskEntry.TABLE_REMINDERS,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE);
        db.close();




    }
    private void updateUI () throws ParseException {
        arrayListReminder = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE_REMINDERS,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_REMINDER_TITLE, TaskContract.TaskEntry.COL_REMINDER_DESCRIPTION, TaskContract.TaskEntry.COL_REMINDER_DATETIME},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            Calendar calendar = Calendar.getInstance();
            int idTitle = cursor.getColumnIndex(TaskContract.TaskEntry.COL_REMINDER_TITLE);
            int idDes = cursor.getColumnIndex(TaskContract.TaskEntry.COL_REMINDER_DESCRIPTION);
            int idDt = cursor.getColumnIndex(TaskContract.TaskEntry.COL_REMINDER_DATETIME);
            String dateTime = cursor.getString(idDt);
            String arr[] = dateTime.split("\\s*-\\s*");
            int yearStr = Integer.parseInt(arr[0]);
            int monthStr = Integer.parseInt(arr[1]);
            int dateStr = Integer.parseInt(arr[2]);
            String time = arr[3];
            String timeArr[] = time.split(":");
            int hour = Integer.parseInt(timeArr[0]);
            int min = Integer.parseInt(timeArr[1]);
            calendar.set(Calendar.YEAR, yearStr);
            calendar.set(Calendar.MONTH, monthStr);
            calendar.set(Calendar.DATE, dateStr);
            calendar.set(Calendar.HOUR, hour);
            calendar.set(Calendar.MINUTE    , min);
            calendar.set(Calendar.SECOND, 0);
            Reminder reminder = new Reminder(cursor.getString(idTitle), cursor.getString(idDes), calendar);
            arrayListReminder.add(reminder);
        }

        cursor.close();
        db.close();


      ArrayAdapter<Reminder> arrayAdapter = new customAdapter();
      listView.setAdapter(arrayAdapter);
    }


    public class customAdapter extends ArrayAdapter<Reminder> {
            public customAdapter() {
                super(context,R.layout.item_reminder, arrayListReminder );
            }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
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
            // (2) create a date "formatter" (the date format we want)
            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm");

            // (3) create a new String using the date format we want
            String time = formatter.format(reminder.getDate().getTime());
            textViewTime.setText(time);


                return itemView;
        }



    }


}

