package siebelunited.cs465.illinois.edu.focus;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ExpGainDialog extends AppCompatDialogFragment {
    private ExpGainDialogListener listener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.exp_gain_dialog, null);
        int duration = getArguments().getInt("duration", 0);

        TextView exp_gained = view.findViewById(R.id.exp_gained);
        exp_gained.setText(String.format("+%d EXP", duration));

        builder.setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.feedback(getArguments().getString("task_name"), getArguments().getInt("duration", 0), 1, getArguments().getInt("duration", 0));
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (ExpGainDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException();
        }
    }

    public interface ExpGainDialogListener {
        void feedback(String task_name, int duration_min, int completed, int exp_gain);
    }
}
