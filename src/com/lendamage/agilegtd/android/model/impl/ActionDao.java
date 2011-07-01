package com.lendamage.agilegtd.android.model.impl;

import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.ACTION_ID_COLUMN;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.ACTION_IN_FOLDER_TABLE;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.ACTION_TABLE;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.BODY_COLUMN;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.FOLDER_ID_COLUMN;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.FOLDER_TABLE;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.HEAD_COLUMN;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.ID_COLUMN;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.SORT_ORDER_COLUMN;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 *  Static utility methods to CRUD actions.
 */
class ActionDao {

    static SQLiteAction insertAction(SQLiteDatabase db, String head, String body) {
        assert(db != null);
        assert(head != null);
        
        ContentValues values = new ContentValues();
        values.put(HEAD_COLUMN, head);
        if (body != null) {
            values.put(BODY_COLUMN, body);
        }
        long id = db.insertOrThrow(ACTION_TABLE, null, values);
        SQLiteAction result = new SQLiteAction(db, id);
        result.head = head;
        result.body = body;
        return result;
    }
    
    /**
     *  Returns the action by ID.
     */
    static SQLiteAction selectAction(SQLiteDatabase db, long id) {
        //TODO
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
        return getAction(db, cursor);
    }
    
    /**
     *  Returns the action from the current cursor position.
     */
    static SQLiteAction getAction(SQLiteDatabase db, Cursor cursor) {
        assert(db != null);
        assert(cursor != null);
        assert(!cursor.isClosed() && !cursor.isBeforeFirst() && !cursor.isAfterLast());
        long id = cursor.getLong(cursor.getColumnIndex(ID_COLUMN));
        SQLiteAction result = new SQLiteAction(db, id);
        result.head = cursor.getString(cursor.getColumnIndex(HEAD_COLUMN));
        result.body = cursor.getString(cursor.getColumnIndex(BODY_COLUMN));
        return result;
    }
    
    /**
     *  Updates the actions to folder assignment.
     */
    static void replaceActionFolder(SQLiteDatabase db, SQLiteAction action, long folderId) {
        assert(db != null);
        assert(action != null);
        assert(action.id != 0);
        assert(folderId != 0);
        ContentValues values = new ContentValues();
        values.put(FOLDER_ID_COLUMN, folderId);
        values.put(ACTION_ID_COLUMN, action.id);
        db.replaceOrThrow(ACTION_IN_FOLDER_TABLE, null, values);
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
