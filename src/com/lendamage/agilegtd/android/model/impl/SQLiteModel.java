package com.lendamage.agilegtd.android.model.impl;

import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.lendamage.agilegtd.model.Folder;
import com.lendamage.agilegtd.model.FolderType;
import com.lendamage.agilegtd.model.Model;
import com.lendamage.agilegtd.model.Path;

/**
 *  Model which uses Android's SQLite database.
 */
public class SQLiteModel implements Model {

    /** DB handler */
    transient SQLiteDatabase db;
    /** Root folder */
    SQLiteFolder root;
    
    /**
     *  Creates the model
     */
    public SQLiteModel(Context context, String dbName) {
        db = new SQLiteModelOpenHelper(context, dbName).getWritableDatabase();
        root = FolderDao.selectRootFolder(db);
    }
    
    //@Override
    public Folder getFolder(Path fullPath) {
        return FolderDao.selectFolder(db, fullPath);
    }
    
    //@Override
    public Folder getRootFolder() {
        return root;
    }
    
    //@Override
    public List<Folder> findFolders(FolderType type) {
        //TODO: implement
        return null;
    }

}
