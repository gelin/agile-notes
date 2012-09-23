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

package com.lendamage.agilegtd.model.operations;

import com.lendamage.agilegtd.model.Folder;

import java.util.List;

/**
 *  Contains the set of common operations over the model/folders/etc...
 *  It's separated from the base model.
 *  It's separated from UI.
 */
public class Operations {

    /**
     *  Moves the folder one step up in the folders list.
     */
    public static void moveUpFolder(Folder parentFolder, Folder folder) {
        List<Folder> folders = parentFolder.getFolders();
        int position = folders.indexOf(folder);
        if (position <= 0) {
            return;
        }
        folders.add(position - 1, folder);
    }

    /**
     *  Moves the folder one step down in the folders list.
     */
    public static void moveDownFolder(Folder parentFolder, Folder folder) {
        List<Folder> folders = parentFolder.getFolders();
        int position = folders.indexOf(folder);
        if (position < 0) {
            return;
        }
        if (position >= folders.size()) {
            return;
        }
        folders.add(position + 1, folder);
    }

    /**
     *  Moves the folder to the first position in the folders list.
     */
    public static void moveFirstFolder(Folder parentFolder, Folder folder) {
        List<Folder> folders = parentFolder.getFolders();
        if (!folders.contains(folder)) {
            return;
        }
        folders.add(0, folder);
    }

    private Operations() {
        //avoid instantiation
    }

}
