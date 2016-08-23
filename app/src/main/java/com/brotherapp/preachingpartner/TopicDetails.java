package com.brotherapp.preachingpartner;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
//import android.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.zip.Inflater;

import data.CustomListViewAdapter;
import data.DatabaseHandler;
import model.CustomDialog;
import model.GenericDialogFragment;
import model.TopicItem;

public class TopicDetails extends AppCompatActivity implements GenericDialogFragment.CallBack, CustomListViewAdapter.ListViewCallback, PopupMenu.OnMenuItemClickListener{

    private TextView topicTitle;
    private EditText topicRename;
    private ListView topicItemsLV;
    private ViewSwitcher viewSwitcher;

    private DatabaseHandler dba;
    private ArrayList<TopicItem> dbTopicItems = new ArrayList<>();
    private CustomListViewAdapter customListViewAdapter;
    private String topicString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_18dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        topicTitle = (TextView) findViewById(R.id.topicTitle);
        topicRename = (EditText) findViewById(R.id.renameTopicTitle);
        topicItemsLV = (ListView) findViewById(R.id.topicItemsLV);
        viewSwitcher = (ViewSwitcher) findViewById(R.id.topicTitleVS);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addTopicItemFAB);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //send user to new topic item activity
                Intent i = new Intent(getApplicationContext(), AddTopicItem.class);
                i.putExtra("topic", topicString);
                startActivity(i);

            }
        });

        //get extras from the intent
        Bundle extras = getIntent().getExtras();
        topicString = extras.getString("topic");
        //set textview to topic title
        topicTitle.setText(topicString);
        refreshData();

        topicTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                renameTopic();
                return true;
            }
        });
    }

    private void refreshData(){
        TopicItem holderItem;

        dbTopicItems.clear();
        dba = new DatabaseHandler(getApplicationContext());
        ArrayList<TopicItem> topicItemsFromDb = dba.getTopicItems(topicString);

        for (int i=0;i<topicItemsFromDb.size();i++){

            String scripBook = topicItemsFromDb.get(i).getScripBook();
            String scripChapter = topicItemsFromDb.get(i).getScripChapter();
            String scripVerse = topicItemsFromDb.get(i).getScripVerse();
            String scripText = topicItemsFromDb.get(i).getScripText();
            String comment = topicItemsFromDb.get(i).getComment();
            int itemId = topicItemsFromDb.get(i).getItemId();

            holderItem = new TopicItem();

            holderItem.setScripBook(scripBook);
            holderItem.setScripChapter(scripChapter);
            holderItem.setScripVerse(scripVerse);
            holderItem.setScripText(scripText);
            holderItem.setComment(comment);
            holderItem.setItemId(itemId);
            dbTopicItems.add(holderItem);
        }

        dba.close();

        //setup adapter
        customListViewAdapter = new CustomListViewAdapter(TopicDetails.this, R.layout.topic_details_row, dbTopicItems);
        topicItemsLV.setAdapter(customListViewAdapter);
        customListViewAdapter.notifyDataSetChanged();
    }

    private void renameTopic(){
        topicRename.setText(topicString);
        viewSwitcher.showNext();
        viewSwitcher.requestFocus();

        //the following two lines gets the systems softinputmethod and shows the keyboard
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        //!!!!!!!!need to update in database and set the textview to new topic
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.topic_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){

            case R.id.renameTopicDetailsOption:
                renameTopic();
                return true;

            case R.id.deleteTopicDetailsOption:
                DialogFragment confirmTopicDelete = new GenericDialogFragment();
                confirmTopicDelete.show(getSupportFragmentManager(), "confirmDeleteDialog");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }

    @Override
    public String setDialogMessage() {
        String space = " ";
        return getResources().getString(R.string.confirm_delte_dialog_message1) + space + topicString  + space + getResources().getString(R.string.confirm_delete_dialog_message2);
    }

    @Override
    public int setDialogIcon() {
        return android.R.drawable.ic_delete;
    }

    @Override
    public String setTitle() {
        return getResources().getString(R.string.confirm_delete_dialog_title);
    }

    @Override
    public String setPositiveBtnText() {
        return getResources().getString(R.string.dialog_positive_delete);
    }

    @Override
    public String setNegativeBtnText() {
        return getString(R.string.dialog_negative);
    }

    @Override
    public DialogInterface.OnClickListener setPositiveButton() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //make a snackbar that allows an undo, when dismissed, execute deletion
                        dba = new DatabaseHandler(getApplicationContext());
                        dba.deleteTopic(new String[]{topicString});
                        finish();
            }
        };
    }

    @Override
    public DialogInterface.OnClickListener setNegativeButton() {
        return null;
    }

    public void showMenu(){
        PopupMenu popupMenu = new PopupMenu(this, );
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.topic_item_row_popupmenu);
        popupMenu.show();
    }

    @Override
    public void setMenuButton(int itemId) {

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {


        return false;
    }
}

