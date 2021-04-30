package siebelunited.cs465.illinois.edu.focus;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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

    private void renderTaskList() {
        LinearLayout task_list = (LinearLayout) getView().findViewById(R.id.task_list);

        ArrayList tasks = (ArrayList) getArguments().getSerializable("tasks");

        for (int i = 0; i < tasks.size(); i += 1) {
            CheckBox temp = new CheckBox(getView().getContext());
            final Task task = (Task)tasks.get(i);
            temp.setId(i);
            temp.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            temp.setText(((Task)tasks.get(i)).task_name);
            temp.setChecked(((Task) tasks.get(i)).is_finished == 1);
            Log.e("sbsb", String.format("i: %d, clickable: %d", i, ((Task) tasks.get(i)).is_finished));
            if (((Task) tasks.get(i)).is_finished == 1) {
                Log.e("sbsb", "here");
                temp.setEnabled(false);
            }
            final int curr_i = i;
            temp.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(task.is_finished == 0) {
                        Log.e("sbsb", "aaaa");

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("You're trying to check a task that you didn't finish during a focus period.\nAre you sure that you have completed it?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        CheckBox cb = getView().findViewById(curr_i);
                                        Log.e("sbsb", String.valueOf(curr_i));
                                        cb.setChecked(true);
                                        cb.setEnabled(false);
                                        dialogInterface.cancel();
                                        task.is_finished = 1;
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        CheckBox cb = getView().findViewById(curr_i);
                                        cb.setChecked(false);
                                        dialogInterface.cancel();
                                    }
                                });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        ((MainActivity)getActivity()).increaseTask();
                    } else {
                        Log.e("sbsb", "bbbb");
                        task.is_finished = 0;
                        ((MainActivity)getActivity()).decreaseTask();
                    }
                }
            });
            task_list.addView(temp);
        }
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
        final int tasks_completed;
        int time_focused_hrs;
        int time_focused_min;
        int max_exp = 100;

        top_banner = (TextView) view.findViewById(R.id.top_banner);
        user_info = (TextView) view.findViewById(R.id.user_info);
        num_tasks = (TextView) view.findViewById(R.id.num_tasks);
        time_focused = (TextView) view.findViewById(R.id.time_focused);
        exp_bar = (ProgressBar) view.findViewById(R.id.exp_bar);


//        Hard coded parameters
        username = getArguments().getString("username");
        user_level = getArguments().getInt("user_lvl");
        user_exp = getArguments().getInt("user_exp");
        tasks_completed = getArguments().getInt("tasks_completed");
        time_focused_hrs = getArguments().getInt("focused_hrs");
        time_focused_min = getArguments().getInt("focused_min");


//        For each task in the task list, render it onto the task list view
        renderTaskList();

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
