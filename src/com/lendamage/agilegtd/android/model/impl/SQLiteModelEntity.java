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

package com.lendamage.agilegtd.android.model.impl;

import android.database.sqlite.SQLiteDatabase;
import com.lendamage.agilegtd.model.ModelSettings;

/**
 *  Fields and methods common to all entities,
 *  such as link to model and SQLiteDatabase.
 */
public abstract class SQLiteModelEntity {
    /** Link to model */
    transient SQLiteModel model;
    /** DB handler */
    transient SQLiteDatabase db;
    /** ID in the database */
    final long id;
    /** True if New item should be added to the first */
    boolean newItemPositionFirst = false;

    public SQLiteModelEntity(SQLiteModel model, long id) {
        this.db = model.db;
        this.model = model;
        this.id = id;
        updateSettings(model.getSettings());
    }

    /**
     *  Updated the folder internals based on the model settings.
     */
    void updateSettings(ModelSettings settings) {
        this.newItemPositionFirst = ModelSettings.NewItemPosition.FIRST.equals(settings.getNewItemPosition());
    }
}
