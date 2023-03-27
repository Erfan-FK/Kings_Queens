package com.app.presidents;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "presidents.db";
    public static final String TABLE_NAME = "presidents";
    public static final int DB_VERSION = 1;

    static class Columns{
        public static final String ID = "_id";
        public static final String NAME = "name";
        public static final String DATE = "date";
        public static final String IMAGE_URI = "imageuri";
        public static final String COUNTRY = "country";
    }

    public DbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE "
                + TABLE_NAME + "("
                + Columns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Columns.NAME + " TEXT, "
                + Columns.DATE + " TEXT, "
                + Columns.IMAGE_URI + " TEXT, "
                + Columns.COUNTRY + " TEXT);";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addPresident(President president){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Columns.NAME, president.getName());
        cv.put(Columns.DATE, president.getDate());
        cv.put(Columns.IMAGE_URI, president.getImageURL());
        cv.put(Columns.COUNTRY, president.getCountry());
        db.insert(TABLE_NAME, null, cv);

//        String query = "INSERT INTO " + TABLE_NAME + "(name, date, imageURI, country) VALUES('"
//                + president.getName() + "', '"
//                + president.getDate() + "', '"
//                + president.getImageURL() + "', '"
//                + president.getCountry() + "');";
//        db.execSQL(query);
    }

    public List<President> getPresidents(){
      List<President> data = new ArrayList<>();

      SQLiteDatabase db = this.getReadableDatabase();
      String query = "SELECT * FROM " + TABLE_NAME;

      Cursor cursor = db.rawQuery(query, null);
      cursor.moveToFirst();
      if (cursor.getCount() <= 0)
          return data;
      do{
          String name = cursor.getString(1);
          String date = cursor.getString(3);
          String imageURI = cursor.getString(2);
          String country = cursor.getString(4);
          data.add(new President(name, date, imageURI, country));
      } while(cursor.moveToNext());

      cursor.close();
      db.close();
      return data;
    }

    public President getOne(String name){
        President president;

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + Columns.NAME + " = '" +  name + "';";

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        president = new President(cursor.getString(1), cursor.getString(3), cursor.getString(2), cursor.getString(4));

        cursor.close();
        db.close();
        return president;
    }

    public void updatePresident(String oldName, President president){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET "
                + Columns.NAME + " = '" + president.getName() + "', "
                + Columns.DATE + " = '" + president.getDate() + "', "
                + Columns.IMAGE_URI + " = '" + president.getImageURL() + "', "
                + Columns.COUNTRY + " = '" + president.getCountry() + "' "
                +"WHERE " + Columns.NAME + " = '" + oldName + "';";
        db.execSQL(query);
    }

    public void deletePresident(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + Columns.NAME + " = '" + name + "';";

        db.execSQL(query);
    }

    public List<President> sort(String columnName, String order){
        List<President> sortedData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + columnName + " " + order;

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        do{
            President president = new President(cursor.getString(1), cursor.getString(3), cursor.getString(2), cursor.getString(4));
            sortedData.add(president);
        } while (cursor.moveToNext());

        cursor.close();
        db.close();
        return sortedData;
    }
}
