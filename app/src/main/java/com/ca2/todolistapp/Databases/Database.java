package com.ca2.todolistapp.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ca2.todolistapp.Pojo.Data;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME = "TodoList.db";
    public static final String TABLE_NAME = "ListItem";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TOPIC = "topic";
    public static final String COLUMN_DESCRIPTION = "description";

    public Database(Context context)
    {
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL(
                "create table ListItem " +
                        "(id integer primary key, topic text,description text)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(sqLiteDatabase);
    }
    public boolean addItemToList(String topic,String description)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(COLUMN_TOPIC,topic);
        values.put(COLUMN_DESCRIPTION,description);
        db.insert(TABLE_NAME,null,values);
        return true;
    }
    public Data getListItems(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from ListItem where id="+id+"", null );
        res.moveToFirst();
        Data data=new Data(res.getString(1),res.getString(2),res.getInt(0));
        res.close();
        return data;
    }
    public boolean updateItem(int id,String topic,String description)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(COLUMN_TOPIC,topic);
        values.put(COLUMN_DESCRIPTION,description);
        db.update(TABLE_NAME, values, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }
    public Integer deleteItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("ListItem",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }
    public ArrayList<Data> getAllItems()
    {
        ArrayList<Data> dataArrayList=new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from ListItem",null);
        if(cursor.moveToFirst())
        {
            do {
                dataArrayList.add(new Data(cursor.getString(1),cursor.getString(2),cursor.getInt(0)));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return dataArrayList;
    }

}
