package com.lendamage.agilegtd.android.model.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
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
        List<SQLiteFolder> result = new ArrayList<SQLiteFolder>();
        db.beginTransaction();
        try {
            Cursor cursor = FolderDao.selectFolders(db, type);
            while (cursor.moveToNext()) {
                result.add(FolderDao.getFolder(db, cursor));
            }
            Collections.sort(result, new SQLiteFolderComparator(db));
            cursor.close();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        List<Folder> typedResult = new ArrayList<Folder>();
        typedResult.addAll(result);
        return Collections.unmodifiableList(typedResult);
    }

}
