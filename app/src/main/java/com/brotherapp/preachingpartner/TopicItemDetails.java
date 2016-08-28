package com.brotherapp.preachingpartner;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

import data.DatabaseHandler;
import model.TopicItem;
import utils.Utils;

public class TopicItemDetails extends AppCompatActivity {

    TopicItem topicItem;

    TextView scriptureTV, scriptureTextTV, comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_item_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        topicItem = (TopicItem) getIntent().getSerializableExtra("topicItemObject");

        scriptureTV = (TextView) findViewById(R.id.scriptureTV_ItemTopicDetails);
        scriptureTextTV = (TextView) findViewById(R.id.scriptureTextTV_ItemTopicDetails);
        comment = (TextView) findViewById(R.id.commentTV_ItemTopicDetails);

        setTitle(topicItem.getTopic());
        scriptureTV.setText(Utils.appendScripture(topicItem.getScripBook(), topicItem.getScripChapter(), topicItem.getScripVerse()));
        scriptureTextTV.setText(topicItem.getScripText());
        comment.setText(topicItem.getComment());

    }

    //update the details view and remove the finish when edit is called
    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.topic_item_details_menu_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_ItemTopicDetails:
                finish();
                Intent i = new Intent(TopicItemDetails.this, AddTopicItem.class);
                Bundle b = new Bundle();
                b.putSerializable("topicItem", topicItem);
                i.putExtras(b);
                startActivity(i);
                return true;
            case R.id.delete_ItemTopicDetails:
                DatabaseHandler dba = new DatabaseHandler(getApplicationContext());
                dba.deleteRow(topicItem.getTopic(), topicItem.getItemId());
                dba.close();
                finish();
                return true;
        }
        return false;
    }
}
