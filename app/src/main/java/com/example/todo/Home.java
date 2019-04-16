package com.example.todo;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todo.db.TaskContract;
import com.example.todo.db.TaskDbHelper;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class Home extends AppCompatActivity  implements View.OnClickListener   {
    private static final String TAG = "MainActivity";
    private  EditText taskEditText,editTextTask;
    private FloatingActionButton floatingActionButton;
    private AlertDialog dialog;
    private TaskDbHelper mHelper;
    private ListView mTaskListView;
    private ArrayAdapter<String> mAdapter;
    private Dialog dialogAddTask;
    private Button buttonAdd, buttonCancel;
    private String task;
    private FirebaseAuth firebaseAuth;
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
//        SQLiteDatabase db = mHelper.getReadableDatabase();
//        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
//                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE},
//                null, null, null, null, null);
//        while(cursor.moveToNext()) {
//            int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
//            updateUI();
//
//        }
//        cursor.close();
//        db.close();
//        updateUI();

//        buttonAdd.setOnClickListener(this);
        firebaseAuth = FirebaseAuth.getInstance();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new TodoFragment()).commit();//Init Todo fragment
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavigationListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        firebaseAuth.signOut();
        startActivity(new Intent(this, Login.class));
        return super.onOptionsItemSelected(item);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavigationListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;
            switch (menuItem.getItemId()){
                case R.id.nav_todo:
                    selectedFragment = new TodoFragment();
                    break;
                case R.id.nav_reminder:
                    selectedFragment = new ReminderFragment();
                    break;
                case R.id.nav_about:
                    selectedFragment = new AboutFragment();
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, selectedFragment).commit();
            return true;
        }

    };
//    @Override
    public void onClick(View v) {
//        if(v == floatingActionButton) {
//            dialogAddTask.setContentView(R.layout.alert_add_task);
//            final TextView textViewWarning = dialogAddTask.findViewById(R.id.textViewWarning);
//            textViewWarning.setVisibility(View.INVISIBLE);
//            dialogAddTask.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            buttonAdd = dialogAddTask.findViewById(R.id.buttonAdd);
//            buttonCancel = dialogAddTask.findViewById(R.id.buttonCancel);
//            editTextTask = dialogAddTask.findViewById(R.id.editTextTask);
//            mHelper = new TaskDbHelper(this);
//            dialogAddTask.show();
//            buttonCancel.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dialogAddTask.dismiss();
//                }
//            });
//            buttonAdd.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    task = editTextTask.getText().toString().trim();
//                    if (TextUtils.isEmpty(task)){
//                        textViewWarning.setVisibility(View.VISIBLE);
//                        return;
//                    }
//                    SQLiteDatabase db = mHelper.getWritableDatabase();
//                    ContentValues values = new ContentValues();
//                    values.put(TaskContract.TaskEntry.COL_TASK_TITLE, task);
//                    db.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
//                            null,
//                            values,
//                            SQLiteDatabase.CONFLICT_REPLACE);
//                    db.close();
//                    dialogAddTask.dismiss();
//                    updateUI();
//                }
//            });
//
//        }
//
        }
//    private void updateUI() {
//        ArrayList<String> taskList = new ArrayList<>();
//        SQLiteDatabase db = mHelper.getReadableDatabase();
//        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
//                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE},
//                null, null, null, null, null);
//        while (cursor.moveToNext()) {
//            int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
//            taskList.add(cursor.getString(idx));
//        }
//
//        if (mAdapter == null) {
//            mAdapter = new ArrayAdapter<>(this,
//                    R.layout.item_todo,
//                    R.id.task_title,
//                    taskList);
//            mTaskListView.setAdapter(mAdapter);
//        } else {
//            mAdapter.clear();
//            mAdapter.addAll(taskList);
//            mAdapter.notifyDataSetChanged();
//        }
//
//        cursor.close();
//        db.close();
//    }
//    public void deleteTask(View view) {
//        View parent = (View) view.getParent();
//        TextView taskTextView =  parent.findViewById(R.id.task_title);
//        String task = String.valueOf(taskTextView.getText());
//        SQLiteDatabase db = mHelper.getWritableDatabase();
//        db.delete(TaskContract.TaskEntry.TABLE,
//                TaskContract.TaskEntry.COL_TASK_TITLE + " = ?",
//                new String[]{task});
//        db.close();
//        updateUI();
//    }

}
