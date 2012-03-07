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

package com.lendamage.agilegtd.android;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 *  Position where to insert the new item: as the first of the last item in the list
 */
@Deprecated
public enum NewItemPosition {
    FIRST, LAST;
    
    public static final String PREFERENCE_KEY = "new_item_position";

    /**
     *  Gets the option from the shared preferences of the context
     *  @param  context application context
     *  @return null or enumeration instance
     */
    public static NewItemPosition valueOf(Context context) {
        String value = NewItemPosition.FIRST.toString();
        value = PreferenceManager.getDefaultSharedPreferences(context).getString(
                NewItemPosition.PREFERENCE_KEY, value);
        try {
            return NewItemPosition.valueOf(value);
        } catch (Exception e) {
            return null;
        }
    }
}
