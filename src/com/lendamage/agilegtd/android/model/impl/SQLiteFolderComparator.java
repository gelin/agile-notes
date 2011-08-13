package com.lendamage.agilegtd.android.model.impl;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.lendamage.agilegtd.model.Path;

import android.database.sqlite.SQLiteDatabase;

/**
 *  Compares two SQLiteFolders in "natural" order.
 *  The order is the order of traversing the folders tree.
 */
class SQLiteFolderComparator implements Comparator<SQLiteFolder> {

    /** Cache of the folders */
    Map<Path, SQLiteFolder> folders = new HashMap<Path, SQLiteFolder>();
    /** Database connection */
    SQLiteDatabase db;
    
    SQLiteFolderComparator(SQLiteDatabase db) {
        this.db = db;
    }
    
    public int compare(SQLiteFolder folder1, SQLiteFolder folder2) {
        assert(folder1 != null);
        assert(folder2 != null);
        cache(folder1);
        cache(folder2);
        if (folder1.equals(folder2)) {
            return 0;
        }
        Path parentPath1 = folder1.getPath().getParent();
        Path parentPath2 = folder2.getPath().getParent();
        if (parentPath1.equals(parentPath2)) {
            if (folder1.sortOrder < folder2.sortOrder) {
                return -1;
            }
            if (folder1.sortOrder > folder2.sortOrder) {
                return 1;
            }
            return 0;
        }
        SQLiteFolder parent1 = getFolder(parentPath1);
        SQLiteFolder parent2 = getFolder(parentPath2);
        //TODO: more implementation details
        return compare(parent1, parent2);
    }
    
    void cache(SQLiteFolder folder) {
        this.folders.put(folder.getPath(), folder);
    }
    
    SQLiteFolder getFolder(Path path) {
        SQLiteFolder result = this.folders.get(path);
        if (result == null) {
            result = FolderDao.selectFolder(db, path);
        }
        return result;
    }

}
