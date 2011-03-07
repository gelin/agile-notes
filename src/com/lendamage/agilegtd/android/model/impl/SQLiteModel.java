package com.lendamage.agilegtd.android.model.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.lendamage.agilegtd.model.Action;
import com.lendamage.agilegtd.model.Folder;
import com.lendamage.agilegtd.model.FolderType;
import com.lendamage.agilegtd.model.Model;

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
    public Action newAction(String head) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Folder newFolder(String fullName, FolderType type) {
        // TODO Auto-generated method stub
        return null;
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
