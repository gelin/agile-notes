package com.lendamage.agilegtd.android.model.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.lendamage.agilegtd.model.Action;
import com.lendamage.agilegtd.model.Folder;
import com.lendamage.agilegtd.model.FolderType;
import com.lendamage.agilegtd.model.Model;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.*;

/**
 *  Model which uses Android's SQLite database.
 */
public class SQLiteModel implements Model {

    /** DB handler */
    SQLiteDatabase db;
    
    /**
     *  Creates the model
     */
    public SQLiteModel(Context context, String dbName) {
        db = new SQLiteModelOpenHelper(context, dbName).getWritableDatabase();
    }
    
    @Override
    public Action newAction(String head, String body) {
        db.beginTransaction();
        try {
            SQLiteStatement st = db.compileStatement(
                    "INSERT into " + ACTION_TABLE + 
                    " (" + HEAD_COLUMN + ", " + BODY_COLUMN +") values (?, ?)");
            st.bindString(1, head);
            st.bindString(2, body);
            long id = st.executeInsert();
            SQLiteAction result = new SQLiteAction(id);
            result.head = head;
            result.body = body;
            db.setTransactionSuccessful();
            return result;
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public Folder newFolder(String fullName, FolderType type) {
        db.beginTransaction();
        try {
            //TODO: subfolders creation
            //TODO: check for folder existence
            SQLiteStatement st = db.compileStatement(
                    "INSERT into " + FOLDER_TABLE + 
                    " (" + FULL_NAME_COLUMN + ", " + TYPE_COLUMN +") values (?, ?)");
            st.bindString(1, fullName);
            if (type != null) {
                st.bindString(2, String.valueOf(type));
            }
            long id = st.executeInsert();
            SQLiteFolder result = new SQLiteFolder(id);
            result.fullName = fullName;
            result.name = fullName; //TODO
            result.type = type;
            db.setTransactionSuccessful();
            return result;
        } finally {
            db.endTransaction();
        }
    }
    
    @Override
    public Folder getFolder(String fullName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Folder getRootFolder() {
        // TODO Auto-generated method stub
        return null;
    }



}
