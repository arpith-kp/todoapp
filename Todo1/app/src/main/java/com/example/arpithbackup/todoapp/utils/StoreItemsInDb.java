package com.example.arpithbackup.todoapp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.arpithbackup.todoapp.models.ItemModel;

import java.util.ArrayList;


/**
 * Created by arpithbackup on 2/11/17.
 */
public class StoreItemsInDb extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "todosDatabase";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_TODOS = "todos";
    public static final String KEY_TODO_ID = "_id";
    public static final String KEY_TODO_TEXT = "text";
    public static final String KEY_TODO_DATE = "date";
    public static StoreItemsInDb sInstance;


    public static synchronized StoreItemsInDb getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new StoreItemsInDb(context.getApplicationContext());
        }
        return sInstance;
    }

    public StoreItemsInDb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TODOS_TABLE = "CREATE TABLE " + TABLE_TODOS +
                "(" +
                KEY_TODO_ID + " INTEGER PRIMARY KEY," +
                KEY_TODO_TEXT + " TEXT," +
                KEY_TODO_DATE + " TEXT" +
                ")";
        db.execSQL(CREATE_TODOS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODOS);
            onCreate(db);
        }
    }

    public void addItemToDb(ItemModel item) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_TODO_TEXT, item.getName());
            db.insertOrThrow(TABLE_TODOS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            db.endTransaction();
        }
    }

    public void updateItemToDb(ItemModel item) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues updateTodo = new ContentValues();
            updateTodo.put(KEY_TODO_TEXT, item.getName());

            db.update(TABLE_TODOS, updateTodo, KEY_TODO_ID + "= ?", new String[]{String.valueOf(item.getId())});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            db.endTransaction();
        }

    }

    public Cursor getCursorForItem() throws SQLException {
        String TODOS_SELECT_QUERY =
                String.format("SELECT _id, text, date FROM %s ",
                        TABLE_TODOS + ";"
                );
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(TODOS_SELECT_QUERY, null);
        try {
            if (cursor != null) {
                cursor.moveToFirst();
            }
        } catch (Exception e) {
            //do nothing
            System.out.print(e);
        }
        try {
            return cursor;

        } catch (Exception e) {
            System.out.print(e);
        }
        return null;
    }

    public ArrayList getAllItemFromDb() {

        ArrayList<ItemModel> todos = new ArrayList<>();
        String TODOS_SELECT_QUERY =
                String.format("SELECT * FROM %s ",
                        TABLE_TODOS
                );
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(TODOS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    String newItem = cursor.getString(cursor.getColumnIndex(KEY_TODO_TEXT));
                    ItemModel item = new ItemModel();
                    item.setId(cursor.getInt(0));
                    item.setName(newItem);
                    todos.add(item);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            //do nothing
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return todos;
    }

    public void deleteItemFromDb(ItemModel item) {
        SQLiteDatabase db = getWritableDatabase();
        String ids = String.valueOf(item.getId());
        db.beginTransaction();
        try {
            String[] whereArgs = {ids};
            db.delete(TABLE_TODOS, KEY_TODO_ID + "=?", whereArgs);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            System.out.print(e);

        } finally {
            db.endTransaction();
        }
    }

    public void deleteAllItemFromDb() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_TODOS, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {

        } finally {
            db.endTransaction();
        }
    }


}
