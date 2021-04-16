package siebelunited.cs465.illinois.edu.focus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        Bar chart resources found at https://medium.com/@karthikganiga007/create-barchart-in-android-studio-14943339a211
        BarChart barChart = (BarChart) findViewById(R.id.barchart);

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
}
