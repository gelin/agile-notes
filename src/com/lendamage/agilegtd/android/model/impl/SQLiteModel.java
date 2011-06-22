package com.lendamage.agilegtd.android.model.impl;

import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.ACTION_TABLE;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.BODY_COLUMN;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.FOLDER_TABLE;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.FULL_NAME_COLUMN;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.HEAD_COLUMN;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

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
