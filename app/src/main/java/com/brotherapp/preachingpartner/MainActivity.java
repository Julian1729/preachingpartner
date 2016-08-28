package com.brotherapp.preachingpartner;

import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import data.DatabaseHandler;
import model.CustomDialog;

public class MainActivity extends AppCompatActivity implements CustomDialog.SetupDialog{

    private DatabaseHandler dba;

    private ListView topicLV;
    private ArrayList<String> dbTopics = new ArrayList<>();
    private TopicAdapter topicAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addTopicFAB);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialogFragment = new CustomDialog();
                dialogFragment.show(getSupportFragmentManager(), "newtopic");
            }
        });

        topicLV = (ListView) findViewById(R.id.topicsLV);
        refreshData();
    }


    public void refreshData(){
        dbTopics.clear();

        dba = new DatabaseHandler(getApplicationContext());

        ArrayList<String> topicsFromDB = dba.getTopics();

        for(int i=0;i<topicsFromDB.size();i++){
            String topic = topicsFromDB.get(i);
            dbTopics.add(topic);
        }
        dba.close();

        //setup adapter
        topicAdapter = new TopicAdapter(MainActivity.this, R.layout.topic_row, dbTopics);
        topicLV.setAdapter(topicAdapter);
        topicAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                //this function is called when the text is submitted
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //this function is called everytime a new character is typed
                return true;
            }
        });


        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }

    @Override
    public void callRefresh(DialogFragment dialogFragment) {
        refreshData();
    }
}

class TopicAdapter extends ArrayAdapter<String>{

    Context context;
    int layoutResource;
    String topic;
    ArrayList<String> topicList = new ArrayList<>();

    public TopicAdapter(Context con, int resource, ArrayList<String> data) {
        super(con, resource, data);
        context = con;
        layoutResource = resource;
        topicList = data;

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return topicList.size();
    }

    @Override
    public String getItem(int position) {
        return topicList.get(position);
    }

    @Override
    public int getPosition(String item) {
        return super.getPosition(item);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder viewHolder = null;

        if(row == null || row.getTag() == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            row = inflater.inflate(layoutResource, null);
            viewHolder = new ViewHolder();
            viewHolder.topicTV = (TextView) row.findViewById(R.id.topicsLV_TV);
            row.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) row.getTag();
        }
        viewHolder.topic = getItem(position);
        viewHolder.topicTV.setText(viewHolder.topic);

        final ViewHolder finalHolder = viewHolder;
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String topicTitle = finalHolder.topic;
                int id = finalHolder.topicId;

                Log.v("string id", String.valueOf(finalHolder.topicId));

                Intent i = new Intent(getContext(), TopicDetails.class);
                i.putExtra("topic", topicTitle);
                context.startActivity(i);
            }
        });

        return row;

    }
}

class ViewHolder{
    String topic;
    TextView topicTV;
    int topicId;
}