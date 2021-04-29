package siebelunited.cs465.illinois.edu.focus;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.Serializable;
import java.util.ArrayList;

import siebelunited.cs465.illinois.edu.focus.Task.*;


/**
 *
 * Structure:
 * There are 2 activities: main activity and timer activity. Main activity is the default activity, it contains the homepage and the setup page.
 * Whenever the app is boosted, we create a copy of the homepage (a fragment), and renders it in the frame layout in activity_main.xml.
 * Whenever we tap Focus or About in the bottom nav bar, we create a corresponding fragment.
 *
 * Basically
 *           homepage <-------------------------|
 *              |                               |
 *              |                               |
 *              |                               |
 *         setup page                           |
 *              |                               |
 *              |                               |
 *              |                               |
 *         Select Task------Add Task            |
 *              |          |                    |
 *              |      -----                    |
 *              |     |                         |
 *           Timer<---                          |
 *              |                               |
 *              |                               |
 *              |                               |
 *           ExpGain ---------------------------|
 *
 */


public class MainActivity extends AppCompatActivity implements SelectTaskDialog.SelectTaskDialogListener, AddTaskDialog.AddTaskDialogListener {

//    Define all the layouts that we might access
    LinearLayout todo_tab, stat_tab, todo_body, stat_body, task_list;
    ImageButton todo_button, stat_button;


//    Define all the variables about the user. We might want to store these in a local file later on
    String username;
    int user_level;
    int user_exp;
    int tasks_completed;
    int time_focused_hrs;
    int time_focused_min;
    private static final int max_exp = 100;
    private static final int max_focus_time_min = 240;
    ArrayList<Task> tasks;

//    The duration (in hours and minutes) as well as name of our current task.
    int curr_hrs;
    int curr_min;
    String curr_task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



//        Hard coded parameters
        username = "???";
        user_level = 10;
        user_exp = 42;
        tasks_completed = 5;
        time_focused_hrs = 5;
        time_focused_min = 30;
        tasks = new ArrayList<>();
        tasks.add(new Task("CS465 HW", 0));
        tasks.add(new Task("100 pushups", 1));
        tasks.add(new Task("breath", 1));


//        Basically creates a copy of the homepage fragment and pass all the parameters to it.
//        The fragment will take these parameters and render the views accordingly.
        HomeFragment home = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("tasks", (Serializable)tasks);
        bundle.putString("username", username);
        bundle.putInt("user_lvl", user_level);
        bundle.putInt("user_exp", user_exp);
        bundle.putInt("tasks_completed", tasks_completed);
        bundle.putInt("focused_hrs", time_focused_hrs);
        bundle.putInt("focused_min", time_focused_min);
        home.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, home).commit();

//        Set a listener for the bottom nav bar.
        BottomNavigationView bottom_nav = findViewById(R.id.bottomNavigationView);
        bottom_nav.setOnNavigationItemSelectedListener(navListener);

    }

//    Get called whenever we click the list icon.
    public void list_onClick(View v) {
        todo_tab = (LinearLayout) findViewById(R.id.todo_tab);
        stat_tab = (LinearLayout) findViewById(R.id.stat_tab);
        todo_button = (ImageButton) findViewById(R.id.todo_button);
        stat_button = (ImageButton) findViewById(R.id.stat_button);
        todo_body = (LinearLayout) findViewById(R.id.todo_body);
        stat_body = (LinearLayout) findViewById(R.id.stat_body);

//        Change the background color of the two buttons, as well as set the list part visible and the statistics part invisible.
        todo_tab.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.layout_selected));
        stat_tab.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.layout_unselected));
        todo_button.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.list_icon_selected));
        stat_button.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bar_icon_unselected));
        todo_body.setVisibility(View.VISIBLE);
        stat_body.setVisibility(View.GONE);

    }

//    Same thing as above
    public void stat_onClick(View v) {
        todo_tab = (LinearLayout) findViewById(R.id.todo_tab);
        stat_tab = (LinearLayout) findViewById(R.id.stat_tab);
        todo_button = (ImageButton) findViewById(R.id.todo_button);
        stat_button = (ImageButton) findViewById(R.id.stat_button);
        todo_body = (LinearLayout) findViewById(R.id.todo_body);
        stat_body = (LinearLayout) findViewById(R.id.stat_body);


        stat_tab.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.layout_selected));
        todo_tab.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.layout_unselected));
        todo_button.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.list_icon_unselected));
        stat_button.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bar_icon_selected));
        todo_body.setVisibility(View.GONE);
        stat_body.setVisibility(View.VISIBLE);
    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        String task_name = ((CheckBox) view).getText().toString();
        Task current_task = getTask(task_name);

        if(checked) {
            current_task.is_finished = 0;
            tasks_completed -=1;
        } else {
            current_task.is_finished = 1;
            tasks_completed +=1;
        }
    }

    public Task getTask(String task_name) {
        for(Task task : tasks){
            if(task.task_name.equals(task_name)) {
                return task;
            }
        }

        return null;
    }


