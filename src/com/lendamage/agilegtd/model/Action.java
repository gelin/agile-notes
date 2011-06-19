package com.lendamage.agilegtd.model;

import java.util.Set;

/**
 *  Agile GTD action.
 *  Each action contains the head and the body.
 *  Each action can belong to any number of folders.
 */
//TODO: removing of actions
public interface Action extends Entity<Action.Editor> {
    
    /**
     *  Get set of folders to which the action belongs.
     *  The returned set is modifiable.
     *  If the folder is deleted from the list, the assignment of the action with this folder is removed.
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
