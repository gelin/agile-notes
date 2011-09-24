package com.lendamage.agilegtd.model;

/**
 *  Helper interface to construct the read-only tree of folders.
 *  It's useful to display expandable tree of folders.
 *  The tree is built starting from the specified folder - root of tree.
 *  The tree looks like list of nodes.
 *  Each node has it's own id.
 *  The node can be expanded if it contains subfolders/subnodes, 
 *  in this case the total (visible) number of items in the nodelist increases.
 *  The node can be collapsed, in this case the total (visible) number
 *  of items in the nodelist decreases.
 *  The node can be leaf if it doesn't contain sufolders/subnodes.
 */
public interface FolderTree {
    
    /**
     *  Folder tree node.
     */
    public interface Node {
        
        /**
         *  Returns the ID which identifies the node.
         */
        long getId();
        
        /**
         *  Returns true if this node has no subnodes.
         */
        boolean isLeaf();
        
        /**
         *  Returns true if this node currently is expanded and all it's
         *  subnodes are visible.
         */
        boolean isExpanded();
        
        /**
         *  Expands or collapses the specified node.
         *  The number of nodes in the nodelist changes in this case.
         */
        void setExpanded(boolean expand);
        
        /**
         *  Returns the depth of this node.
         *  Root node has the depth = 0, it's subnodes had depth = 1, etc...
         */
        int getDepth();
        
        /**
         *  Returns the folder backed by this node.
         */
        Folder getFolder();
        
    }
    
    /**
     *  Returns the number of expanded (visible) nodes.
     *  Usually only the root node and first-level subnodes are expanded initially.
     */
    int getCount();
    
    /**
     *  Returns the node by ID.
     */
    Node getNodeById(long id);
    
    /**
     *  Returns the node by position in the nodelist.
     */
    Node getNodeByPosition(int position);

}
