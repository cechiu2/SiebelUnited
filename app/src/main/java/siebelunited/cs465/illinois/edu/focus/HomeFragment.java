package siebelunited.cs465.illinois.edu.focus;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.io.Serializable;
import java.util.ArrayList;

public class HomeFragment extends Fragment{

    @Nullable
    @Override


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
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
        int max_exp = 100;

        top_banner = (TextView) view.findViewById(R.id.top_banner);
        user_info = (TextView) view.findViewById(R.id.user_info);
        num_tasks = (TextView) view.findViewById(R.id.num_tasks);
        time_focused = (TextView) view.findViewById(R.id.time_focused);
        exp_bar = (ProgressBar) view.findViewById(R.id.exp_bar);
        task_list = (LinearLayout) view.findViewById(R.id.task_list);

//        Hard coded parameters
        username = getArguments().getString("username");
        user_level = getArguments().getInt("user_lvl");
        user_exp = getArguments().getInt("user_exp");
        tasks_completed = getArguments().getInt("tasks_completed");
        time_focused_hrs = getArguments().getInt("focused_hrs");
        time_focused_min = getArguments().getInt("focused_min");
        ArrayList tasks = (ArrayList) getArguments().getSerializable("tasks");


//        For each task in the task list, render it onto the task list view
        for (int i = 0; i < tasks.size(); i += 1) {
            CheckBox temp = new CheckBox(view.getContext());
            temp.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            temp.setText(((Task)tasks.get(i)).task_name);
            temp.setChecked(((Task) tasks.get(i)).is_finished == 1);
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


//        Bar chart code
//        Bar chart resources found at https://medium.com/@karthikganiga007/create-barchart-in-android-studio-14943339a211
        BarChart barChart = (BarChart) view.findViewById(R.id.barchart);
//        barChart.
//        Hard coded data
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(4f, 0));
        entries.add(new BarEntry(3f, 1));
        entries.add(new BarEntry(5f, 2));
        entries.add(new BarEntry(7f, 3));
        entries.add(new BarEntry(8f, 4));
        entries.add(new BarEntry(6f, 5));
        entries.add(new BarEntry((float) tasks_completed, 6));

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
        data.setValueTextSize(16);
        barChart.getXAxis().setTextSize(16);
        barChart.getLegend().setTextSize(16);
        barChart.setDescription("Record for the past 7 days");
        barChart.setDescriptionTextSize(14);
        barChart.setData(data); // set the data and list of labels into chart
        barChart.animateY(3000);
    }
}
