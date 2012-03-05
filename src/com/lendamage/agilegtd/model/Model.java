/*
    Agile GTD. Flexible implementation of GTD.
    Copyright (C) 2011  Denis Nelubin

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

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

    /**
     *  Returns the settings of the model.
     *  Settings can be changed during the model life.
     */
    ModelSettings getSettings();

}
