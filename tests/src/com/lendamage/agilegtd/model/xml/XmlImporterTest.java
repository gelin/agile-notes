package com.lendamage.agilegtd.model.xml;

import android.test.AndroidTestCase;

import com.lendamage.agilegtd.android.model.impl.SQLiteModel;
import com.lendamage.agilegtd.android.model.impl.SimplePath;
import com.lendamage.agilegtd.model.Action;
import com.lendamage.agilegtd.model.Folder;
import com.lendamage.agilegtd.model.FolderType;

import static com.lendamage.agilegtd.model.ModelSettings.NewItemPosition.FIRST;
import static com.lendamage.agilegtd.model.ModelSettings.NewItemPosition.LAST;

public class XmlImporterTest extends AndroidTestCase {
    
    SQLiteModel model;
    
    protected void setUp() {
        getContext().getDatabasePath("agile-gtd-test.db").delete();
        model = new SQLiteModel(getContext(), "agile-gtd-test.db");
        model.getSettings().setNewItemPosition(LAST);
    }
    
    public void testImport() {
        XmlImporter.importModel(model, getClass().getResourceAsStream(("agilegtd.xml")));
        
        Folder folder1 = model.getFolder(new SimplePath("folder1"));
        assertNotNull(folder1);
        assertEquals("folder1", folder1.getName());
        assertEquals(FolderType.PROJECTS, folder1.getType());
        assertEquals(2, folder1.getFolders().size());
        assertEquals(0, folder1.getActions().size());
        
        Folder folder2 = model.getFolder(new SimplePath("folder2"));
        assertNotNull(folder2);
        assertEquals("folder2", folder2.getName());
        assertNull(folder2.getType());
        assertEquals(0, folder2.getFolders().size());
        assertEquals(1, folder2.getActions().size());
        
        Folder folder3 = model.getFolder(new SimplePath("folder3"));
        assertNotNull(folder3);
        assertEquals("folder3", folder3.getName());
        assertNull(folder3.getType());
        assertEquals(0, folder3.getFolders().size());
        assertEquals(2, folder3.getActions().size());
        
        Folder subfolder1 = model.getFolder(new SimplePath("folder1/subfolder1"));
        assertNotNull(subfolder1);
        assertEquals("subfolder1", subfolder1.getName());
        assertEquals(FolderType.PROJECT, subfolder1.getType());
        assertEquals(0, subfolder1.getFolders().size());
        assertEquals(2, subfolder1.getActions().size());
        
        Folder subfolder2 = model.getFolder(new SimplePath("folder1/subfolder2"));
        assertNotNull(subfolder2);
        assertEquals("subfolder2", subfolder2.getName());
        assertEquals(FolderType.PROJECT, subfolder2.getType());
        assertEquals(0, subfolder2.getFolders().size());
        assertEquals(1, subfolder2.getActions().size());
        
        Action action1 = subfolder1.getActions().get(0);
        assertEquals("action1", action1.getHead());
        assertEquals("action1 line1\naction1 line2", action1.getBody());
        assertEquals(action1, folder2.getActions().get(0));
        assertEquals(2, action1.getFolders().size());
        
        Action action2 = subfolder1.getActions().get(1);
        assertEquals("action2", action2.getHead());
        assertEquals("action2", action2.getBody());
        assertEquals(action2, folder3.getActions().get(0));
        assertEquals(2, action2.getFolders().size());
        
        Action action3 = subfolder2.getActions().get(0);
        assertEquals("action3", action3.getHead());
        assertEquals("action3", action3.getBody());
        assertEquals(action3, folder3.getActions().get(1));
        assertEquals(2, action3.getFolders().size());
    }

