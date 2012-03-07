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

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.lendamage.agilegtd.model.ModelSettings;

/**
 *  Model settings which holds the settings in the application
 *  SharedPreferences.
 */
public class PreferenceModelSettings implements ModelSettings {

    public static final String NEW_ITEM_POSITION_KEY = "new_item_position";
    
    SharedPreferences preferences;
    
    PreferenceModelSettings(Context context) {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }
    
    @Override
    public NewItemPosition getNewItemPosition() {
        String value = NewItemPosition.FIRST.toString();
        value = this.preferences.getString(NEW_ITEM_POSITION_KEY, value);
        try {
            return NewItemPosition.valueOf(value);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void setNewItemPosition(NewItemPosition position) {
        this.preferences.edit().putString(
                NEW_ITEM_POSITION_KEY, String.valueOf(position)).commit();
    }

}
