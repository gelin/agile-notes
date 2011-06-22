package com.lendamage.agilegtd.android.model.impl;

import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.FOLDER_ID_COLUMN;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.FOLDER_TABLE;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.FULL_NAME_COLUMN;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.ID_COLUMN;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.NAME_COLUMN;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.TYPE_COLUMN;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.lendamage.agilegtd.model.FolderType;
import com.lendamage.agilegtd.model.Path;

/**
 *  Static utility methods to CRUD folders.
 */
class FolderDao {

    static SQLiteFolder insertFolder(SQLiteDatabase db, String name, FolderType type, long parentId) {
        assert(db != null);
        assert(name != null);
        SQLiteFolder parent = selectFolder(db, parentId);
        if (parent == null) {
            parent = selectRootFolder(db);
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
    static SQLiteFolder selectFolder(SQLiteDatabase db, long id) {
        assert(db != null);
        assert(id != 0);
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
    static SQLiteFolder selectRootFolder(SQLiteDatabase db) {
        assert(db != null);
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
        assert(db != null);
        assert(cursor != null);
        assert(!cursor.isClosed() && !cursor.isBeforeFirst() && !cursor.isAfterLast());
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
    
    /**
     *  Updates the folder's parent link.
     *  Updates the folder's full name.
     */
    static void updateFolderParent(SQLiteDatabase db, SQLiteFolder folder, long parentId) {
        assert(db != null);
        assert(folder != null);
        assert(folder.id != 0);
        assert(parentId != 0);
        SQLiteFolder parent = selectFolder(db, parentId);
        if (parent == null) {
            parent = selectRootFolder(db);
        }
        assert(parent != null);
        Path path = parent.getPath().addSegment(folder.getName());
        ContentValues values = new ContentValues();
        values.put(FULL_NAME_COLUMN, String.valueOf(path));
        values.put(FOLDER_ID_COLUMN, parent.id);
        db.update(FOLDER_TABLE, values, ID_COLUMN + " = ?", 
                new String[] { String.valueOf(folder.id) });
        folder.path = path;
        //TODO: link to parent?
    }

}