    public void testImportToFirst() {
        model.getSettings().setNewItemPosition(FIRST);
        XmlImporter.importModel(model, getClass().getResourceAsStream(("agilegtd.xml")));

        Folder folder1 = model.getFolder(new SimplePath("folder1"));
        assertNotNull(folder1);
        assertEquals("folder1", folder1.getName());
        assertEquals(FolderType.PROJECTS, folder1.getType());
        assertEquals(2, folder1.getFolders().size());
        assertEquals(0, folder1.getActions().size());

        Folder folder2 = model.getFolder(new SimplePath("folder2"));
        assertNotNull(folder2);
        assertEquals("folder2", folder2.getName());
        assertNull(folder2.getType());
        assertEquals(0, folder2.getFolders().size());
        assertEquals(1, folder2.getActions().size());

        Folder folder3 = model.getFolder(new SimplePath("folder3"));
        assertNotNull(folder3);
        assertEquals("folder3", folder3.getName());
        assertNull(folder3.getType());
        assertEquals(0, folder3.getFolders().size());
        assertEquals(2, folder3.getActions().size());

        Folder subfolder1 = model.getFolder(new SimplePath("folder1/subfolder1"));
        assertNotNull(subfolder1);
        assertEquals("subfolder1", subfolder1.getName());
        assertEquals(FolderType.PROJECT, subfolder1.getType());
        assertEquals(0, subfolder1.getFolders().size());
        assertEquals(2, subfolder1.getActions().size());

        Folder subfolder2 = model.getFolder(new SimplePath("folder1/subfolder2"));
        assertNotNull(subfolder2);
        assertEquals("subfolder2", subfolder2.getName());
        assertEquals(FolderType.PROJECT, subfolder2.getType());
        assertEquals(0, subfolder2.getFolders().size());
        assertEquals(1, subfolder2.getActions().size());

        Action action1 = subfolder1.getActions().get(0);
        assertEquals("action1", action1.getHead());
        assertEquals("action1 line1\naction1 line2", action1.getBody());
        assertEquals(action1, folder2.getActions().get(0));
        assertEquals(2, action1.getFolders().size());

        Action action2 = subfolder1.getActions().get(1);
        assertEquals("action2", action2.getHead());
        assertEquals("action2", action2.getBody());
        assertEquals(action2, folder3.getActions().get(0));
        assertEquals(2, action2.getFolders().size());

        Action action3 = subfolder2.getActions().get(0);
        assertEquals("action3", action3.getHead());
        assertEquals("action3", action3.getBody());
        assertEquals(action3, folder3.getActions().get(1));
        assertEquals(2, action3.getFolders().size());
    }
    
    public void testImport2() {
        XmlImporter.importModel(model, getClass().getResourceAsStream(("agilegtd2.xml")));
        
        assertNotNull(model.getFolder(new SimplePath("Persons")));
        assertNotNull(model.getFolder(new SimplePath("Persons/Svetik")));
        assertNotNull(model.getFolder(new SimplePath("Test")));
        assertNotNull(model.getFolder(new SimplePath("Test/SubTest")));
        assertNotNull(model.getFolder(new SimplePath("Test/Subtest2")));
        assertNotNull(model.getFolder(new SimplePath("Test/Subtest3")));
        assertNotNull(model.getFolder(new SimplePath("Test/Subtest3/Subsub")));
        assertNotNull(model.getFolder(new SimplePath("Restore test")));
        
        XmlImporter.importModel(model, getClass().getResourceAsStream(("agilegtd2.xml")));
        
        assertNotNull(model.getFolder(new SimplePath("Persons")));
        assertNotNull(model.getFolder(new SimplePath("Persons/Svetik")));
        assertNotNull(model.getFolder(new SimplePath("Test")));
        assertNotNull(model.getFolder(new SimplePath("Test/SubTest")));
        assertNotNull(model.getFolder(new SimplePath("Test/Subtest2")));
        assertNotNull(model.getFolder(new SimplePath("Test/Subtest3")));
        assertNotNull(model.getFolder(new SimplePath("Test/Subtest3/Subsub")));
        assertNotNull(model.getFolder(new SimplePath("Restore test")));
    }
    
}
