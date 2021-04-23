package siebelunited.cs465.illinois.edu.focus;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;


public class SelectTaskDialog extends AppCompatDialogFragment {
    private SelectTaskDialogListener listener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

//        The following 3 lines of code creates a popup window and renders a layout onto it. I don't really know how.
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.popup_dialog, null);

//        A spinner is a drop down menu.
        Spinner spinner = view.findViewById(R.id.history);
//        I copied the following 3 lines from the documentation of android studio.
//        Also, task_history is a string array in string.xml. We can modify it manually.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.task_history, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

//        This is declared as an array because android says that this variable must be a final variable,
//        so if want to change it we might have to declare it as an array.
        final String[] task_selected = {""};

//        Spinner listener.
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Triggers whenever you select an element from the spinner.
                task_selected[0] = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


//        Edit the popup window.
        builder.setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Call the callback function to send the task back to the main activity.
                        listener.selectTask(task_selected[0]);
                    }
                })
                .setNeutralButton("Add New", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        If we click Add New, we create an Add Task dialog.
                        AddTaskDialog addTaskDialog = new AddTaskDialog();
                        dialogInterface.cancel();
                        addTaskDialog.show(getActivity().getSupportFragmentManager(), "add_task");
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.cancel();
                    }
                });
        return builder.create();
    }

//    The following code is really wonky and I don't know why it works, I just followed some tutorial online
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (SelectTaskDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException();
        }
    }

    public interface SelectTaskDialogListener {
        void selectTask(String task_selected);
    }
}
