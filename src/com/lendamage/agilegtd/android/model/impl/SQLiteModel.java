package com.lendamage.agilegtd.android.model.impl;

import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.lendamage.agilegtd.model.Action;
import com.lendamage.agilegtd.model.Folder;
import com.lendamage.agilegtd.model.FolderType;
import com.lendamage.agilegtd.model.Model;
import com.lendamage.agilegtd.model.Path;

import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.*;

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
    public Folder newFolder(Path path, FolderType type) {
        assert(path != null);
        SQLiteFolder currentFolder = (SQLiteFolder)getFolder(path);
        if (currentFolder != null) {
            return currentFolder;
        }
        List<String> segments = path.getSegments();
        db.beginTransaction();
        try {
            Path currentPath = new SimplePath("");
            SQLiteFolder parentFolder = (SQLiteFolder)getFolder(currentPath);
            for (int i = 0; i < segments.size() - 1; i++) {
                String segment = segments.get(i);
                currentPath = currentPath.addSegment(segment);
                //TODO: check the existence in more elegant way
                currentFolder = (SQLiteFolder)getFolder(currentPath);
                if (currentFolder == null) {
                    currentFolder = SQLiteUtils.insertFolder(db, currentPath, null, parentFolder.id);    //transit folders have no type
                }
                parentFolder = currentFolder;
            }
            Folder result = SQLiteUtils.insertFolder(db, path, type, parentFolder.id);
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
        return SQLiteUtils.getFolder(db, cursor);
    }
    
    //@Override
    public Folder getRootFolder() {
        return getFolder(new SimplePath(""));
    }

}
