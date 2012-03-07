/*
    Agile GTD. Flexible Android implementation of GTD.
    Copyright (C) 2011  Denis Nelubin

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.lendamage.agilegtd.android.model.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.lendamage.agilegtd.model.FolderType;
import com.lendamage.agilegtd.model.OuroborosException;
import com.lendamage.agilegtd.model.Path;

import static com.lendamage.agilegtd.android.model.impl.CommonDao.checkDb;
import static com.lendamage.agilegtd.android.model.impl.SQLiteModelOpenHelper.*;

/**
 *  Static utility methods to CRUD folders.
 */
class FolderDao {

    static SQLiteFolder insertFolder(SQLiteModel model, long parentId, String name, FolderType type) {
        SQLiteDatabase db = model.db;
        checkDb(db);
        assert(parentId != 0);
        assert(name != null);
        SQLiteFolder parent = selectFolder(model, parentId);
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
        SQLiteFolder result = new SQLiteFolder(model, id);
        result.path = path;
        result.name = path.getLastSegment();
        result.type = type;
        return result;
    }
    
    /**
     *  Returns the folder by ID.
     */
    static SQLiteFolder selectFolder(SQLiteModel model, long id) {
        SQLiteDatabase db = model.db;
        checkDb(db);
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
        SQLiteFolder folder = getFolder(model, cursor);
        cursor.close();
        return folder;
    }
    
    /**
     *  Returns the folder by path.
     */
    static SQLiteFolder selectFolder(SQLiteModel model, Path path) {
        SQLiteDatabase db = model.db;
        checkDb(db);
        assert(path != null);
        Cursor cursor = db.query(FOLDER_TABLE, 
                null, 
                FULL_NAME_COLUMN + " = ?",
                new String[] {path.toString()},
                null,
                null,
                null);
        if (!cursor.moveToFirst()) {
            return null;
        }
        SQLiteFolder folder = getFolder(model, cursor);
        cursor.close();
        return folder;
    }
    
    /**
     *  Returns root folder by type.
     */
    static SQLiteFolder selectRootFolder(SQLiteModel model) {
        SQLiteDatabase db = model.db;
        checkDb(db);
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
        SQLiteFolder folder = getFolder(model, cursor);
        cursor.close();
        return folder;
    }
    
    /**
     *  Returns cursor over all subfolders.
     */
    static Cursor selectFolders(SQLiteDatabase db, long folderId) {
        checkDb(db);
        assert(folderId != 0);
        return db.query(FOLDER_TABLE, 
                null, 
                FOLDER_ID_COLUMN + " = ?",
                new String[] {String.valueOf(folderId)},
                null,
                null,
                SORT_ORDER_COLUMN + " ASC, " + ID_COLUMN + " ASC");
    }
    
    /**
     *  Returns cursor over all folders of specified type.
     */
    static Cursor selectFolders(SQLiteDatabase db, FolderType type) {
        checkDb(db);
        String selection;
        String[] selectionArgs;
        if (type == null) {
            selection = TYPE_COLUMN + " IS NULL";
            selectionArgs = null;
        } else {
            selection = TYPE_COLUMN + " = ?";
            selectionArgs = new String[] { String.valueOf(type) };
        }
        return db.query(FOLDER_TABLE, 
                null, 
                selection,
                selectionArgs,
                null,
                null,
                SORT_ORDER_COLUMN + " ASC, " + ID_COLUMN + " ASC");
    }
    
    /**
     *  Returns cursor over all actions.
     */
    static Cursor selectActions(SQLiteDatabase db, long folderId) {
        checkDb(db);
        assert(folderId != 0);
        return db.query(
                ACTION_TABLE + " a JOIN " + ACTION_IN_FOLDER_TABLE + " af " +
                "ON (a." + ID_COLUMN + " = af." + ACTION_ID_COLUMN + ")", 
                null, 
                FOLDER_ID_COLUMN + " = ?",
                new String[] {String.valueOf(folderId)},
                null,
                null,
                SORT_ORDER_COLUMN + " ASC, " + ID_COLUMN + " ASC");
    }
    
