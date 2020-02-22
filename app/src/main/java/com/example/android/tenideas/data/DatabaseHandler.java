package com.example.android.tenideas.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.android.tenideas.model.DateEntry;
import com.example.android.tenideas.model.Idea;
import com.example.android.tenideas.util.Constants;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String DB_ADD = "ADD";

    public DatabaseHandler(@Nullable Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Define database schema
        String ITEM_TABLE = "CREATE TABLE " + Constants.TABLE_NAME_IDEA + "("
                + Constants.KEY_IDEA_ID + " INTEGER PRIMARY KEY,"
                + Constants.KEY_IDEA + " TEXT,"
                + Constants.KEY_IS_FAVOURITE + " BOOLEAN,"
                + Constants.KEY_DATE_SECONDARY_ID + " INTEGER,"
                + "FOREIGN KEY(" + Constants.KEY_DATE_SECONDARY_ID + ") REFERENCES " + Constants.TABLE_NAME_DATE + "(" + Constants.KEY_DATE_ID + "))";

        String DATE_TABLE = "CREATE TABLE " + Constants.TABLE_NAME_DATE + "("
                + Constants.KEY_DATE_ID + " INTEGER PRIMARY KEY,"
                + Constants.KEY_DATE + " TEXT)";
        Log.d("TAG", "onCreate: " + DATE_TABLE);
        db.execSQL(DATE_TABLE);
        db.execSQL(ITEM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME_IDEA);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME_DATE);
        onCreate(db);
    }

    //Add a date entry to the DB
    public long addDate() {
        SQLiteDatabase db = this.getWritableDatabase();

        //Get values from idea, place in ContentValue class (acceptable by DB)
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.KEY_DATE, java.lang.System.currentTimeMillis());

        long id = db.insert(Constants.TABLE_NAME_DATE, null, contentValues);
        Log.d(DB_ADD, "addItem: " + "added");
        return id;
    }

    //Get a date entry with a specific id
    public DateEntry getDateEntry(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Constants.TABLE_NAME_DATE,
                new String[]{Constants.KEY_IDEA_ID, Constants.KEY_DATE},
                Constants.KEY_IDEA_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        DateEntry dateEntry = new DateEntry();
        if (cursor != null) {
            dateEntry.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_DATE_ID))));
            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
            String formatDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE))).getTime());
            dateEntry.setDateAdded(formatDate);
        }
        assert cursor != null;
        cursor.close();
        return dateEntry;
    }

    //Get all date entries
    public List<DateEntry> getAllDates() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<DateEntry> dateEntryList = new ArrayList<>();
        Cursor cursor = db.query(Constants.TABLE_NAME_DATE,
                new String[]{Constants.KEY_DATE_ID, Constants.KEY_DATE},
                null, null, null, null, Constants.KEY_DATE + " DESC");

        if (cursor.moveToFirst()) {
            do {
                DateEntry dateEntry = new DateEntry();
                dateEntry.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_DATE_ID))));
                DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
                String formatDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE))).getTime());
                dateEntry.setDateAdded(formatDate);

                dateEntryList.add(dateEntry);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return dateEntryList;
    }

    //Add new item to db
    public long addIdea(Idea idea) {
        SQLiteDatabase db = this.getWritableDatabase();

        //Get values from idea, place in ContentValue class (acceptable by DB)
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.KEY_IDEA, idea.getIdea());
        contentValues.put(Constants.KEY_IS_FAVOURITE, idea.getFavourite());
        Log.d("TAG", "addIdea: " + idea.getDateId());
        contentValues.put(Constants.KEY_DATE_SECONDARY_ID, idea.getDateId());

        return db.insert(Constants.TABLE_NAME_IDEA, null, contentValues);
    }

    //Delete a date entry with a specific id
    public void deleteDateEntry(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.TABLE_NAME_DATE,
                Constants.KEY_DATE_ID + "=?",
                new String[]{String.valueOf(id)});

        db.close();
    }

    //Delete all date entries
    public void deleteAllDateEntry() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + Constants.TABLE_NAME_DATE);
        db.execSQL("delete from " + Constants.TABLE_NAME_IDEA);
        db.close();
    }

    //Delete ten idea lists with certain id
    public void deleteAllDateEntryWhere(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.TABLE_NAME_IDEA, Constants.KEY_DATE_SECONDARY_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    //Get the number of date entry entries
    public int getDateEntryCount() {
        String query = "SELECT * FROM " + Constants.TABLE_NAME_DATE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    //Get an idea with a given id
    public Idea getIdea(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Constants.TABLE_NAME_IDEA,
                new String[]{Constants.KEY_IDEA_ID, Constants.KEY_IDEA, Constants.KEY_IS_FAVOURITE, Constants.KEY_DATE_SECONDARY_ID},
                Constants.KEY_IDEA_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);

        Idea idea = new Idea();
        if (cursor.moveToFirst()) {
            idea.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_IDEA_ID))));
            idea.setIdea(cursor.getString(cursor.getColumnIndex(Constants.KEY_IDEA)));
            idea.setDateId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_DATE_SECONDARY_ID))));
            idea.setFavourite(cursor.getString(cursor.getColumnIndex(Constants.KEY_IS_FAVOURITE)).equals("1"));
        }
        cursor.close();
        return idea;
    }

    //Get all ideas
    public List<Idea> getAllIdeas() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Idea> itemList = new ArrayList<>();
        Cursor cursor = db.query(Constants.TABLE_NAME_IDEA,
                new String[]{Constants.KEY_IDEA_ID, Constants.KEY_IDEA, Constants.KEY_IS_FAVOURITE, Constants.KEY_DATE_SECONDARY_ID},
                null, null, null, null, Constants.KEY_DATE_SECONDARY_ID + " ASC");

        if (cursor.moveToFirst()) {
            do {
                Idea idea = new Idea();
                idea.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_IDEA_ID))));
                idea.setIdea(cursor.getString(cursor.getColumnIndex(Constants.KEY_IDEA)));
                idea.setDateId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_DATE_SECONDARY_ID))));
                idea.setFavourite(cursor.getString(cursor.getColumnIndex(Constants.KEY_IS_FAVOURITE)).equals("1"));

                itemList.add(idea);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return itemList;
    }

    //Get an idea with a specific id
    public List<Idea> getAllIdeasWhere(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Idea> itemList = new ArrayList<>();
        Cursor cursor = db.query(Constants.TABLE_NAME_IDEA,
                new String[]{Constants.KEY_IDEA_ID, Constants.KEY_IDEA, Constants.KEY_IS_FAVOURITE, Constants.KEY_DATE_SECONDARY_ID},
                Constants.KEY_DATE_SECONDARY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Idea idea = new Idea();
                idea.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_IDEA_ID))));
                idea.setIdea(cursor.getString(cursor.getColumnIndex(Constants.KEY_IDEA)));
                idea.setDateId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_DATE_SECONDARY_ID))));
                idea.setFavourite(cursor.getString(cursor.getColumnIndex(Constants.KEY_IS_FAVOURITE)).equals("1"));

                itemList.add(idea);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return itemList;
    }

    //Get all ideas that are set to favourite
    public List<Idea> getAllIdeasWhereFavourite() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Idea> itemList = new ArrayList<>();
        Cursor cursor = db.query(Constants.TABLE_NAME_IDEA,
                new String[]{Constants.KEY_IDEA_ID, Constants.KEY_IDEA, Constants.KEY_IS_FAVOURITE, Constants.KEY_DATE_SECONDARY_ID},
                Constants.KEY_IS_FAVOURITE + "=?",
                new String[]{String.valueOf(1)}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Idea idea = new Idea();
                idea.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_IDEA_ID))));
                idea.setIdea(cursor.getString(cursor.getColumnIndex(Constants.KEY_IDEA)));
                idea.setDateId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_DATE_SECONDARY_ID))));
                idea.setFavourite(cursor.getString(cursor.getColumnIndex(Constants.KEY_IS_FAVOURITE)).equals("1"));

                itemList.add(idea);
            } while (cursor.moveToNext());
        }
        cursor.close();
        Log.d("RAJ", "getAllIdeasWhere: " + itemList.size());
        return itemList;
    }


    //Update a given idea
    public void updateIdea(Idea idea) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.KEY_IDEA, idea.getIdea());
        contentValues.put(Constants.KEY_IS_FAVOURITE, idea.getFavourite());

        db.update(Constants.TABLE_NAME_IDEA, contentValues,
                Constants.KEY_IDEA_ID + "=?",
                new String[]{String.valueOf(idea.getId())});
    }

    //Delete an idea with a specific id
    public void deleteIdea(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.TABLE_NAME_IDEA,
                Constants.KEY_IDEA_ID + "=?",
                new String[]{String.valueOf(id)});

        db.close();
    }

    //Get the number of ideas
    public int getIdeaCount() {
        String query = "SELECT * FROM " + Constants.TABLE_NAME_IDEA;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        int count = cursor.getCount();
        cursor.close();
        return count;
    }
}
