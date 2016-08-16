package model;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Path;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.brotherapp.preachingpartner.R;

import data.DatabaseHandler;

/**
 * Created by julian1729 on 8/15/16.
 */
public class GenericDialogFragment extends DialogFragment{

    public interface CallBack{
        public void setTitle(String title);
        public void setMessage(String message);
        public void setIcon(Path path);
        public void setPositiveButton(String text, DialogInterface.OnClickListener onClickListener);
        public void setNegativeButton(String text, DialogInterface.OnClickListener onClickListener);
    }

    CallBack callBacks;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            callBacks = (CallBack) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement ...");
        }
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        callBacks.setTitle(alert.setTitle());
        alert.setMessage("Enter a new topic");
        //alert.setView(inflater.inflate(R.layout.text_dialog, null));
        alert.setView(dialogView);
        alert.setIcon(android.R.drawable.ic_input_add);
        alert.setCancelable(true);
        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String topic = topicInput.getText().toString().trim();
                if(!topic.isEmpty()) {
                    Log.v("empty?", "no");
                    DatabaseHandler dba = new DatabaseHandler(getActivity().getApplicationContext());
                    dba.addTopic(topic);
                    dba.close();
                }else{
                    Log.v("empty?", "yes");
                    Toast.makeText(getActivity(), "No topic added", Toast.LENGTH_LONG).show();
                }
                setupDialog.callRefresh(CustomDialog.this);
            }
        });
        alert.setNegativeButton("Cancel", null);


        return alert.create();
    }

}
