package siebelunited.cs465.illinois.edu.focus;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import siebelunited.cs465.illinois.edu.focus.Task.*;


public class MainActivity extends AppCompatActivity {

    LinearLayout todo_tab, stat_tab, todo_body, stat_body, task_list;
    ImageButton todo_button, stat_button;
    TextView top_banner, user_info, num_tasks, time_focused;
    ProgressBar exp_bar;

    String username;
    int user_level;
    int user_exp;
    int tasks_completed;
    int time_focused_hrs;
    int time_focused_min;
    private static final int max_exp = 100;
    ArrayList<Task> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        todo_tab = (LinearLayout) findViewById(R.id.todo_tab);
        stat_tab = (LinearLayout) findViewById(R.id.stat_tab);
        todo_button = (ImageButton) findViewById(R.id.todo_button);
        stat_button = (ImageButton) findViewById(R.id.stat_button);
        todo_body = (LinearLayout) findViewById(R.id.todo_body);
        stat_body = (LinearLayout) findViewById(R.id.stat_body);
        top_banner = (TextView) findViewById(R.id.top_banner);
        user_info = (TextView) findViewById(R.id.user_info);
        num_tasks = (TextView) findViewById(R.id.num_tasks);
        time_focused = (TextView) findViewById(R.id.time_focused);
        exp_bar = (ProgressBar) findViewById(R.id.exp_bar);
        task_list = (LinearLayout) findViewById(R.id.task_list);

//        Hard coded parameters
        username = "Youhei";
        user_level = 10;
        user_exp = 42;
        tasks_completed = 10;
        time_focused_hrs = 5;
        time_focused_min = 30;
        tasks = new ArrayList<>();
        tasks.add(new Task("CS465 HW", false));
        tasks.add(new Task("100 pushups", true));
        tasks.add(new Task("breath", true));

        for (int i = 0; i < tasks.size(); i += 1) {
            CheckBox temp = new CheckBox(this);
            temp.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            temp.setText(tasks.get(i).task_name);
            temp.setChecked(tasks.get(i).is_finished);
//            temp.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View v) {
//
//                }
//            });
            task_list.addView(temp);
        }
        
        top_banner.setText(String.format("Hi %s, maybe start a new focusing after 10 mins?", username));
        user_info.setText(String.format("Lv. %d %s", user_level, username));
        num_tasks.setText(String.format("Completed %d tasks today!", tasks_completed));
        time_focused.setText(String.format("%d hrs %d min", time_focused_hrs, time_focused_min));
        exp_bar.setProgress(user_exp);




//        Bar chart resources found at https://medium.com/@karthikganiga007/create-barchart-in-android-studio-14943339a211
        BarChart barChart = (BarChart) findViewById(R.id.barchart);
//        Hard coded data
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(4f, 0));
        entries.add(new BarEntry(3f, 1));
        entries.add(new BarEntry(5f, 2));
        entries.add(new BarEntry(7f, 3));
        entries.add(new BarEntry(8f, 4));
        entries.add(new BarEntry(6f, 5));
        entries.add(new BarEntry(5f, 5));

        BarDataSet bardataset = new BarDataSet(entries, "Days");

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("S");
        labels.add("M");
        labels.add("T");
        labels.add("W");
        labels.add("U");
        labels.add("F");
        labels.add("S");

        BarData data = new BarData(labels, bardataset);
        barChart.setData(data); // set the data and list of labels into chart
        barChart.setDescription("Your Record");  // set the description
        barChart.animateY(5000);
    }

    public void list_onClick(View v) {
        todo_tab.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.layout_selected));
        stat_tab.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.layout_unselected));
        todo_button.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.list_icon_selected));
        stat_button.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bar_icon_unselected));
        todo_body.setVisibility(View.VISIBLE);
        stat_body.setVisibility(View.GONE);

    }

    public void stat_onClick(View v) {
        stat_tab.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.layout_selected));
        todo_tab.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.layout_unselected));
        todo_button.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.list_icon_unselected));
        stat_button.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bar_icon_selected));
        todo_body.setVisibility(View.GONE);
        stat_body.setVisibility(View.VISIBLE);
    }
}
