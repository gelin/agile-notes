package com.lendamage.agilegtd.android.model.impl;

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
    SQLiteDatabase db;
    
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
        db.beginTransaction();
        try {
            Folder currentFolder = getFolder(path.toString());
            if (currentFolder != null) {
                return currentFolder;
            }
            Folder result = insertFolder(db, path, type);
            Path currentPath = path.getParent();
            while (!currentPath.isRoot()) {
                //TODO: check the existence in more elegant way
                currentFolder = getFolder(currentPath.toString());
                if (currentFolder == null) {
                    insertFolder(db, currentPath, null);    //type for transit folder is not defined
                }
                currentPath = currentPath.getParent();
            }
            db.setTransactionSuccessful();
            return result;
        } finally {
            db.endTransaction();
        }
    }
    
    static SQLiteFolder insertFolder(SQLiteDatabase db, Path path, FolderType type) {
        SQLiteStatement st = db.compileStatement(
                "INSERT into " + FOLDER_TABLE + 
                " (" + FULL_NAME_COLUMN + ", " + NAME_COLUMN + ", " + TYPE_COLUMN +")" +
                " values (?, ?, ?)");
        st.bindString(1, String.valueOf(path));
        st.bindString(2, path.getLastSegment());
        if (type != null) {
            st.bindString(3, String.valueOf(type));
        }
        long id = st.executeInsert();
        SQLiteFolder result = new SQLiteFolder(id);
        result.path = path;
        result.name = path.getLastSegment();
        result.type = type;
        return result;
    }
    
    //@Override
    public Folder getFolder(String fullName) {
        assert(fullName != null);
        Cursor cursor = db.query(FOLDER_TABLE, 
                null, 
                FULL_NAME_COLUMN + " = ?",
                new String[] {fullName},
                null,
                null,
                null);
        if (!cursor.moveToFirst()) {
            return null;
        }
        long id = cursor.getLong(cursor.getColumnIndex(ID_COLUMN));
        SQLiteFolder result = new SQLiteFolder(id);
        Path path = new SimplePath(cursor.getString(cursor.getColumnIndex(FULL_NAME_COLUMN)));
        result.path = path;
        result.name = path.getLastSegment();
        String type = cursor.getString(cursor.getColumnIndex(TYPE_COLUMN));
        if (type != null) {
            result.type = FolderType.valueOf(type);
        }
        return result;
    }

    //@Override
    public Folder getRootFolder() {
        return getFolder("");
    }



}
