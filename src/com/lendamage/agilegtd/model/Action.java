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
     *  If the folder is deleted from the set, the assignment of the action with this folder is removed.<br>
     *  If the folder is added to the set, the assignment of the action with this folder is added.<br>
     *  If the set becomes empty (for example, by clear() call), the action is effectively
     *  deleted from model, because it becomes not assigned to any folder.<br>
     *  The order of the folders in the set equals to the "natural" order of the folders - how they are nested and
     *  ordered within the folders tree. The order cannot be changed here.<br>
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
