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

import android.preference.PreferenceManager;
import android.test.AndroidTestCase;
import com.lendamage.agilegtd.model.ModelSettings;

public class PreferenceModelSettingsTest extends AndroidTestCase {
    
    public void testSetNewItemPositionFirst() {
        ModelSettings settings = new PreferenceModelSettings(getContext());
        settings.setNewItemPosition(ModelSettings.NewItemPosition.FIRST);
        assertEquals("FIRST", PreferenceManager.getDefaultSharedPreferences(getContext()).
                getString("new_item_position", "LAST"));
    }

    public void testSetNewItemPositionLast() {
        ModelSettings settings = new PreferenceModelSettings(getContext());
        settings.setNewItemPosition(ModelSettings.NewItemPosition.LAST);
        assertEquals("LAST", PreferenceManager.getDefaultSharedPreferences(getContext()).
                getString("new_item_position", "FIRST"));
    }

    public void testGetNewItemPositionFirst() {
        PreferenceManager.getDefaultSharedPreferences(getContext()).edit().
                putString("new_item_position", "FIRST").commit();
        ModelSettings settings = new PreferenceModelSettings(getContext());
        assertEquals(ModelSettings.NewItemPosition.FIRST, settings.getNewItemPosition());
    }

    public void testGetNewItemPositionLast() {
        PreferenceManager.getDefaultSharedPreferences(getContext()).edit().
                putString("new_item_position", "LAST").commit();
        ModelSettings settings = new PreferenceModelSettings(getContext());
        assertEquals(ModelSettings.NewItemPosition.LAST, settings.getNewItemPosition());
    }

}
