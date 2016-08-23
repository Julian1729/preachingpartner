package model;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.brotherapp.preachingpartner.R;

import data.DatabaseHandler;

/**
 * Created by julian1729 on 8/15/16.
 */
public class GenericDialogFragment extends DialogFragment{

    public interface CallBack{
        public String setDialogMessage();
        public int setDialogIcon();
        public String setTitle();
        public String setPositiveBtnText();
        public String setNegativeBtnText();
        public DialogInterface.OnClickListener setPositiveButton();
        public DialogInterface.OnClickListener setNegativeButton();
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
        //alert.setView(inflater.inflate(R.layout.text_dialog, null));
        alert.setTitle(callBacks.setTitle());
        alert.setMessage(callBacks.setDialogMessage());
        alert.setIcon(callBacks.setDialogIcon());
        alert.setCancelable(true);
        alert.setPositiveButton(callBacks.setPositiveBtnText(), callBacks.setPositiveButton());
        alert.setNegativeButton(callBacks.setNegativeBtnText(), callBacks.setNegativeButton());

        return alert.create();
    }

}
