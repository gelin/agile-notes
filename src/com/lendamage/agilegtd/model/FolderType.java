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

/**
 *  Enumeration of supported folder types.
 */
public enum FolderType {
    
    ROOT,
    INBOX,
    TRASH,
    COMPLETED,
    PROJECTS,
    PROJECT,
    CONTEXTS,
    CONTEXT,
    PRIORITIES,
    PRIORITY,
    PERSONS,
    PERSON,
    STATUSES,
    STATUS;
    
    /**
     *  Returns the recommended child folder type for this parent folder type
     *  @return recommended type or null 
     */
    public FolderType getChildType() {
        switch (this) {
        case PROJECTS:
            return PROJECT;
        case CONTEXTS:
            return CONTEXT;
        case PRIORITIES:
            return PRIORITY;
        case PERSONS:
            return PERSON;
        case STATUSES:
            return STATUS;
        default:
            return null;
        }
    }

}
