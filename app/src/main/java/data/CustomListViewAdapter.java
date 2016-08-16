package data;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.brotherapp.preachingpartner.R;
import com.brotherapp.preachingpartner.TopicItemDetails;

import java.util.ArrayList;
import java.util.List;

import model.TopicItem;

/**
 * Created by julian1729 on 8/15/16.
 */
public class CustomListViewAdapter extends ArrayAdapter<TopicItem>{

    private int layoutResource;
    private Activity activity;
    private ArrayList<TopicItem> topicItemList = new ArrayList<>();


    public CustomListViewAdapter(Activity act, int resource, ArrayList<TopicItem> data) {
        super(act, resource, data);
        layoutResource = resource;
        activity = act;
        topicItemList = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return topicItemList.size();
    }

    @Override
    public TopicItem getItem(int position) {
        return topicItemList.get(position);
    }

    @Override
    public int getPosition(TopicItem item) {
        return super.getPosition(item);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;
        if(row == null || row.getTag() == null){
            LayoutInflater layoutInflater = LayoutInflater.from(activity);
            row = layoutInflater.inflate(layoutResource, null);
            holder = new ViewHolder();
            holder.scripCred = (TextView) row.findViewById(R.id.scripCred);
            holder.scriptText = (TextView) row.findViewById(R.id.scripText);
            holder.comment = (TextView) row.findViewById(R.id.comment);
            row.setTag(holder);
        }else{
            holder = (ViewHolder) row.getTag();
        }

        holder.topicItem = getItem(position);
        holder.scripCred.setText(holder.topicItem.getScripCred());
        holder.scriptText.setText(holder.topicItem.getScripText());
        holder.comment.setText(holder.topicItem.getComment());

        final ViewHolder finalHolder = holder;
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, TopicItemDetails.class);
                Bundle b = new Bundle();

                //serialize topicItem object and send to the next activity for extraction
                b.putSerializable("topicItemObject", finalHolder.topicItem);
                //send serialized object as extra through intent
                i.putExtras(b);
                //start topic items details activity
                activity.startActivity(i);
            }
        });

        return row;
    }

    public class ViewHolder{
        TopicItem topicItem;
        TextView scripCred;
        TextView scriptText;
        TextView comment;
    }

}
