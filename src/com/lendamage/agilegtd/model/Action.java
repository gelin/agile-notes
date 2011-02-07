package com.lendamage.agilegtd.model;

import java.util.Set;

/**
 *  Agile GTD action.
 *  Each action contains the head and the body.
 *  Each action can belong to any number of folders.
 */
public interface Action {
    
    /**
     *  Get set of folders to which the action belongs.
     *  The returned set is modifiable.
     */
    Set<Folder> getFolders();
    
    /**
     *  Get action head.
     */
    String getHead();
    
    /**
     *  Updates the action head.
     */
    void setHead(String head);
    
    /**
     *  Get action body.
     */
    String getBody();
    
    /**
     *  Updates the action body.
     */
    void setBody(String body);

}
