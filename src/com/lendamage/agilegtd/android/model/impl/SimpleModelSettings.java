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

import com.lendamage.agilegtd.model.ModelSettings;

/**
 *  Simple model settings.
 *  Just holds the values in memory.
 */
public class SimpleModelSettings implements ModelSettings {

    private NewItemPosition newItemPosition = NewItemPosition.LAST;

    @Override
    public NewItemPosition getNewItemPosition() {
        return this.newItemPosition;
    }

    @Override
    public void setNewItemPosition(NewItemPosition position) {
        this.newItemPosition = position;
    }
}
