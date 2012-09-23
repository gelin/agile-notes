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
import com.lendamage.agilegtd.model.FolderType;
import com.lendamage.agilegtd.model.Model;

import java.util.List;

/**
 *  Contains the set of common operations over the model/folders/etc...
 *  It's separated from the base model.
 *  It's separated from UI.
 */
public class Operations {

    Model model;

    public Operations(Model model) {
        this.model = model;
    }

    /**
     *  Moves the folder one step up in the folders list.
     */
    public void moveUpFolder(Folder parentFolder, Folder folder) {
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
    public void moveDownFolder(Folder parentFolder, Folder folder) {
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
    public void moveFirstFolder(Folder parentFolder, Folder folder) {
        List<Folder> folders = parentFolder.getFolders();
        if (!folders.contains(folder)) {
            return;
        }
        folders.add(0, folder);
    }

    /**
     *  Moves the folder to the last position in the folders list.
     */
    public void moveLastFolder(Folder parentFolder, Folder folder) {
        List<Folder> folders = parentFolder.getFolders();
        if (!folders.contains(folder)) {
            return;
        }
        folders.add(folders.size(), folder);
    }

    /**
     *  Returns true if this model has one or more Trash folders.
     */
    public boolean hasTrashFolder() {
        return !this.model.findFolders(FolderType.TRASH).isEmpty();
    }

    /**
     *  Returns true if this folder will be deleted to trash by the delete operation.
     */
    public boolean isDeletableToTrash(Folder folder) {
        if (!hasTrashFolder()) {
            return false;
        }
        if (FolderType.TRASH.equals(folder.getType())) {
            return false;
        }
        Folder parent = this.model.getFolder(folder.getPath().getParent());
        if (FolderType.TRASH.equals(parent.getType())) {
            return false;
        }
        return true;
    }

    /**
     *  Deletes the folder to Trash or permanently removes it from the model.
     */
    public void deleteFolder(Folder folder) {
        if (isDeletableToTrash(folder)) {
            List<Folder> trashFolders = this.model.findFolders(FolderType.TRASH);
            trashFolders.get(0).getFolders().add(folder);
        } else {
            Folder parent = this.model.getFolder(folder.getPath().getParent());
            parent.getFolders().remove(folder);
        }
    }

}
