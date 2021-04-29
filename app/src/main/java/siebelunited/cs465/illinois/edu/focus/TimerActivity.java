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
import android.widget.Button;
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

    ImageButton play, next;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        play = (ImageButton) findViewById(R.id.musicpauseorplay);
        // next = (ImageButton) findViewById(R.id.musicchoice); // replace this with the next button

		mp = MediaPlayer.create(this, R.raw.beautifulhope);
		play.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mp.isPlaying()) {
					mp.pause();
				}
				else {
					mp.start();
				}
			}
		});

//        Read parameter passed in by the main activity
        TextView task_description = findViewById(R.id.task_description);
        task_description.setText(getIntent().getExtras().getString("task_name"));

        duration_mins = getIntent().getExtras().getInt("duration");
        startTimer((long) (duration_mins * 60 * 1000));

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
                .setNegativeButton("Return to Focus Period", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
		mp.seekTo(0);
		if (mp.isPlaying()) {
			mp.pause();
		}
    }

    public void finishEarly(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure that you have finished your task and wish to leave the focus period?\nYour progress will be saved.")
                .setCancelable(false)
                .setPositiveButton("Finish Early", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ExpGainDialog expGainDialog = new ExpGainDialog();
                        Bundle bundle = new Bundle();
                        bundle.putInt("duration", (int) (duration_mins - (time_left_mili / (1000 * 60))));
                        bundle.putString("task_name", task_name);
                        expGainDialog.setArguments(bundle);
                        expGainDialog.show(getSupportFragmentManager(), "exp_gain");
                        mp.seekTo(0);
                        if (mp.isPlaying()) {
                            mp.pause();
                        }
                    }
                })
                .setNegativeButton("Return to Focus Period", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        mp.seekTo(0);
        if (mp.isPlaying()) {
            mp.pause();
        }
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
                mp.seekTo(0);
				if (mp.isPlaying()) {
					mp.pause();
				}
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
        mp.seekTo(0);
		if (mp.isPlaying()) {
			mp.pause();
		}
        finish();
    }
}
