package com.lendamage.agilegtd.model.xml;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import android.os.Environment;
import android.test.AndroidTestCase;

import com.lendamage.agilegtd.android.model.impl.SQLiteModel;
import com.lendamage.agilegtd.android.model.impl.SimplePath;
import com.lendamage.agilegtd.model.Action;
import com.lendamage.agilegtd.model.Folder;
import com.lendamage.agilegtd.model.FolderType;
import com.lendamage.agilegtd.model.ModelException;

public class XmlExporterTest extends AndroidTestCase {
    
    SQLiteModel model;
    
    protected void setUp() {
        getContext().getDatabasePath("agile-gtd-test.db").delete();
        model = new SQLiteModel(getContext(), "agile-gtd-test.db");
    }
    
    File getBackupFile() throws IOException {
        File storage = Environment.getExternalStorageDirectory();
        File agilegtdFolder = new File(storage, "agilegtd");
        agilegtdFolder.mkdirs();
        File backup = new File(agilegtdFolder, "agilegtd.xml");
        return backup;
    }
    
    public void testExport() throws ModelException, IOException {
        {
        Folder folder1 = model.getRootFolder().newFolder("folder1", FolderType.PROJECTS);
        Folder folder2 = model.getRootFolder().newFolder("folder2", null);
        Folder folder3 = model.getRootFolder().newFolder("folder3", null);
        Folder subfolder1 = folder1.newFolder("subfolder1", FolderType.PROJECT);
        Folder subfolder2 = folder1.newFolder("subfolder2", FolderType.PROJECT);
        Action action1 = subfolder1.newAction("action1", "action1 line1\naction1 line2");
        Action action2 = subfolder1.newAction("action2", "action2");
        Action action3 = subfolder2.newAction("action3", "action3");
        folder2.getActions().add(action1);
        folder3.getActions().add(action2);
        folder3.getActions().add(action3);
        }
        
        XmlExporter.exportModel(model, new FileOutputStream(getBackupFile()));
        setUp();
        XmlImporter.importModel(model, new FileInputStream(getBackupFile()));
        
        {
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
    }
    
    public void testEncoding() throws UnsupportedEncodingException {
        model.getRootFolder().newFolder("абв", null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XmlExporter.exportModel(model, out);
        String result = out.toString("utf8");
        System.out.println(result);
        assertTrue(result.startsWith("<?xml version='1.0' encoding='utf-8'"));
        assertTrue(result.contains("абв"));
    }
    
}
