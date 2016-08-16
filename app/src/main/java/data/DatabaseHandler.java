package data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import model.TopicItem;

/**
 * Created by julian1729 on 7/27/16.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private String createTableConclusion = " ("
            + Constants.ID_COLUMN + " INTEGER PRIMARY KEY, "
            + Constants.SCRIPTURE_CRED + " TEXT, "
            + Constants.SCRIPTURE_TEXT + " TEXT, "
            + Constants.COMMENT_COLUMN + " TEXT);";

    public DatabaseHandler(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_GENERIC_TABLE = "CREATE TABLE " + Constants.GENERIC_TABLE_NAME + " ("
                + Constants.ID_COLUMN + " INTEGER PRIMARY KEY, "
                + Constants.SCRIPTURE_CRED + " TEXT, "
                + Constants.SCRIPTURE_TEXT + " TEXT, "
                + Constants.COMMENT_COLUMN + " TEXT);";
        sqLiteDatabase.execSQL(CREATE_GENERIC_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Constants.GENERIC_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void deleteTopic(String[] topic){
        SQLiteDatabase db = getWritableDatabase();
        //does this have to be equal to zero?
        Log.v("delete topic array", "lenght = " + topic.length);
        if (topic.length == 1){
            db.execSQL("DROP TABLE IF EXISTS " + "\"" + topic[0] + "\"");
            Log.v("one table", "DELETED!!!!");
            db.close();
        }else{
            StringBuilder sb = new StringBuilder();
            sb.append("DROP TABLE IF EXISTS ");
            for(int i=0;i<topic.length;i++){
                if ((i + 1) == topic.length) {
                    sb.append("\"" + topic[i] + "\";");
                }else{
                    sb.append("\""+ topic[i] + "\",");
                }
                db.execSQL(sb.toString());
                Log.v("multiple tables", "DELETED!!!!");
                db.close();
            }
        }
    }

    public void deleteRow(String table, int id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(table, Constants.ID_COLUMN + " =? ", new String[]{String.valueOf(id)});
        db.close();
    }

    public void addTopic(String topic){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("CREATE TABLE " + "\"" + topic + "\"" + createTableConclusion);

        Log.v("Table created", "true");
        db.close();
    }

    public void addRow(TopicItem topicItem){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(Constants.SCRIPTURE_CRED, topicItem.getScripCred());
        cv.put(Constants.SCRIPTURE_TEXT, topicItem.getScripText());
        cv.put(Constants.COMMENT_COLUMN, topicItem.getComment());

        db.insert(topicItem.getTopic(), null, cv);

        Log.v("added topic row to db", "yesssss");
        db.close();
    }

    public ArrayList<String> getTopics(){
        ArrayList<String> topics = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' ORDER BY NAME", null);
        if (c.moveToFirst()){
            do{
                String topic = c.getString(c.getColumnIndex("name"));
                //Log.v("topic from dba", topic);
                if (!topic.equals("android_metadata")) {
                    topics.add(topic);
                }
            }while(c.moveToNext());
        }else{Log.v("FROM DBA", "found no tables");}
        db.close();

        return topics;
    }

    public ArrayList<TopicItem> getTopicItems(String topic){
        ArrayList<TopicItem> topicItems = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String preparedTopicQuery = "\"" + topic + "\"";

        Cursor c = db.query(preparedTopicQuery, new String[]{Constants.ID_COLUMN,
                Constants.SCRIPTURE_CRED,
                Constants.SCRIPTURE_TEXT,
                Constants.COMMENT_COLUMN}, null, null,null,null, Constants.ID_COLUMN + " DESC");

        if (c.moveToFirst()){
            do {
                TopicItem tI = new TopicItem();
                tI.setItemId(c.getInt(c.getColumnIndex(Constants.ID_COLUMN)));
                tI.setScripCred(c.getString(c.getColumnIndex(Constants.SCRIPTURE_CRED)));
                tI.setScripText(c.getString(c.getColumnIndex(Constants.SCRIPTURE_TEXT)));
                tI.setComment(c.getString(c.getColumnIndex(Constants.COMMENT_COLUMN)));
                topicItems.add(tI);
            }while(c.moveToNext());
        }
        Log.v("got topics", "yessssss");

        db.close();
        return topicItems;
    }

    public void renameTopic(String oldTopic, String newTopic){
        SQLiteDatabase db = getWritableDatabase();

        String preparedOldTopic = "\"" + oldTopic + "\"";
        String preparedNewTopic = "\"" + newTopic + "\"";

        db.execSQL("ALTER TABLE " + preparedOldTopic + " RENAME TO " + newTopic);
    }

}
