package model;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.brotherapp.preachingpartner.R;

/**
 * Created by julian1729 on 8/27/16.
 */
public class ConfirmDeleteOfItem extends DialogFragment {

    public interface Setter{
        public void setPositive(DialogInterface.OnClickListener onClickListener);
    }

    Setter setter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            setter = (Setter) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement ...");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        //alert.setView(inflater.inflate(R.layout.text_dialog, null));
        alert.setTitle(getResources().getString(R.string.confirmDelete));
        alert.setMessage(getResources().getString(R.string.confirm_delte_dialog_message1)
                + " this item " + getResources().getString(R.string.confirm_delete_dialog_message2));
        alert.setIcon(R.drawable.ic_delete_black_24dp);
        alert.setCancelable(true);
        alert.setPositiveButton(getResources().getString(R.string.dialog_positive_delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setter.setPositive(this);
            }
        });
        alert.setNegativeButton(getResources().getString(R.string.dialog_negative), null);

        return alert.create();
    }
}
