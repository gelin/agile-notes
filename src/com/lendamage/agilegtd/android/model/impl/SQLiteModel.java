package com.lendamage.agilegtd.android.model.impl;

import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.FOLDER_TABLE;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.FULL_NAME_COLUMN;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lendamage.agilegtd.model.Action;
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
    
    /**
     *  Creates the model
     */
    public SQLiteModel(Context context, String dbName) {
        db = new SQLiteModelOpenHelper(context, dbName).getWritableDatabase();
    }
    
    //@Override
    public Action newAction(String head, String body) {
        assert(head != null);
        db.beginTransaction();
        try {
            SQLiteAction result = ActionDao.insertAction(db, head, body);
            result.head = head;
            result.body = body;
            db.setTransactionSuccessful();
            return result;
        } finally {
            db.endTransaction();
        }
    }

    //@Override
    public Folder newFolder(String name, FolderType type) {
        assert(name != null);
        db.beginTransaction();
        try {
            SQLiteFolder parent = (SQLiteFolder)getRootFolder();
            SQLiteFolder result = FolderDao.insertFolder(db, name, type, parent.id);
            db.setTransactionSuccessful();
            return result;
        } finally {
            db.endTransaction();
        }
    }
    
    //@Override
    public Folder getFolder(Path fullPath) {
        assert(fullPath != null);
        Cursor cursor = db.query(FOLDER_TABLE, 
                null, 
                FULL_NAME_COLUMN + " = ?",
                new String[] {fullPath.toString()},
                null,
                null,
                null);
        if (!cursor.moveToFirst()) {
            return null;
        }
        return FolderDao.getFolder(db, cursor);
    }
    
    //@Override
    public Folder getRootFolder() {
        return FolderDao.selectRootFolder(db);
    }

}
