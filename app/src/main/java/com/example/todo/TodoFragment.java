package com.example.todo;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.todo.db.TaskContract;
import com.example.todo.db.TaskDbHelper;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class TodoFragment extends Fragment implements View.OnClickListener {
    private EditText taskEditText, editTextTask;

    private FloatingActionButton floatingActionButton;
    private TextView textViewWarning;
    private TaskDbHelper mHelper;
    private ListView mTaskListView;
    private ArrayAdapter<String> mAdapter;
    private Dialog dialogAddTask;
    private Button buttonAdd, buttonCancel;
    private String task;
    private BottomNavigationView bottomNavigationView;
    private View viewx;
    Context context;
    ArrayList<String> taskList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.todo_layout, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //findViewById
        floatingActionButton = view.findViewById(R.id.floatingActionButton);
        mTaskListView =  view.findViewById(R.id.listView);
        dialogAddTask = new Dialog(getContext()); //for dialog box
        context = getContext();
        //db
        mHelper = new TaskDbHelper(getContext());
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE},
                null, null, null, null, null);
        while(cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
            updateUI();

        }
        cursor.close();
        db.close();
        updateUI();


        //OnClick
        floatingActionButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (v == floatingActionButton){
            dialogAddTask.setContentView(R.layout.alert_add_task);
            textViewWarning = dialogAddTask.findViewById(R.id.textViewWarning);
            textViewWarning.setVisibility(View.INVISIBLE);
            dialogAddTask.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            buttonAdd = dialogAddTask.findViewById(R.id.buttonAdd);
            buttonCancel = dialogAddTask.findViewById(R.id.buttonCancel);
            editTextTask = dialogAddTask.findViewById(R.id.editTextReminder);

            dialogAddTask.show();
            mHelper = new TaskDbHelper(getContext());
            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogAddTask.dismiss();
                }
            });
            buttonAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    task = editTextTask.getText().toString().trim();
                    if (TextUtils.isEmpty(task)){
                        textViewWarning.setVisibility(View.VISIBLE);
                        return;
                    }
                    SQLiteDatabase db = mHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(TaskContract.TaskEntry.COL_TASK_TITLE, task);
                    db.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                            null,
                            values,
                            SQLiteDatabase.CONFLICT_REPLACE);
                    db.close();
                    dialogAddTask.dismiss();
                    updateUI();
                }
            });
        }
    }
        private void updateUI() {
         taskList = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
            taskList.add(cursor.getString(idx));
        }


        mAdapter = new customAdapter();
        mTaskListView.setAdapter(mAdapter);

        cursor.close();
        db.close();
    }
    public void deleteTask(String title) {

        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(TaskContract.TaskEntry.TABLE,
                TaskContract.TaskEntry.COL_TASK_TITLE + " = ?",
                new String[]{title});
        db.close();
        updateUI();
    }

    class customAdapter extends ArrayAdapter<String>{
        public customAdapter(){
            super(context, R.layout.item_todo, taskList);

        }


        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            View itemView = convertView;
            if(convertView == null)
                itemView = getLayoutInflater().inflate(R.layout.item_todo, parent, false);
            String todo = taskList.get(position);
            TextView textViewTitle = itemView.findViewById(R.id.task_title);
            textViewTitle.setText(todo);
            CheckBox checkBox = itemView.findViewById(R.id.checkBoxTask);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String todoTitle = taskList.get(position);
                    deleteTask(todoTitle);
                }
            });




            return itemView;
        }
    }


}



























/*
TextView active =  itemView.findViewById(R.id.reminderTitle);
            active.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    arrayListReminder.remove(position);
                    updateUI();
//                    Toast.makeText(context, arrayListReminder.get(position).getReminderTitle(), Toast.LENGTH_SHORT).show();
                }
            });
 */