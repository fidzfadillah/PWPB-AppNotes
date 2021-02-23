package com.fizha.appnotes;

public class NoteModel {
    int id;
    String title;
    String desc;
    String date;

    public NoteModel(int id, String title, String desc, String date) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.date = date;
    }

    public NoteModel(String title, String desc, String date) {
        this.title = title;
        this.desc = desc;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
