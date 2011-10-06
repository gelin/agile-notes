package com.lendamage.agilegtd.android.model.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import com.lendamage.agilegtd.model.Folder;
import com.lendamage.agilegtd.model.FolderTree;

/**
 *  Implementation of folder tree.
 */
class SQLiteFolderTree implements FolderTree {

    static final long NONEXISTED_ID = -1;
    
    /** Root node */
    SQLiteTreeNode root;
    /** ID to Node map */
    Map<Long, SQLiteTreeNode> nodeMap = new HashMap<Long, SQLiteTreeNode>();
    /** List of nodes */
    List<SQLiteTreeNode> nodeList = new ArrayList<SQLiteTreeNode>();
    
    SQLiteFolderTree(SQLiteFolder root) {
        initNodeMap(root, 0, NONEXISTED_ID);
        this.root = nodeMap.get(root.id);
        nodeList.add(this.root);
        this.root.setExpanded(true);
    }

    public int getCount() {
        return this.nodeList.size();
    }

    public Node getNodeById(long id) {
        return nodeMap.get(id);
    }

    public Node getNodeByPosition(int position) {
        return this.nodeList.get(position);
    }
    
    public Node getNodeByFolder(Folder folder) {
        if (!(folder instanceof SQLiteFolder)) {
            throw new IllegalArgumentException("cannot operate with not-SQLite folder");
        }
        SQLiteFolder sqlFolder = (SQLiteFolder)folder;
        return getNodeById(sqlFolder.id);
    }
    
    void initNodeMap(SQLiteFolder folder, int depth, long parentId) {
        SQLiteTreeNode node = new SQLiteTreeNode(folder, depth, parentId);
        this.nodeMap.put(folder.id, node);
        for (SQLiteFolder subfolder : node.folders) {
            initNodeMap(subfolder, depth + 1, folder.id);
        }
    }
    
    void expand(SQLiteTreeNode node) {
        int position = this.nodeList.indexOf(node);
        if (position < 0) {
            return;
        }
        //inserting subnodes in reverse order after the current node 
        ListIterator<SQLiteFolder> i = node.folders.listIterator(node.folders.size());
        while (i.hasPrevious()) {
            SQLiteFolder folder = i.previous();
            SQLiteTreeNode subnode = this.nodeMap.get(folder.id);
            this.nodeList.add(position + 1, subnode);
            if (subnode.isExpanded()) {
                expand(subnode);
            }
        }
    }
    
    void collapse(SQLiteTreeNode node) {
        for (SQLiteFolder folder : node.folders) {
            SQLiteTreeNode subnode = this.nodeMap.get(folder.id);
            this.nodeList.remove(subnode);
            collapse(subnode);
        }
    }
    
    class SQLiteTreeNode implements FolderTree.Node {

        SQLiteFolder folder;
        List<SQLiteFolder> folders;
        boolean expanded = false;
        int depth = 0;
        long parentId = NONEXISTED_ID;
        
        public SQLiteTreeNode(SQLiteFolder folder, int depth, long parentId) {
            this.folder = folder;
            this.depth = depth;
            this.folders = ((SQLiteFolderList)folder.getFolders()).folders;
            this.parentId = parentId;
        }
        
        public Folder getFolder() {
            return this.folder;
        }

        public long getId() {
            return this.folder.id;
        }

        public boolean isExpanded() {
            return this.expanded;
        }

        public boolean isLeaf() {
            return this.folders.isEmpty();
        }
        
        public int getDepth() {
            return this.depth;
        }

        public void setExpanded(boolean expand) {
            if (this.expanded == expand) {
                return;
            }
            if (expand) {
                this.expanded = true;
                expand(this);
            } else {
                this.expanded = false;
                collapse(this);
            }
        }
        
        public Node getParent() {
            if (parentId == NONEXISTED_ID) {
                return null;
            }
            return getNodeById(this.parentId);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            SQLiteTreeNode other = (SQLiteTreeNode) obj;
            return this.folder.equals(other.folder);
        }
        
        @Override
        public int hashCode() {
            return this.folder.hashCode();
        }
        
        @Override
        public String toString() {
            return "node" + (this.expanded ? "(*)" : "") + ": " + this.folder.toString();
        }

    }

}
