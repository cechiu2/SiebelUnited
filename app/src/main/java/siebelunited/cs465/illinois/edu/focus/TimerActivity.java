package siebelunited.cs465.illinois.edu.focus;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
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

public class TimerActivity extends AppCompatActivity implements ExpGainDialog.ExpGainDialogListener {
    private CountDownTimer timer;
    private long time_left_mili;
    MediaPlayer mediaPlayer;
    int duration_mins;
    String task_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

//        Read parameter passed in by the main activity
        TextView lvl = findViewById(R.id.level);
        lvl.setText(String.format("Lv. %s", getIntent().getExtras().getString("userlvl")));
        TextView username = findViewById(R.id.name);
        username.setText(getIntent().getExtras().getString("username"));

        duration_mins = getIntent().getExtras().getInt("duration");
        startTimer((long) (duration_mins * 60 * 1000));

        task_name = getIntent().getExtras().getString("task_name");

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.beautifulhope);
    }


    public void exitFocus(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure to quit? \n Giving up a focusing task will be recorded in your data.")
                .setCancelable(false)
                .setPositiveButton("Give Up", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void startTimer(final long duration) {
        time_left_mili = duration;
        timer = new CountDownTimer(time_left_mili, 1000) {
            @Override
            public void onTick(long l) {
                time_left_mili = l;
                updateCountdown();
            }

            @Override
            public void onFinish() {
                ExpGainDialog expGainDialog = new ExpGainDialog();
                Bundle bundle = new Bundle();
                bundle.putInt("duration", duration_mins);
                bundle.putString("task_name", task_name);
                expGainDialog.setArguments(bundle);
                expGainDialog.show(getSupportFragmentManager(), "exp_gain");
            }
        }.start();
    }

    private void updateCountdown() {
        long duration_mili = getIntent().getExtras().getInt("duration") * 60 * 1000;

        TextView time_left = findViewById(R.id.timeleft);
        time_left.setText(String.valueOf((time_left_mili / 1000) / 60));
        ProgressBar pgbar = findViewById(R.id.progresscircle);
        pgbar.setProgress((int) ((double) (time_left_mili) * 100 / (double) (duration_mili)));
    }

    private void startPlaying() {

    }

    @Override
    public void feedback(String task_name, int duration_mins, int completed, int exp_gain) {
        Intent intent = new Intent();
        intent.putExtra("task_name", task_name);
        intent.putExtra("completed", completed);
        intent.putExtra("exp_gain", exp_gain);
        intent.putExtra("duration", duration_mins);
        setResult(RESULT_OK, intent);
        finish();
    }
}