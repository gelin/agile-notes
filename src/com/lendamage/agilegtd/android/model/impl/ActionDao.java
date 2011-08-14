package com.lendamage.agilegtd.android.model.impl;

import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.ACTION_ID_COLUMN;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.ACTION_IN_FOLDER_TABLE;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.ACTION_TABLE;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.BODY_COLUMN;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.FOLDER_ID_COLUMN;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.FOLDER_TABLE;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.FULL_NAME_COLUMN;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.HEAD_COLUMN;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.ID_COLUMN;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.NAME_COLUMN;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.SORT_ORDER_COLUMN;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.TYPE_COLUMN;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 *  Static utility methods to CRUD actions.
 */
class ActionDao {

    static SQLiteAction insertAction(SQLiteDatabase db, long folderId, String head, String body) {
        assert(db != null);
        assert(folderId != 0);
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
        replaceActionInFolder(db, folderId, result.id);
        return result;
    }
    
    /**
     *  Returns the action by ID.
     */
    static SQLiteAction selectAction(SQLiteDatabase db, long id) {
        assert(db != null);
        assert(id != 0);
        Cursor cursor = db.query(ACTION_TABLE, 
                null, 
                ID_COLUMN + " = ?",
                new String[] {String.valueOf(id)},
                null,
                null,
                null);
        if (!cursor.moveToFirst()) {
            return null;
        }
        SQLiteAction action = getAction(db, cursor);
        cursor.close();
        return action;
    }
    
    /**
     *  Returns the action's folders cursor by the action ID.
     */
    static Cursor selectFolders(SQLiteDatabase db, long actionId) {
        assert(db != null);
        assert(actionId != 0);
        Cursor cursor = db.query(
                FOLDER_TABLE + " f JOIN " + ACTION_IN_FOLDER_TABLE + " af " +
                "ON (f." + ID_COLUMN + " = af." + FOLDER_ID_COLUMN + ")", 
                new String[] {
                        "f." + ID_COLUMN + " " + ID_COLUMN,
                        "f." + FULL_NAME_COLUMN + " " + FULL_NAME_COLUMN,
                        "f." + NAME_COLUMN + " " + NAME_COLUMN,
                        "f." + TYPE_COLUMN + " " + TYPE_COLUMN,
                        "f." + SORT_ORDER_COLUMN + " " + SORT_ORDER_COLUMN,
                        }, 
                ACTION_ID_COLUMN + " = ?",
                new String[] {String.valueOf(actionId)},
                null,
                null,
                "f." + SORT_ORDER_COLUMN + " ASC, f." + ID_COLUMN + " ASC");
        return cursor;
    }
    
    /**
     *  Returns the action from the current cursor position.
     */
    static SQLiteAction getAction(SQLiteDatabase db, Cursor cursor) {
        assert(db != null);
        assert(cursor != null);
        assert(!cursor.isClosed() && !cursor.isBeforeFirst() && !cursor.isAfterLast());
        long id = cursor.getLong(cursor.getColumnIndexOrThrow(ID_COLUMN));
        SQLiteAction result = new SQLiteAction(db, id);
        result.head = cursor.getString(cursor.getColumnIndexOrThrow(HEAD_COLUMN));
        result.body = cursor.getString(cursor.getColumnIndexOrThrow(BODY_COLUMN));
        return result;
    }
    
    /**
     *  Deletes the action from the folder.
     *  If the action doesn't exists in any folder, remove it.
     */
    static void deleteActionFromFolder(SQLiteDatabase db, long folderId, long actionId) {
        assert(db != null);
        assert(folderId != 0);
        assert(actionId != 0);
        db.delete(ACTION_IN_FOLDER_TABLE, 
                FOLDER_ID_COLUMN + " = ? AND " + ACTION_ID_COLUMN + " = ?", 
                new String[] { String.valueOf(folderId), String.valueOf(actionId) });
        //removing the action if it's removed from all folders is implemented by DB trigger
        /*
        Cursor cursor = selectFolders(db, actionId);
        if (!cursor.moveToFirst()) {
            db.delete(ACTION_TABLE, ID_COLUMN + " = ?", new String[] { String.valueOf(actionId) });
        }
        cursor.close();
        */
    }
    
    /**
     *  Inserts or updates the actions to folder assignment.
     */
    static void replaceActionInFolder(SQLiteDatabase db, long folderId, long actionId) {
        assert(db != null);
        assert(folderId != 0);
        assert(actionId != 0);
        ContentValues values = new ContentValues();
        values.put(FOLDER_ID_COLUMN, folderId);
        values.put(ACTION_ID_COLUMN, actionId);
        db.replaceOrThrow(ACTION_IN_FOLDER_TABLE, null, values);
    }
    
    /**
     *  Updates the action sort order.
     *  @throws IllegalStateException if the action to update doesn't exist
     */
    static void updateActionOrder(SQLiteDatabase db, SQLiteAction action, int order) throws IllegalStateException {
        assert(db != null);
        assert(action != null);
        assert(action.id != 0);
        ContentValues values = new ContentValues();
        values.put(SORT_ORDER_COLUMN, order);
        int updated = db.update(ACTION_IN_FOLDER_TABLE, values, ACTION_ID_COLUMN + " = ?", 
                new String[] { String.valueOf(action.id) });
        if (updated == 0) {
            throw new IllegalStateException("no action with id = " + action.id + ", order update failed");
        }
    }
    
    /**
     *  Deletes all actions from the folder.
     */
    static void deleteActionsFromFolder(SQLiteDatabase db, long folderId) {
        assert(db != null);
        assert(folderId != 0);
        db.delete(ACTION_IN_FOLDER_TABLE, FOLDER_ID_COLUMN + " = ?", new String[] { String.valueOf(folderId) });
    }
    
    /**
     *  Updates the action head and body.
     *  @throws IllegalStateException if the action to update doesn't exist
     */
    static void updateAction(SQLiteDatabase db, SQLiteAction action, String head, String body) throws IllegalStateException {
        assert(db != null);
        assert(action != null);
        assert(action.id != 0);
        assert(head != null);
        ContentValues values = new ContentValues();
        values.put(HEAD_COLUMN, head);
        if (body == null) {
            values.putNull(BODY_COLUMN);
        } else {
            values.put(BODY_COLUMN, body);
        }
        int updated = db.update(ACTION_TABLE, values, ID_COLUMN + " = ?", 
                new String[] { String.valueOf(action.id) });
        if (updated == 0) {
            throw new IllegalStateException("no action with id = " + action.id + ", update failed");
        }
    }

}
