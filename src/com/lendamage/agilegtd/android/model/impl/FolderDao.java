package com.lendamage.agilegtd.android.model.impl;

import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.FOLDER_ID_COLUMN;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.FOLDER_TABLE;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.FULL_NAME_COLUMN;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.ID_COLUMN;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.NAME_COLUMN;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.SORT_ORDER_COLUMN;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.TYPE_COLUMN;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lendamage.agilegtd.model.FolderType;
import com.lendamage.agilegtd.model.Path;

/**
 *  Static utility methods to CRUD folders.
 */
class FolderDao {

    static SQLiteFolder insertFolder(SQLiteDatabase db, long parentId, String name, FolderType type) {
        assert(db != null);
        assert(parentId != 0);
        assert(name != null);
        SQLiteFolder parent = selectFolder(db, parentId);
        assert(parent != null);
        Path path = parent.getPath().addSegment(name);
        ContentValues values = new ContentValues();
        values.put(FULL_NAME_COLUMN, String.valueOf(path));
        values.put(NAME_COLUMN, name);
        if (type != null) {
            values.put(TYPE_COLUMN, String.valueOf(type));
        }
        values.put(FOLDER_ID_COLUMN, parent.id);
        long id = db.insertOrThrow(FOLDER_TABLE, null, values);
        SQLiteFolder result = new SQLiteFolder(db, id);
        result.path = path;
        result.name = path.getLastSegment();
        result.type = type;
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
        return result;
    }
    
    /**
     *  Updates the folder's parent link.
     *  Updates the folder's full name.
     *  @throws IllegalStateException if the folder to update doesn't exist
     */
    static void updateFolderParent(SQLiteDatabase db, SQLiteFolder folder, long parentId) throws IllegalStateException {
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
        int updated = db.update(FOLDER_TABLE, values, ID_COLUMN + " = ?", 
                new String[] { String.valueOf(folder.id) });
        if (updated == 0) {
            throw new IllegalStateException("no folder with id = " + folder.id + ", move failed");
        }
        folder.path = path;
    }
    
    /**
     *  Updates the folder sort order.
     *  @throws IllegalStateException if the folder to update doesn't exist
     */
    static void updateFolderOrder(SQLiteDatabase db, SQLiteFolder folder, int order) throws IllegalStateException {
        assert(db != null);
        assert(folder != null);
        assert(folder.id != 0);
        ContentValues values = new ContentValues();
        values.put(SORT_ORDER_COLUMN, order);
        int updated = db.update(FOLDER_TABLE, values, ID_COLUMN + " = ?", 
                new String[] { String.valueOf(folder.id) });
        if (updated == 0) {
            throw new IllegalStateException("no folder with id = " + folder.id + ", order update failed");
        }
    }
    
    /**
     *  Deletes the folder.
     */
    static void deleteFolder(SQLiteDatabase db, long id) {
        assert(db != null);
        assert(id != 0);
        db.delete(FOLDER_TABLE, ID_COLUMN + " = ?", new String[] { String.valueOf(id) });
    }
    
    /**
     *  Deletes all child folder.
     */
    static void deleteChildFolders(SQLiteDatabase db, long parentId) {
        assert(db != null);
        assert(parentId != 0);
        db.delete(FOLDER_TABLE, FOLDER_ID_COLUMN + " = ?", new String[] { String.valueOf(parentId) });
    }

}
