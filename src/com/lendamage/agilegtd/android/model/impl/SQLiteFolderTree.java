package com.lendamage.agilegtd.android.model.impl;

import java.util.List;

import com.lendamage.agilegtd.model.Folder;
import com.lendamage.agilegtd.model.FolderTree;

/**
 *  Implementation of folder tree.
 */
class SQLiteFolderTree implements FolderTree {

    /** Root folder */
    SQLiteFolder root;
    
    SQLiteFolderTree(SQLiteFolder root) {
        this.root = root;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return 1;
    }

    public Node getNodeById(long id) {
        // TODO Auto-generated method stub
        return null;
    }

    public Node getNodeByPosition(int position) {
        return new SQLiteTreeNode(this.root);
    }
    
    static class SQLiteTreeNode implements FolderTree.Node {

        SQLiteFolder folder;
        List<Folder> folders;
        
        public SQLiteTreeNode(SQLiteFolder folder) {
            this.folder = folder;
            this.folders = folder.getFolders();
        }
        
        public Folder getFolder() {
            // TODO Auto-generated method stub
            return null;
        }

        public int getId() {
            // TODO Auto-generated method stub
            return 0;
        }

        public boolean isExpanded() {
            // TODO Auto-generated method stub
            return false;
        }

        public boolean isLeaf() {
            return this.folders.isEmpty();
        }

        public void setExpanded(boolean expand) {
            // TODO Auto-generated method stub
            
        }
        
    }

}
