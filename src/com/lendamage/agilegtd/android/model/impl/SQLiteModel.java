/*
    Agile GTD. Flexible Android implementation of GTD.
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

package com.lendamage.agilegtd.android.model.impl;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.lendamage.agilegtd.model.Action;
import com.lendamage.agilegtd.model.Folder;
import com.lendamage.agilegtd.model.FolderType;
import com.lendamage.agilegtd.model.Model;
import com.lendamage.agilegtd.model.ModelException;
import com.lendamage.agilegtd.model.ModelSettings;
import com.lendamage.agilegtd.model.Path;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.lendamage.agilegtd.android.model.impl.CommonDao.checkDb;

/**
 *  Model which uses Android's SQLite database.
 */
public class SQLiteModel implements Model {

    /** DB handler */
    transient SQLiteDatabase db;
    /** Root folder */
    SQLiteFolder root;
    /** Settings */
    ModelSettings settings;
    
    /**
     *  Creates the model
     */
    public SQLiteModel(Context context, String dbName) {
        this.settings = new PreferenceModelSettings(context);
        this.db = new SQLiteModelOpenHelper(context, dbName).getWritableDatabase();
        checkDb(this.db);
        this.db.beginTransaction();
        try {
            this.root = FolderDao.selectRootFolder(this.db);
            this.db.setTransactionSuccessful();
        } finally {
            this.db.endTransaction();
        }
        if (this.root == null) {
            throw new ModelException("no root folder");
        }
    }
    
    //@Override
    public Folder getFolder(Path fullPath) {
        Folder result = null;
        checkDb(this.db);
        this.db.beginTransaction();
        try {
            result = FolderDao.selectFolder(this.db, fullPath);
            this.db.setTransactionSuccessful();
        } finally {
            this.db.endTransaction();
        }
        return result;
    }
    
    //@Override
    public Folder getRootFolder() {
        return this.root;
    }
    
    //@Override
    public List<Folder> findFolders(FolderType type) {
        List<SQLiteFolder> result = new ArrayList<SQLiteFolder>();
        checkDb(this.db);
        this.db.beginTransaction();
        try {
            Cursor cursor = FolderDao.selectFolders(this.db, type);
            while (cursor.moveToNext()) {
                result.add(FolderDao.getFolder(this.db, cursor));
            }
            Collections.sort(result, new SQLiteFolderComparator(this.db));
            cursor.close();
            this.db.setTransactionSuccessful();
        } finally {
            this.db.endTransaction();
        }
        List<Folder> typedResult = new ArrayList<Folder>();
        typedResult.addAll(result);
        return Collections.unmodifiableList(typedResult);
    }
    
    //@Override
    public Action findAction(Action action) {
        if (!(action instanceof SQLiteAction)) {
            return null;
        }
        this.db.beginTransaction();
        try {
            Action result = ActionDao.selectAction(this.db, ((SQLiteAction)action).id);
            this.db.setTransactionSuccessful();
            return result;
        } finally {
            this.db.endTransaction();
        }
    }
    
    public void close() {
        this.db.close();
    }

    @Override
    public ModelSettings getSettings() {
        return this.settings;
    }

}
