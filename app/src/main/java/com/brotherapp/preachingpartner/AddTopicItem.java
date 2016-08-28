package com.brotherapp.preachingpartner;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.IOException;

import data.Constants;
import data.DatabaseHandler;
import model.TopicItem;
import utils.*;

public class AddTopicItem extends AppCompatActivity {

    AutoCompleteTextView scriptureET;
    EditText scriptureChapter, scriptureVerse, scriptureText, comment;

    //do these have to be initiated??
    String scripBook, scripChapter, scripVerse;

    TopicItem bundledTopicItem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_topic_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_18dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, Constants.BIBLE_BOOKS);
        scriptureET = (AutoCompleteTextView) findViewById(R.id.addTopicItem_scriptureET);
        scriptureET.setAdapter(arrayAdapter);
        scriptureChapter = (EditText) findViewById(R.id.addTopicItem_chapterET);
        scriptureVerse = (EditText) findViewById(R.id.addTopicItem_verseET);
        scriptureText = (EditText) findViewById(R.id.addTopicItem_scriptureTextET);
        comment = (EditText) findViewById(R.id.addTopicItem_commentET);

        scriptureET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    //this runs when this edit text loses focus
                    if (isScriptureEntered()){
                        try {
                            new RetrieveScripture().execute();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        scriptureChapter.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    //this runs when this edit text loses focus
                    if (isScriptureEntered()){
                        try {
                            new RetrieveScripture().execute();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        scriptureVerse.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    //this runs when this edit text loses focus
                    if (isScriptureEntered()){
                        try {
                            new RetrieveScripture().execute();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });


        if (getIntent().getSerializableExtra("topicItem") != null){
            bundledTopicItem = (TopicItem) getIntent().getSerializableExtra("topicItem");

            String bundledBibleBook = bundledTopicItem.getScripBook();
            String bundledBibleChapter = bundledTopicItem.getScripChapter();
            String bundledBibleVerse = bundledTopicItem.getScripVerse();
            String bundledBibleText = bundledTopicItem.getScripText();
            String bundledComment = bundledTopicItem.getComment();

            scriptureET.setText(bundledBibleBook);
            scriptureChapter.setText(bundledBibleChapter);
            scriptureVerse.setText(bundledBibleVerse);
            scriptureText.setText(bundledBibleText);
            comment.setText(bundledComment);
        }else{

        }



    }

    private String[] getScriptureInput(){
        return new String[]{scriptureET.getText().toString(), scriptureChapter.getText().toString(), scriptureVerse.getText().toString()};
    }

    private Boolean isScriptureEntered(){
        if (scriptureET.getText().toString().isEmpty() || scriptureChapter.getText().toString().isEmpty() || scriptureVerse.getText().toString().isEmpty()){
            return false;
        }else{
            return true;
        }
    }

    private Boolean regexTester(String string, String pattern){

        Pattern p = Pattern.compile(pattern);

        Matcher m = p.matcher(string);
            if (m.find()){
                return true;
            }else {
                return false;
            }
    }

    private String regexMatcher(String string, String pattern){
        String result = null;
        Pattern p = Pattern.compile(pattern);

        Matcher m = p.matcher(string);
            if (m.find()){
                result = m.group();
                return result;
            }else {
                return null;
            }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_topic_item_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (bundledTopicItem == null) {
                    String bibleBook = scriptureET.getText().toString().trim();
                    String bibleChapter = scriptureChapter.getText().toString().trim();
                    String bibleVerse = scriptureVerse.getText().toString().trim();
                    String bibleText = scriptureText.getText().toString().trim();
                    String userComment = comment.getText().toString().trim();

                    TopicItem topicItem = new TopicItem();
                    topicItem.setScripBook(bibleBook);
                    topicItem.setScripChapter(bibleChapter);
                    topicItem.setScripVerse(bibleVerse);
                    topicItem.setScripText(bibleText);
                    topicItem.setComment(userComment);
                    topicItem.setTopic(getIntent().getStringExtra("topic"));

                    Bundle b = getIntent().getExtras();

                    topicItem.setTopic(b.getString("topic"));

                    DatabaseHandler dba = new DatabaseHandler(getApplicationContext());
                    dba.addRow(topicItem);
                }else{
                    if (bundledTopicItem.getScripBook() != scriptureET.getText().toString() ||
                            bundledTopicItem.getScripChapter() != scriptureChapter.getText().toString() ||
                            bundledTopicItem.getScripVerse() != scriptureVerse.getText().toString() ||
                            bundledTopicItem.getComment() != comment.getText().toString()){
                        TopicItem newItem = new TopicItem();
                        newItem.setScripBook(scriptureET.getText().toString());
                        newItem.setScripChapter(scriptureChapter.getText().toString());
                        newItem.setScripVerse(scriptureVerse.getText().toString());
                        newItem.setScripText(scriptureText.getText().toString());
                        newItem.setComment(comment.getText().toString());
                        newItem.setTopic(bundledTopicItem.getTopic());

                        DatabaseHandler dba = new DatabaseHandler(getApplicationContext());
                        dba.deleteRow(bundledTopicItem.getTopic(), bundledTopicItem.getItemId());
                        dba.addRow(newItem);
                    }
                }
                finish();
                return true;
            }
        });

        return false;
    }

    private class RetrieveScripture extends AsyncTask<String, Void, String>{

        String finalText;
        String rawScripture;
        Elements selector;

        String bibleBook;
        String bibleChapter;
        String bibleVerse;

        @Override
        protected String doInBackground(String... params) {
            String[] scriptureInput = getScriptureInput();
            bibleBook = scriptureInput[0].trim();
            bibleChapter = scriptureInput[1].trim();
            bibleVerse = scriptureInput[2].trim();

            String scriptureURL = null;

            if (regexTester(bibleBook, "\\dst") || regexTester(bibleBook, "\\dnd")) {
                return null;
            }else {
                if (bibleVerse.contains(",")) {
                    String[] verses = bibleVerse.split(",");
                    scriptureURL = Constants.WOL_SEARCH_SCRIPTURE_URL
                            + bibleBook
                            + "+"
                            + bibleChapter
                            + "%3A"
                            + verses[0].trim()
                            + "%2c"
                            + verses[1].trim();
                }  else {
                    scriptureURL = Constants.WOL_SEARCH_SCRIPTURE_URL
                            + bibleBook
                            + "+"
                            + bibleChapter
                            + "%3A"
                            + bibleVerse;
                }
            }

            Document doc;
            try {
                doc = Jsoup.connect(scriptureURL).get();
                selector = doc.select(".bibleCitation");

            } catch (Exception e) {
                e.printStackTrace();
            }

            if (!selector.isEmpty()) {
                rawScripture = selector.text();
            }else{
                return null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (rawScripture != null) {
                scriptureText.setText(regexMatcher(rawScripture, "^.*[^+]+"));
            }else if (regexTester(bibleBook, "\\dst") || regexTester(bibleBook, "\\dnd")) {
                Toast.makeText(getApplicationContext(), "Please no 'st' or 'nd' on Bible Book", Toast.LENGTH_LONG).show();
            }else if (rawScripture == null){
                Toast.makeText(getApplicationContext(), Utils.appendScripture(bibleBook, bibleChapter, bibleVerse) + " is not a valid scripture.", Toast.LENGTH_LONG).show();
            }
        }
    }

}

