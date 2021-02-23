package com.fizha.appnotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "db_note";
    private static final String TABLE_NAME = "tbl_note";

    private static String KEY_ID = "id";
    private static String KEY_TITLE = "title";
    private static String KEY_DESC = "description";
    private static String KEY_DATE = "date";

    public DatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createNoteTable = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_TITLE + " TEXT, "
                + KEY_DESC + " TEXT, "
                + KEY_DATE + " TEXT" + ")";
        sqLiteDatabase.execSQL(createNoteTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql =("DROP TABLE IF EXISTS " + TABLE_NAME);
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }

    public void insert(NoteModel noteModel) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, noteModel.getTitle());
        values.put(KEY_DESC, noteModel.getDesc());
        values.put(KEY_DATE, noteModel.getDate());

        db.insert(TABLE_NAME, null, values);
    }

    public void update(NoteModel noteModel){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, noteModel.getTitle());
        values.put(KEY_DESC, noteModel.getDesc());
        values.put(KEY_DATE, noteModel.getDate());
        db.update(TABLE_NAME, values, KEY_ID + " = ?", new String[] { String.valueOf(noteModel.getId())});
    }
    
    public void delete(String id){
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = KEY_ID+" = '"+id+"'";
        db.delete(TABLE_NAME, whereClause, null);
    }


    public ArrayList<NoteModel> selectNoteData(){
        ArrayList<NoteModel> noteList = new ArrayList<NoteModel>();
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {KEY_ID, KEY_TITLE, KEY_DESC, KEY_DATE};
        Cursor c = db.query(TABLE_NAME, columns, null, null, null, null, null);

        if (c.moveToFirst()){
            while(c.moveToNext()){
                int id = Integer.parseInt(c.getString(0));
                String title = c.getString(1);
                String desc = c.getString(2);
                String date = c.getString(3);

                noteList.add(new NoteModel(id, title, desc, date));
            }
        }

        c.close();
        return noteList;
    }

    public NoteModel findNote(String text){
        SQLiteDatabase db = getWritableDatabase();
        NoteModel model = null;
        String query = "SELECT * FROM " + TABLE_NAME
                + " WHERE " + KEY_TITLE + " = " + text
                + " OR " + KEY_DESC + " = " + text;

        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()){
            int id = Integer.parseInt(c.getString(0));
            String title = c.getString(1);
            String desc = c.getString(2);
            String date = c.getString(3);

            model = new NoteModel(id, title, desc, date);

        }

        c.close();
        return model;
    }
}
