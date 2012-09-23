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

import android.test.AndroidTestCase;
import com.lendamage.agilegtd.android.model.impl.SQLiteModel;
import com.lendamage.agilegtd.android.model.impl.SimplePath;
import com.lendamage.agilegtd.model.Folder;
import com.lendamage.agilegtd.model.FolderType;

public class OperationsTest extends AndroidTestCase {
    
    SQLiteModel model;
    
    protected void setUp() {
        getContext().getDatabasePath("agile-gtd-test.db").delete();
        model = new SQLiteModel(getContext(), "agile-gtd-test.db");
    }
    
    public void testMoveUpFolder() {
        Folder folder1 = model.getRootFolder().newFolder("folder1", null);
        Folder folder2 = model.getRootFolder().newFolder("folder2", null);
        Operations.moveUpFolder(model.getRootFolder(), folder2);
        assertEquals(folder2, model.getRootFolder().getFolders().get(0));
        assertEquals(folder1, model.getRootFolder().getFolders().get(1));
    }

    public void testMoveUpFolderNonParent() {
        Folder parent = model.getRootFolder().newFolder("parent", null);
        Folder folder1 = parent.newFolder("folder1", null);
        Folder folder2 = parent.newFolder("folder2", null);
        Operations.moveUpFolder(model.getRootFolder(), folder2);
        assertEquals(folder1, parent.getFolders().get(0));
        assertEquals(folder2, parent.getFolders().get(1));
        assertEquals(parent, model.getRootFolder().getFolders().get(0));
    }

    public void testMoveUpFolderFirst() {
        Folder folder1 = model.getRootFolder().newFolder("folder1", null);
        Folder folder2 = model.getRootFolder().newFolder("folder2", null);
        Operations.moveUpFolder(model.getRootFolder(), folder1);
        assertEquals(folder1, model.getRootFolder().getFolders().get(0));
        assertEquals(folder2, model.getRootFolder().getFolders().get(1));
    }

    public void testMoveDownFolder() {
        Folder folder1 = model.getRootFolder().newFolder("folder1", null);
        Folder folder2 = model.getRootFolder().newFolder("folder2", null);
        Operations.moveDownFolder(model.getRootFolder(), folder1);
        assertEquals(folder2, model.getRootFolder().getFolders().get(0));
        assertEquals(folder1, model.getRootFolder().getFolders().get(1));
    }

    public void testMoveDownFolderNonParent() {
        Folder parent = model.getRootFolder().newFolder("parent", null);
        Folder folder1 = parent.newFolder("folder1", null);
        Folder folder2 = parent.newFolder("folder2", null);
        Operations.moveDownFolder(model.getRootFolder(), folder1);
        assertEquals(folder1, parent.getFolders().get(0));
        assertEquals(folder2, parent.getFolders().get(1));
        assertEquals(parent, model.getRootFolder().getFolders().get(0));
    }

    public void testMoveDownFolderLast() {
        Folder folder1 = model.getRootFolder().newFolder("folder1", null);
        Folder folder2 = model.getRootFolder().newFolder("folder2", null);
        Operations.moveDownFolder(model.getRootFolder(), folder2);
        assertEquals(folder1, model.getRootFolder().getFolders().get(0));
        assertEquals(folder2, model.getRootFolder().getFolders().get(1));
    }

    public void testMoveFirstFolder() {
        Folder folder1 = model.getRootFolder().newFolder("folder1", null);
        Folder folder2 = model.getRootFolder().newFolder("folder2", null);
        Folder folder3 = model.getRootFolder().newFolder("folder3", null);
        Operations.moveFirstFolder(model.getRootFolder(), folder3);
        assertEquals(folder3, model.getRootFolder().getFolders().get(0));
        assertEquals(folder1, model.getRootFolder().getFolders().get(1));
        assertEquals(folder2, model.getRootFolder().getFolders().get(2));
    }

    public void testMoveFirstFolderNonParent() {
        Folder parent = model.getRootFolder().newFolder("parent", null);
        Folder folder1 = parent.newFolder("folder1", null);
        Folder folder2 = parent.newFolder("folder2", null);
        Folder folder3 = parent.newFolder("folder3", null);
        Operations.moveFirstFolder(model.getRootFolder(), folder3);
        assertEquals(folder1, parent.getFolders().get(0));
        assertEquals(folder2, parent.getFolders().get(1));
        assertEquals(folder3, parent.getFolders().get(2));
        assertEquals(parent, model.getRootFolder().getFolders().get(0));
    }

