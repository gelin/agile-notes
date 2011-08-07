package com.lendamage.agilegtd.model;

import java.util.Set;

/**
 *  Agile GTD action.<br>
 *  Each action contains the head and the body.<br>
 *  Each action can belong to any number of folders.
 */
public interface Action extends Entity<Action.Editor> {
    
    /**
     *  Get set of folders to which the action belongs.<br>
     *  The returned set is modifiable.<br>
     *  If the folder is deleted from the list, the assignment of the action with this folder is removed.<br>
     *  If the folder is added to the list, the assignment of the action with this folder is added. 
     */
    Set<Folder> getFolders();
    
    /**
     *  Get action head.
     */
    String getHead();
    
    /**
     *  Get action body.
     */
    String getBody();
    
    public interface Editor extends Entity.Editor {

        /**
         *  Updates the action head.
         */
        Editor setHead(String head);

        /**
         *  Updates the action body.
         */
        Editor setBody(String body);

    }

}
