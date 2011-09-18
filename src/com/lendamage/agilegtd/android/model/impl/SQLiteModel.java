package com.lendamage.agilegtd.android.model.impl;

import static com.lendamage.agilegtd.android.model.impl.CommonDao.checkDb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lendamage.agilegtd.model.Folder;
import com.lendamage.agilegtd.model.FolderType;
import com.lendamage.agilegtd.model.Model;
import com.lendamage.agilegtd.model.ModelException;
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
        checkDb(db);
        db.beginTransaction();
        try {
            root = FolderDao.selectRootFolder(db);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        if (root == null) {
            throw new ModelException("no root folder");
        }
    }
    
    //@Override
    public Folder getFolder(Path fullPath) {
        Folder result = null;
        checkDb(db);
        db.beginTransaction();
        try {
            result = FolderDao.selectFolder(db, fullPath);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return result;
    }
    
    //@Override
    public Folder getRootFolder() {
        return root;
    }
    
    //@Override
    public List<Folder> findFolders(FolderType type) {
        List<SQLiteFolder> result = new ArrayList<SQLiteFolder>();
        checkDb(db);
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
    
    public void close() {
        db.close();
    }

}
