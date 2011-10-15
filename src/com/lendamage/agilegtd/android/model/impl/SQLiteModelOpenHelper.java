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
class SQLiteModelOpenHelper extends SQLiteOpenHelper {

    /** Version of database schema */
    private static final int DATABASE_VERSION = 2;
    
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
                FULL_NAME_COLUMN + " TEXT UNIQUE, " +
                FOLDER_ID_COLUMN + " INTEGER REFERENCES " + 
                        FOLDER_TABLE + "(" + ID_COLUMN + ") ON DELETE CASCADE, " +
                SORT_ORDER_COLUMN + " INTEGER DEFAULT " + Long.MAX_VALUE + ", " +
                TYPE_COLUMN + " TEXT " +
                ")");
        db.execSQL("CREATE TABLE " + ACTION_TABLE + " (" +
                ID_COLUMN + " INTEGER PRIMARY KEY, "+
                HEAD_COLUMN + " TEXT, " +
                BODY_COLUMN + " TEXT " +
                ")");
        db.execSQL("CREATE TABLE " + ACTION_IN_FOLDER_TABLE + " (" +
                FOLDER_ID_COLUMN + " INTEGER REFERENCES " + 
                        FOLDER_TABLE + "(" + ID_COLUMN + ") ON DELETE CASCADE, " +
                ACTION_ID_COLUMN + " INTEGER REFERENCES " + 
                        ACTION_TABLE + "(" + ID_COLUMN + ") ON DELETE CASCADE, " +
                SORT_ORDER_COLUMN + " INTEGER DEFAULT " + Long.MAX_VALUE + ", " +
                "PRIMARY KEY (" + FOLDER_ID_COLUMN + ", "+ ACTION_ID_COLUMN + ") " +
                ")");
        //foreign keys are supported from SQLite v. 3.6.19 ( http://www.sqlite.org/foreignkeys.html )
        //adding triggers to emulate ON DELETE CASCADE
        //IMPORTANT: this trigger is not actually cascade, 
        //because recursive triggers appears only in SQLite v.3.6.18
        //( http://www.sqlite.org/pragma.html#pragma_recursive_triggers )
        /*  The trigger removed in DB v.2, the cascade deletion is moved to Java code
        db.execSQL("CREATE TRIGGER " + FOLDER_TABLE + "_AFTER_DELETE AFTER DELETE ON " + FOLDER_TABLE + 
                " BEGIN " +
                "DELETE FROM " + FOLDER_TABLE + " WHERE " + FOLDER_ID_COLUMN + " = OLD." + ID_COLUMN + "; " +
                "DELETE FROM " + ACTION_IN_FOLDER_TABLE + " WHERE " + FOLDER_ID_COLUMN + " = OLD." + ID_COLUMN + "; " +
                " END");
        */
        db.execSQL("CREATE TRIGGER " + ACTION_TABLE + "_AFTER_DELETE AFTER DELETE ON " + ACTION_TABLE + 
                " BEGIN " +
                "DELETE FROM " + ACTION_IN_FOLDER_TABLE + " WHERE " + ACTION_ID_COLUMN + " = OLD." + ID_COLUMN + "; " +
                " END");
        //when the last folder for the action is deleted
        db.execSQL("CREATE TRIGGER " + ACTION_IN_FOLDER_TABLE + "_AFTER_DELETE AFTER DELETE ON " + ACTION_IN_FOLDER_TABLE +
                " WHEN NOT EXISTS " +   
                "( SELECT * FROM " + ACTION_IN_FOLDER_TABLE +" WHERE " + ACTION_ID_COLUMN + " = OLD." + ACTION_ID_COLUMN + " )" +
                " BEGIN " +
                "DELETE FROM " + ACTION_TABLE + " WHERE " + ID_COLUMN + " = OLD." + ACTION_ID_COLUMN + "; " +
                " END");
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
        if (oldVersion == 1 && newVersion == 2) {
            upgrade1to2(db);
        }
    }
    
    void upgrade1to2(SQLiteDatabase db) {
        db.execSQL("DROP TRIGGER IF EXISTS " + FOLDER_TABLE + "_AFTER_DELETE");
    }

}
