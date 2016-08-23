package model;

import java.io.Serializable;

/**
 * Created by julian1729 on 7/27/16.
 */
public class TopicItem implements Serializable{

    public String topic;
    public String scripBook;
    public String scripChapter;
    public String scripVerse;
    public String scripText;
    public String comment;
    public int itemId;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getScripBook() {
        return scripBook;
    }

    public void setScripBook(String scripBook) {
        this.scripBook = scripBook;
    }

    public String getScripChapter() {
        return scripChapter;
    }

    public void setScripChapter(String scripChapter) {
        this.scripChapter = scripChapter;
    }

    public String getScripVerse() {
        return scripVerse;
    }

    public void setScripVerse(String scripVerse) {
        this.scripVerse = scripVerse;
    }

    public String getScripText() {
        return scripText;
    }

    public void setScripText(String scripText) {
        this.scripText = scripText;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
}
