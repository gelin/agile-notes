package com.lendamage.agilegtd.android.model.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.lendamage.agilegtd.model.FolderType;

/**
 *  Helper to open our SQLite database.
 */
public class SQLiteModelOpenHelper extends SQLiteOpenHelper {

    /** Version of database schema */
    private static final int DATABASE_VERSION = 1;
    
    /** Folder table name */
    static final String FOLDER_TABLE = "folder";
    /** Action table name */
    static final String ACTION_TABLE = "action";
    /** Action in folder table name */
    static final String ACTION_IN_FOLDER_TABLE = "action_in_folder";
    
    /** ID column name */
    static final String ID_COLUMN = BaseColumns._ID;
    /** Name column name */
    static final String NAME_COLUMN = "name";
    /** Full name column name */
    static final String FULL_NAME_COLUMN = "full_name";
    /** Folder ID column name */
    static final String FOLDER_ID_COLUMN = "folder_id";
    /** Sort order column name */
    static final String SORT_ORDER_COLUMN = "sort_order";
    /** Type column name */
    static final String TYPE_COLUMN = "type";
    /** Head column name */
    static final String HEAD_COLUMN = "head";
    /** Body column name */
    static final String BODY_COLUMN = "body";
    /** Action ID column name */
    static final String ACTION_ID_COLUMN = "action_id";
    
    SQLiteModelOpenHelper(Context context, String name) {
        super(context, name, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + FOLDER_TABLE + " (" +
                ID_COLUMN + " INTEGER PRIMARY KEY, "+
                NAME_COLUMN + " TEXT, " +
                FULL_NAME_COLUMN + " TEXT, " +
                FOLDER_ID_COLUMN + " INTEGER REFERENCES " + 
                        FOLDER_TABLE + "(" + ID_COLUMN + ") ON DELETE CASCADE, " +
                SORT_ORDER_COLUMN + " INTEGER, " +
                TYPE_COLUMN + " TEXT " +
                ")");
        db.execSQL("CREATE TABLE " + ACTION_TABLE + " (" +
                ID_COLUMN + " INTEGER PRIMARY KEY, "+
                HEAD_COLUMN + " TEXT, " +
                BODY_COLUMN + " TEXT " +
                ")");
        db.execSQL("CREATE TABLE " + ACTION_IN_FOLDER_TABLE + " (" +
                FOLDER_ID_COLUMN + " INTEGER REFERENCES " + 
                        FOLDER_TABLE + "(" + ID_COLUMN + ") ON DELETE SET NULL, " +
                ACTION_ID_COLUMN + " INTEGER REFERENCES " + 
                        ACTION_TABLE + "(" + ID_COLUMN + ") ON DELETE SET NULL, " +
                SORT_ORDER_COLUMN + " INTEGER, " +
                "PRIMARY KEY (" + FOLDER_ID_COLUMN + ", "+ ACTION_ID_COLUMN + ") " +
                ")");
        insertRootFolder(db);
    }
    
    void insertRootFolder(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(FULL_NAME_COLUMN, "");
        values.put(NAME_COLUMN, "");
        values.put(TYPE_COLUMN, String.valueOf(FolderType.ROOT));
        db.insert(FOLDER_TABLE, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // nothing to do, only one version
    }

}
