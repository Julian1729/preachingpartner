package com.brotherapp.preachingpartner;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.Toolbar;
//import android.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.zip.Inflater;

import data.CustomListViewAdapter;
import data.DatabaseHandler;
import model.CustomDialog;
import model.TopicItem;

public class TopicDetails extends AppCompatActivity{

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



        topicTitle = (TextView) findViewById(R.id.topicTitle);
        topicRename = (EditText) findViewById(R.id.renameTopicTitle);
        topicItemsLV = (ListView) findViewById(R.id.topicItemsLV);
        viewSwitcher = (ViewSwitcher) findViewById(R.id.topicTitleVS);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addTopicItemFAB);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //send user to new topic item activity
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

            String scripCred = topicItemsFromDb.get(i).getScripCred();
            String scripText = topicItemsFromDb.get(i).getScripText();
            String comment = topicItemsFromDb.get(i).getComment();
            int itemId = topicItemsFromDb.get(i).getItemId();

            Log.v("food id of " + String.valueOf(i), String.valueOf(itemId));

            holderItem = new TopicItem();

            holderItem.setScripCred(scripCred);
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
                AlertDialog.Builder confirmDelete = new AlertDialog.Builder(getApplicationContext());
                confirmDelete.setTitle("Are you sure?");
                confirmDelete.setMessage("Are you sure you would like to delete " + topicString
                        + " and all of its contents?");
                confirmDelete.setIcon(android.R.drawable.ic_input_delete);
                confirmDelete.setCancelable(true);
                confirmDelete.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //make a snackbar that allows an undo, when dismissed, execute deletion
                        dba = new DatabaseHandler(getApplicationContext());
                        dba.deleteTopic(new String[]{topicString});
                        finish();
                    }
                });
                confirmDelete.setNegativeButton("Cancel", null);
                confirmDelete.create();
                //fix this crap

                confirmDelete.show();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}

