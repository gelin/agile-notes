/*
 * Agile GTD. Flexible Android implementation of GTD.
 * Copyright (C) 2012  Denis Nelubin
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.lendamage.agilegtd.model;

/**
 *  Model settings.
 *  Can be changed on runtime, during the model life.
 */
public interface ModelSettings {

    /**
     *  Position where to insert the new action or folder: as the first of the last item in the list
     */
    public static enum NewItemPosition {
        FIRST, LAST;
    }

    /**
     *  Returns the position where to insert the new action or folder.
     */
    NewItemPosition getNewItemPosition();

    /**
     *  Sets the position where to insert the new action or folder.
     *  This parameter affects the creation of the folder or the action
     *  and default (without position parameter) adding to the folders.
     */
    void setNewItemPosition(NewItemPosition position);

}
