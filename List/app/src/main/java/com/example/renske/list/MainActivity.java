package com.example.renske.list;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private EditText userInput;
    private ListView mainListView;
    private ArrayAdapter<String> listAdapter;
    private ArrayList<String> todoList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        mainListView = (ListView) findViewById(R.id.todolist);

        // get list from preferences
        SharedPreferences savedList = this.getSharedPreferences("prefs", MODE_PRIVATE);
        Set<String> set = savedList.getStringSet("theSavedList", null);

        // if there are preferences, use those
        if (set != null) {
            List<String> savedlist = new ArrayList<String>(set);
            todoList = new ArrayList<String>();
            todoList.addAll(savedlist);
        }

        // else start with an empty list with 1 example
        else {
            // create full new list?
            String[] initialtodos = new String[]{"Wash the dishes"};
            todoList = new ArrayList<String>();
            todoList.addAll(Arrays.asList(initialtodos));
        }

        // create and set arrayadapter
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoList);
        mainListView.setAdapter(listAdapter);
        mainListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                // determine which string to remove
                String toremove = String.valueOf(parent.getItemAtPosition(position));
                listAdapter.remove(toremove);

                // place updated list in sharedprefs
                SharedPreferences prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = prefs.edit();

                Set<String> set = new HashSet<String>();
                set.addAll(todoList);
                edit.putStringSet("theSavedList", set);
                edit.commit();

                // notify user it has been deleted
                Toast.makeText(MainActivity.this, toremove + " has been deleted.", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

    }

    // put user input in the array
    public void putTodoInArray(View view) {

        userInput = (EditText) findViewById(R.id.userinput);
        String usertodo = userInput.getText().toString();

        if (usertodo.length() == 0) {
            Toast.makeText(MainActivity.this, "Please fill in a todo first!", Toast.LENGTH_SHORT).show();
        }

        // hide keyboard once Go button is pressed
        // source: http://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

        listAdapter.add(usertodo);
        userInput.getText().clear();

        SharedPreferences prefs = this.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();

        Set<String> set = new HashSet<String>();
        set.addAll(todoList);
        edit.putStringSet("theSavedList", set);
        edit.commit();


    }


}





