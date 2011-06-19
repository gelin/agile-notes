package com.lendamage.agilegtd.android.model.impl;

import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.FOLDER_ID_COLUMN;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.FOLDER_TABLE;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.FULL_NAME_COLUMN;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.ID_COLUMN;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.NAME_COLUMN;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.TYPE_COLUMN;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.lendamage.agilegtd.model.FolderType;
import com.lendamage.agilegtd.model.Path;

/**
 *  Static utility methods
 */
public class SQLiteUtils {

    static SQLiteFolder insertFolder(SQLiteDatabase db, String name, FolderType type, long parentId) {
        assert(name != null);
        SQLiteFolder parent = getFolder(db, parentId);
        if (parent == null) {
            parent = getRootFolder(db);
        }
        assert(parent != null);
        Path path = parent.getPath().addSegment(name);
        SQLiteStatement st = db.compileStatement(
                "INSERT into " + FOLDER_TABLE + 
                " (" + FULL_NAME_COLUMN + ", " + NAME_COLUMN + ", " + TYPE_COLUMN + ", " + FOLDER_ID_COLUMN + ")" +
                " values (?, ?, ?, ?)");
        st.bindString(1, String.valueOf(path));
        st.bindString(2, name);
        if (type != null) {
            st.bindString(3, String.valueOf(type));
        }
        st.bindLong(4, parent.id);
        long id = st.executeInsert();
        SQLiteFolder result = new SQLiteFolder(db, id);
        result.path = path;
        result.name = path.getLastSegment();
        result.type = type;
        //TODO: link to parent?
        return result;
    }
    
    /**
     *  Returns the folder by ID.
     */
    static SQLiteFolder getFolder(SQLiteDatabase db, long id) {
        Cursor cursor = db.query(FOLDER_TABLE, 
                null, 
                ID_COLUMN + " = ?",
                new String[] {String.valueOf(id)},
                null,
                null,
                null);
        if (!cursor.moveToFirst()) {
            return null;
        }
        return getFolder(db, cursor);
    }
    
    /**
     *  Returns root folder by type.
     */
    static SQLiteFolder getRootFolder(SQLiteDatabase db) {
        Cursor cursor = db.query(FOLDER_TABLE, 
                null, 
                TYPE_COLUMN + " = ?",
                new String[] {FolderType.ROOT.toString()},
                null,
                null,
                null);
        if (!cursor.moveToFirst()) {
            return null;
        }
        return getFolder(db, cursor);
    }
    
    /**
     *  Returns the folder from the current cursor position.
     */
    static SQLiteFolder getFolder(SQLiteDatabase db, Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex(ID_COLUMN));
        SQLiteFolder result = new SQLiteFolder(db, id);
        Path path = new SimplePath(cursor.getString(cursor.getColumnIndex(FULL_NAME_COLUMN)));
        result.path = path;
        result.name = cursor.getString(cursor.getColumnIndex(NAME_COLUMN));
        String type = cursor.getString(cursor.getColumnIndex(TYPE_COLUMN));
        if (type != null) {
            result.type = FolderType.valueOf(type);
        }
        //TODO: link to parent?
        return result;
    }

}
