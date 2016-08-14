package model;

import android.app.Activity;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.brotherapp.preachingpartner.MainActivity;
import com.brotherapp.preachingpartner.R;

import data.DatabaseHandler;

/**
 * Created by julian1729 on 7/27/16.
 */
public class CustomDialog extends DialogFragment {

    public String topic;

    public interface SetupDialog{
        //public void setOnOnDismissListener(DialogFragment dialogFragment);
        public void callRefresh(DialogFragment dialogFragment);
    }

    SetupDialog setupDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            setupDialog = (SetupDialog) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement CustomDialog.SetupDialog");
        }
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.text_dialog, null);
        final EditText topicInput = (EditText) dialogView.findViewById(R.id.dialogET);

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Add New Topic");
        alert.setMessage("Enter a new topic");
        //alert.setView(inflater.inflate(R.layout.text_dialog, null));
        alert.setView(dialogView);

        alert.setIcon(android.R.drawable.ic_input_add);
        alert.setCancelable(true);
        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseHandler dba = new DatabaseHandler(getActivity().getApplicationContext());
                String topic = topicInput.getText().toString().trim();
                dba.addTopic(topic);
                //Log.v("Topics", dba.getTopics().get(1).toString());
                setupDialog.callRefresh(CustomDialog.this);

            }
        });
        alert.setNegativeButton("Cancel", null);


        return alert.create();
    }



}
