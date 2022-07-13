package com.dis.designeditor.api;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String TABLE_NAME = "images";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FILEPATH = "path";
    public DBHelper(Context context){
        super(context,DATABASE_NAME,null,1);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table images " +
                        "(id integer primary key AUTOINCREMENT, path text)"
        );
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS images");
        onCreate(db);
    }

    public boolean insertImage (String path) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_FILEPATH, path);
        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }

    public ArrayList<String> getAllImages() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from images", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(COLUMN_FILEPATH)));
            res.moveToNext();
        }
        return array_list;
    }

}
