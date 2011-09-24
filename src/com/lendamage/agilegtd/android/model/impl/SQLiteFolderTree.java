package com.lendamage.agilegtd.android.model.impl;

import java.util.List;

import com.lendamage.agilegtd.model.Folder;
import com.lendamage.agilegtd.model.FolderTree;

/**
 *  Implementation of folder tree.
 */
class SQLiteFolderTree implements FolderTree {

    /** Root node */
    SQLiteTreeNode root;
    /** Current count */
    int count = 1;
    
    SQLiteFolderTree(SQLiteFolder root) {
        this.root = new SQLiteTreeNode(root);
        this.root.setExpanded(true);
    }

    public int getCount() {
        return this.count;
    }

    public Node getNodeById(long id) {
        // TODO Auto-generated method stub
        return null;
    }

    public Node getNodeByPosition(int position) {
        return this.root;
    }
    
    void incrementCount(int inc) {
        this.count += inc;
    }
    
    class SQLiteTreeNode implements FolderTree.Node {

        SQLiteFolder folder;
        List<Folder> folders;
        boolean expanded = false;
        
        public SQLiteTreeNode(SQLiteFolder folder) {
            this.folder = folder;
            this.folders = folder.getFolders();
        }
        
        public Folder getFolder() {
            return this.folder;
        }

        public int getId() {
            // TODO Auto-generated method stub
            return 0;
        }

        public boolean isExpanded() {
            return this.expanded;
        }

        public boolean isLeaf() {
            return this.folders.isEmpty();
        }

        public void setExpanded(boolean expand) {
            if (this.expanded == expand) {
                return;
            }
            if (expand) {
                this.expanded = true;
                incrementCount(this.folders.size());
            } else {
                this.expanded = false;
                incrementCount(-this.folders.size());
            }
        }
        
    }

}
