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
 *  The exception is thrown then the folder cannot be created or moved because it already exists.
 */
public class FolderAlreadyExistsException extends ModelException {

    private static final long serialVersionUID = 9055027206711254309L;
    
    public FolderAlreadyExistsException() {
        super();
    }

    public FolderAlreadyExistsException(String detailMessage,
            Throwable throwable) {
        super(detailMessage, throwable);
    }

    public FolderAlreadyExistsException(String detailMessage) {
        super(detailMessage);
    }

    public FolderAlreadyExistsException(Throwable throwable) {
        super(throwable);
    }

}
