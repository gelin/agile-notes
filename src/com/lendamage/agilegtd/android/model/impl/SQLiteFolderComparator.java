package com.lendamage.agilegtd.android.model.impl;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
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
    transient SQLiteDatabase db;
    
    SQLiteFolderComparator(SQLiteDatabase db) {
        this.db = db;
    }
    
    public int compare(SQLiteFolder folder1, SQLiteFolder folder2) {
        assert(folder1 != null);
        assert(folder2 != null);
        cache(folder1);
        cache(folder2);
        //System.out.println("comparing: " + folder1 + " - " + folder2);
        
        //folders are equal
        if (folder1.equals(folder2)) {
            //System.out.println("0 folders are equal: " + folder1 + " - " + folder2);
            return 0;
        }
        
        //one of folder is root
        if (folder1.getPath().isRoot()) {
            //System.out.println("-1 left is root: " + folder1);
            return -1;  //root is always less than any other folder
        }
        if (folder2.getPath().isRoot()) {
            //System.out.println("+1 right is root: " + folder2);
            return 1;  //root is always less than any other folder
        }
        
        //folders are subfolders of the same parent folder, comparing by sort order
        Path parentPath1 = folder1.getPath().getParent();
        Path parentPath2 = folder2.getPath().getParent();
        if (parentPath1.equals(parentPath2)) {
            //System.out.println("folders are subfolders of the same parent folder: " + folder1 + " - " + folder2);
            return compareBySortOrder(folder1, folder2);
        }
        
        //comparing by the path
        //finding the first differs part of the path
        List<String> segments1 = folder1.getPath().getSegments();
        List<String> segments2 = folder2.getPath().getSegments();
        int count = Math.min(segments1.size(), segments2.size());
        Path path1 = new SimplePath("");
        Path path2 = new SimplePath("");
        for (int i = 0; i < count; i++) {
            path1 = path1.addSegment(segments1.get(i));
            path2 = path2.addSegment(segments2.get(i));
            if (path1.equals(path2)) {
                continue;
            }
            //comparing different paths by sort order
            //System.out.println("comparing different paths by sort order: " + path1 + " - " + path2);
            return compareBySortOrder(getFolder(path1), getFolder(path2));
        }
        
        //one path is subpath of another, the longer path is greater
        if (segments1.size() > segments2.size()) {
            //System.out.println("+1 left path is longer");
            return 1;
        }
        if (segments1.size() < segments2.size()) {
            //System.out.println("-1 right path is longer");
            return -1;
        }
        //System.out.println("0 Why?");
        return 0;
    }
    
    int compareBySortOrder(SQLiteFolder folder1, SQLiteFolder folder2) {
        if (folder1.sortOrder < folder2.sortOrder) {
            //System.out.println("-1 left is before");
            return -1;
        }
        if (folder1.sortOrder > folder2.sortOrder) {
            //System.out.println("+1 right is before");
            return 1;
        }
        System.out.println("the same order, comparing by id");
        if (folder1.id < folder2.id) {
            //System.out.println("-1 left is before");
            return -1;
        }
        if (folder1.id > folder2.id) {
            //System.out.println("+1 right is before");
            return 1;
        }
        //System.out.println("the same order?");
        return 0;
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
