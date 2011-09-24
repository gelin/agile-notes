package com.lendamage.agilegtd.android.model.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lendamage.agilegtd.model.Folder;
import com.lendamage.agilegtd.model.FolderTree;

/**
 *  Implementation of folder tree.
 */
class SQLiteFolderTree implements FolderTree {

    /** Root node */
    SQLiteTreeNode root;
    /** ID to Node map */
    Map<Long, SQLiteTreeNode> nodeMap = new HashMap<Long, SQLiteTreeNode>();
    /** List of nodes */
    List<SQLiteTreeNode> nodeList = new ArrayList<SQLiteTreeNode>();
    
    SQLiteFolderTree(SQLiteFolder root) {
        initNodeMap(root);
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
        return this.root;
    }
    
    void initNodeMap(SQLiteFolder folder) {
        SQLiteTreeNode node = new SQLiteTreeNode(folder);
        this.nodeMap.put(folder.id, node);
        for (SQLiteFolder subfolder : node.folders) {
            initNodeMap(subfolder);
        }
    }
    
    void expand(SQLiteTreeNode node) {
        for (SQLiteFolder folder : node.folders) {
            SQLiteTreeNode subnode = this.nodeMap.get(folder.id);
            this.nodeList.add(subnode);
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
        
        public SQLiteTreeNode(SQLiteFolder folder) {
            this.folder = folder;
            this.folders = ((SQLiteFolderList)folder.getFolders()).folders;
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
            // TODO Auto-generated method stub
            return 0;
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