//    Bottom nav bar listener
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()) {
                        case R.id.about:
//                            If we tap About: create a homepage fragment
                            selectedFragment = new HomeFragment();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("tasks", (Serializable)tasks);
                            bundle.putString("username", username);
                            bundle.putInt("user_lvl", user_level);
                            bundle.putInt("user_exp", user_exp);
                            bundle.putInt("tasks_completed", tasks_completed);
                            bundle.putInt("focused_hrs", time_focused_hrs);
                            bundle.putInt("focused_min", time_focused_min);
                            selectedFragment.setArguments(bundle);
                            break;

                        case R.id.focus:
//                            If we tap focus: create a setup fragment
//                            Since we don't really need to display any info in the setup fragment,
//                            There's no parameter passed to it
                            selectedFragment = new SetupFragment();
                    }
//                    render the chosen fragment
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    return true;
                }
            };

//    Get called when tapping the submit button on the setup screen
    public void onSubmitSetup(View v) {
//        Take user input!
        EditText hours_input = findViewById(R.id.hrs_input);
        EditText min_input = findViewById(R.id.min_input);

        curr_hrs = Integer.parseInt(hours_input.getText().toString());
        curr_min = Integer.parseInt(min_input.getText().toString());

//        Render the progress bar accordingly
        ProgressBar setup_progress_bar = findViewById(R.id.setup_progress_bar);
        setup_progress_bar.setProgress((int) (((double) curr_hrs * 60 + curr_min) * 100 / (double) max_focus_time_min));

//        Render the text accordingly
        TextView setup_time = findViewById(R.id.setup_time);
        setup_time.setText(String.format("%d minutes", curr_hrs * 60 + curr_min));

//        Create a popup window.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(String.format("Start focusing for %d mins?", curr_hrs * 60 + curr_min))
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        If we click yes: render the task select window
                        popup_task_select();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Nothing happens if we click no
                        dialogInterface.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

//    Helper function
    public void popup_task_select() {
//        Creates a select task window
        SelectTaskDialog selectTaskDialog = new SelectTaskDialog();
        selectTaskDialog.show(getSupportFragmentManager(), "select_task");
    }

//    A callback function that is called when you select a task in a task select window
    @Override
    public void selectTask(String task_selected) {
//      Go to timer
        curr_task = task_selected;
//        Create an intent, which is kinda like a bundle. It can contain any number of parameters
        Intent intent = new Intent(this, TimerActivity.class);
        intent.putExtra("duration", curr_hrs * 60 + curr_min);
        intent.putExtra("task_name", curr_task);
        intent.putExtra("username", username);
        intent.putExtra("userlvl", String.valueOf(user_level));
//        Create a timer activity
        startActivityForResult(intent, 0);
    }

//    Same thing as above
    @Override
    public void addTask(String new_task) {
        curr_task = new_task;
        Intent intent = new Intent(this, TimerActivity.class);
        intent.putExtra("duration", curr_hrs * 60 + curr_min);
        intent.putExtra("task_name", curr_task);
        intent.putExtra("username", username);
        intent.putExtra("userlvl", String.valueOf(user_level));
        startActivityForResult(intent, 0);
    }


//    This gets called when the timer finishes. We record the name, duration and exp of the focus period.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Checks if the request code matches
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
//              Get the parameter returned by the timer activity and update the corresponding variables
//                Add a new task to the Tasks array list
                Task task = new Task(data.getExtras().getString("task_name"), data.getExtras().getInt("completed"));
                tasks.add(task);
//                Update exp
                user_exp += data.getExtras().getInt("exp_gained");
                if (user_exp / max_exp > 1) {
                    user_level += user_exp / max_exp;
                    user_exp = user_exp % max_exp;
                }
//                Update time focused
                time_focused_min += data.getExtras().getInt("duration");
                if (time_focused_min / 60 > 1) {
                    time_focused_hrs += time_focused_min / 60;
                    time_focused_min = time_focused_min % 60;
                }

                tasks_completed += 1;
            }
        }
    }
}