    /**
     *  Returns the folder from the current cursor position.
     */
    static SQLiteFolder getFolder(SQLiteModel model, Cursor cursor) {
        SQLiteDatabase db = model.db;
        checkDb(db);
        assert(cursor != null);
        assert(!cursor.isClosed() && !cursor.isBeforeFirst() && !cursor.isAfterLast());
        long id = cursor.getLong(cursor.getColumnIndexOrThrow(ID_COLUMN));
        SQLiteFolder result = new SQLiteFolder(model, id);
        Path path = new SimplePath(cursor.getString(cursor.getColumnIndexOrThrow(FULL_NAME_COLUMN)));
        result.path = path;
        result.name = cursor.getString(cursor.getColumnIndexOrThrow(NAME_COLUMN));
        String type = cursor.getString(cursor.getColumnIndexOrThrow(TYPE_COLUMN));
        if (type != null) {
            result.type = FolderType.valueOf(type);
        }
        result.sortOrder = cursor.getLong(cursor.getColumnIndexOrThrow(SORT_ORDER_COLUMN));
        return result;
    }
    
    /**
     *  Updates the folder's parent link.
     *  Updates the folder's full name.
     *  @throws IllegalStateException if the folder to update doesn't exist
     */
    static void updateFolderParent(SQLiteModel model, SQLiteFolder folder, long parentId) throws IllegalStateException {
        SQLiteDatabase db = model.db;
        checkDb(db);
        assert(folder != null);
        assert(folder.id != 0);
        assert(parentId != 0);
        SQLiteFolder parent = selectFolder(model, parentId);
        if (parent == null) {
            parent = selectRootFolder(model);
        }
        assert(parent != null);
        if (parent.getPath().startsWith(folder.getPath())) {
            throw new OuroborosException("cannot move '" + folder + "' to a subfolder of itself");
        }
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
        Cursor cursor = selectFolders(db, folder.id);
        while (cursor.moveToNext()) {   //update subfolders recursively
            SQLiteFolder subfolder = getFolder(model, cursor);
            updateFolderParent(model, subfolder, folder.id);
        }
        cursor.close();
    }
    
    /**
     *  Updates the folder sort order.
     *  @throws IllegalStateException if the folder to update doesn't exist
     */
    static void updateFolderOrder(SQLiteDatabase db, SQLiteFolder folder, int order) throws IllegalStateException {
        checkDb(db);
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
     *  Updates the folder name and type.
     *  @throws IllegalStateException if the folder to update doesn't exist
     */
    static void updateFolder(SQLiteDatabase db, SQLiteFolder folder, String name, FolderType type) throws IllegalStateException {
        checkDb(db);
        assert(folder != null);
        assert(folder.id != 0);
        assert(folder.path != null);
        assert(name != null);
        Path path = folder.path.replaceLastSegment(name);
        ContentValues values = new ContentValues();
        values.put(NAME_COLUMN, name);
        values.put(FULL_NAME_COLUMN, path.toString());
        if (type == null) {
            values.putNull(TYPE_COLUMN);
        } else {
            values.put(TYPE_COLUMN, String.valueOf(type));
        }
        int updated = db.update(FOLDER_TABLE, values, ID_COLUMN + " = ?", 
                new String[] { String.valueOf(folder.id) });
        if (updated == 0) {
            throw new IllegalStateException("no folder with id = " + folder.id + ", update failed");
        }
    }
    
    /**
     *  Deletes the folder.
     */
    static void deleteFolder(SQLiteModel model, long id) {
        SQLiteDatabase db = model.db;
        checkDb(db);
        assert(id != 0);
        db.delete(FOLDER_TABLE, ID_COLUMN + " = ?", new String[] { String.valueOf(id) });
        Cursor cursor = selectFolders(db, id);
        while (cursor.moveToNext()) {
            SQLiteFolder folder = getFolder(model, cursor);
            deleteFolder(model, folder.id);
        }
        cursor.close();
    }
    
    private FolderDao() {
        //avoid instantiation
    }

}
