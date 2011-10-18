package com.lendamage.agilegtd.model;

import java.util.List;

/**
 *  Agile GTD model
 */
public interface Model {
    
    /**
     *  Returns the root folder.
     */
    Folder getRootFolder();
    
    /**
     *  Returns the folder by the full name.<br>
     *  If the folder is not found, null is returned.
     */
    Folder getFolder(Path fullPath);
    
    /**
     *  Finds folders with specified type
     *  @return unmodifiable list
     */
    List<Folder> findFolders(FolderType type);
    
    /**
     *  Finds the action.
     *  Actually after the model is closed the instances of actions and folders become non-functional.
     *  This method tries to refresh the Action instance saved from the previous working session.
     *  @param  action  old action instance
     *  @return new action instance corresponding to the old one or null
     */
    Action findAction(Action action);
    
    /**
     *  Closes the model. All unsaved changes are saved, further modifications are errorneous.
     */
    void close();

}