    public void testMoveFirstFolderFirst() {
        Folder folder1 = model.getRootFolder().newFolder("folder1", null);
        Folder folder2 = model.getRootFolder().newFolder("folder2", null);
        Folder folder3 = model.getRootFolder().newFolder("folder3", null);
        Operations.moveFirstFolder(model.getRootFolder(), folder1);
        assertEquals(folder1, model.getRootFolder().getFolders().get(0));
        assertEquals(folder2, model.getRootFolder().getFolders().get(1));
        assertEquals(folder3, model.getRootFolder().getFolders().get(2));
    }

    public void testMoveLastFolder() {
        Folder folder1 = model.getRootFolder().newFolder("folder1", null);
        Folder folder2 = model.getRootFolder().newFolder("folder2", null);
        Folder folder3 = model.getRootFolder().newFolder("folder3", null);
        Operations.moveLastFolder(model.getRootFolder(), folder1);
        assertEquals(folder2, model.getRootFolder().getFolders().get(0));
        assertEquals(folder3, model.getRootFolder().getFolders().get(1));
        assertEquals(folder1, model.getRootFolder().getFolders().get(2));
    }

    public void testMoveLastFolderNonParent() {
        Folder parent = model.getRootFolder().newFolder("parent", null);
        Folder folder1 = parent.newFolder("folder1", null);
        Folder folder2 = parent.newFolder("folder2", null);
        Folder folder3 = parent.newFolder("folder3", null);
        Operations.moveLastFolder(model.getRootFolder(), folder1);
        assertEquals(folder1, parent.getFolders().get(0));
        assertEquals(folder2, parent.getFolders().get(1));
        assertEquals(folder3, parent.getFolders().get(2));
        assertEquals(parent, model.getRootFolder().getFolders().get(0));
    }

    public void testMoveLastFolderLast() {
        Folder folder1 = model.getRootFolder().newFolder("folder1", null);
        Folder folder2 = model.getRootFolder().newFolder("folder2", null);
        Folder folder3 = model.getRootFolder().newFolder("folder3", null);
        Operations.moveLastFolder(model.getRootFolder(), folder3);
        assertEquals(folder1, model.getRootFolder().getFolders().get(0));
        assertEquals(folder2, model.getRootFolder().getFolders().get(1));
        assertEquals(folder3, model.getRootFolder().getFolders().get(2));
    }

    public void testHasTrashFolder() {
        assertFalse(Operations.hasTrashFolder(model));
        model.getRootFolder().newFolder("trash", FolderType.TRASH);
        assertTrue(Operations.hasTrashFolder(model));
    }

    public void testIsDeletableToTrashNoTrash() {
        Folder folder = model.getRootFolder().newFolder("folder", null);
        assertFalse(Operations.isDeletableToTrash(model, folder));
    }

    public void testIsDeletableToTrash() {
        model.getRootFolder().newFolder("trash", FolderType.TRASH);
        Folder folder = model.getRootFolder().newFolder("folder", null);
        assertTrue(Operations.isDeletableToTrash(model, folder));
    }

    public void testIsDeletableToTrashTrash() {
        Folder trash = model.getRootFolder().newFolder("trash", FolderType.TRASH);
        assertFalse(Operations.isDeletableToTrash(model, trash));
    }

    public void testIsDeletableToTrashParentTrash() {
        Folder trash = model.getRootFolder().newFolder("trash", FolderType.TRASH);
        Folder folder = trash.newFolder("folder", null);
        assertFalse(Operations.isDeletableToTrash(model, folder));
    }

    public void testDeleteFolderFromModel() {
        Folder parent = model.getRootFolder().newFolder("parent", null);
        parent.newFolder("child", null);
        Operations.deleteFolder(model, parent);
        assertNull(model.getFolder(new SimplePath("parent")));
        assertNull(model.getFolder(new SimplePath("parent/child")));
    }

    public void testDeleteFolderToTrash() {
        model.getRootFolder().newFolder("trash", FolderType.TRASH);
        Folder parent = model.getRootFolder().newFolder("parent", null);
        Folder child = parent.newFolder("child", null);
        Operations.deleteFolder(model, parent);
        assertNull(model.getFolder(new SimplePath("parent")));
        assertNull(model.getFolder(new SimplePath("parent/child")));
        assertEquals(parent, model.getFolder(new SimplePath("trash/parent")));
        assertEquals(child, model.getFolder(new SimplePath("trash/parent/child")));
    }

    public void testDeleteFolderTrash() {
        Folder trash = model.getRootFolder().newFolder("trash", FolderType.TRASH);
        Operations.deleteFolder(model, trash);
        assertNull(model.getFolder(new SimplePath("trash")));
    }

    public void testDeleteFolderParentTrash() {
        Folder trash = model.getRootFolder().newFolder("trash", FolderType.TRASH);
        Folder folder = trash.newFolder("folder", null);
        Operations.deleteFolder(model, folder);
        assertNull(model.getFolder(new SimplePath("trash/folder")));
    }
    
}
